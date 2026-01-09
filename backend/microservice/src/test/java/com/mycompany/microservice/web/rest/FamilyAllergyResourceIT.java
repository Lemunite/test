package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.FamilyAllergyAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.FamilyAllergy;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.domain.enumeration.AllergyType;
import com.mycompany.microservice.repository.FamilyAllergyRepository;
import com.mycompany.microservice.service.dto.FamilyAllergyDTO;
import com.mycompany.microservice.service.mapper.FamilyAllergyMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FamilyAllergyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamilyAllergyResourceIT {

    private static final AllergyType DEFAULT_TYPE = AllergyType.DRUGS;
    private static final AllergyType UPDATED_TYPE = AllergyType.CHEMICALS;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_AFFECTED_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_AFFECTED_PERSON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/family-allergies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FamilyAllergyRepository familyAllergyRepository;

    @Autowired
    private FamilyAllergyMapper familyAllergyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilyAllergyMockMvc;

    private FamilyAllergy familyAllergy;

    private FamilyAllergy insertedFamilyAllergy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyAllergy createEntity(EntityManager em) {
        FamilyAllergy familyAllergy = new FamilyAllergy()
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .affectedPerson(DEFAULT_AFFECTED_PERSON);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        familyAllergy.setPatient(patient);
        return familyAllergy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyAllergy createUpdatedEntity(EntityManager em) {
        FamilyAllergy updatedFamilyAllergy = new FamilyAllergy()
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .affectedPerson(UPDATED_AFFECTED_PERSON);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedFamilyAllergy.setPatient(patient);
        return updatedFamilyAllergy;
    }

    @BeforeEach
    void initTest() {
        familyAllergy = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFamilyAllergy != null) {
            familyAllergyRepository.delete(insertedFamilyAllergy);
            insertedFamilyAllergy = null;
        }
    }

    @Test
    @Transactional
    void createFamilyAllergy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FamilyAllergy
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(familyAllergy);
        var returnedFamilyAllergyDTO = om.readValue(
            restFamilyAllergyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyAllergyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FamilyAllergyDTO.class
        );

        // Validate the FamilyAllergy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFamilyAllergy = familyAllergyMapper.toEntity(returnedFamilyAllergyDTO);
        assertFamilyAllergyUpdatableFieldsEquals(returnedFamilyAllergy, getPersistedFamilyAllergy(returnedFamilyAllergy));

        insertedFamilyAllergy = returnedFamilyAllergy;
    }

    @Test
    @Transactional
    void createFamilyAllergyWithExistingId() throws Exception {
        // Create the FamilyAllergy with an existing ID
        familyAllergy.setId(1L);
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(familyAllergy);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyAllergyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyAllergyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FamilyAllergy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        familyAllergy.setType(null);

        // Create the FamilyAllergy, which fails.
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(familyAllergy);

        restFamilyAllergyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyAllergyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAffectedPersonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        familyAllergy.setAffectedPerson(null);

        // Create the FamilyAllergy, which fails.
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(familyAllergy);

        restFamilyAllergyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyAllergyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFamilyAllergies() throws Exception {
        // Initialize the database
        insertedFamilyAllergy = familyAllergyRepository.saveAndFlush(familyAllergy);

        // Get all the familyAllergyList
        restFamilyAllergyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyAllergy.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].affectedPerson").value(hasItem(DEFAULT_AFFECTED_PERSON)));
    }

    @Test
    @Transactional
    void getFamilyAllergy() throws Exception {
        // Initialize the database
        insertedFamilyAllergy = familyAllergyRepository.saveAndFlush(familyAllergy);

        // Get the familyAllergy
        restFamilyAllergyMockMvc
            .perform(get(ENTITY_API_URL_ID, familyAllergy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(familyAllergy.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.affectedPerson").value(DEFAULT_AFFECTED_PERSON));
    }

    @Test
    @Transactional
    void getNonExistingFamilyAllergy() throws Exception {
        // Get the familyAllergy
        restFamilyAllergyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFamilyAllergy() throws Exception {
        // Initialize the database
        insertedFamilyAllergy = familyAllergyRepository.saveAndFlush(familyAllergy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the familyAllergy
        FamilyAllergy updatedFamilyAllergy = familyAllergyRepository.findById(familyAllergy.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFamilyAllergy are not directly saved in db
        em.detach(updatedFamilyAllergy);
        updatedFamilyAllergy.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).affectedPerson(UPDATED_AFFECTED_PERSON);
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(updatedFamilyAllergy);

        restFamilyAllergyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyAllergyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(familyAllergyDTO))
            )
            .andExpect(status().isOk());

        // Validate the FamilyAllergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFamilyAllergyToMatchAllProperties(updatedFamilyAllergy);
    }

    @Test
    @Transactional
    void putNonExistingFamilyAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyAllergy.setId(longCount.incrementAndGet());

        // Create the FamilyAllergy
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(familyAllergy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyAllergyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyAllergyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(familyAllergyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyAllergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamilyAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyAllergy.setId(longCount.incrementAndGet());

        // Create the FamilyAllergy
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(familyAllergy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyAllergyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(familyAllergyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyAllergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamilyAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyAllergy.setId(longCount.incrementAndGet());

        // Create the FamilyAllergy
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(familyAllergy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyAllergyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyAllergyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyAllergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilyAllergyWithPatch() throws Exception {
        // Initialize the database
        insertedFamilyAllergy = familyAllergyRepository.saveAndFlush(familyAllergy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the familyAllergy using partial update
        FamilyAllergy partialUpdatedFamilyAllergy = new FamilyAllergy();
        partialUpdatedFamilyAllergy.setId(familyAllergy.getId());

        partialUpdatedFamilyAllergy.description(UPDATED_DESCRIPTION).affectedPerson(UPDATED_AFFECTED_PERSON);

        restFamilyAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyAllergy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFamilyAllergy))
            )
            .andExpect(status().isOk());

        // Validate the FamilyAllergy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFamilyAllergyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFamilyAllergy, familyAllergy),
            getPersistedFamilyAllergy(familyAllergy)
        );
    }

    @Test
    @Transactional
    void fullUpdateFamilyAllergyWithPatch() throws Exception {
        // Initialize the database
        insertedFamilyAllergy = familyAllergyRepository.saveAndFlush(familyAllergy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the familyAllergy using partial update
        FamilyAllergy partialUpdatedFamilyAllergy = new FamilyAllergy();
        partialUpdatedFamilyAllergy.setId(familyAllergy.getId());

        partialUpdatedFamilyAllergy.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).affectedPerson(UPDATED_AFFECTED_PERSON);

        restFamilyAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyAllergy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFamilyAllergy))
            )
            .andExpect(status().isOk());

        // Validate the FamilyAllergy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFamilyAllergyUpdatableFieldsEquals(partialUpdatedFamilyAllergy, getPersistedFamilyAllergy(partialUpdatedFamilyAllergy));
    }

    @Test
    @Transactional
    void patchNonExistingFamilyAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyAllergy.setId(longCount.incrementAndGet());

        // Create the FamilyAllergy
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(familyAllergy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familyAllergyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(familyAllergyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyAllergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamilyAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyAllergy.setId(longCount.incrementAndGet());

        // Create the FamilyAllergy
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(familyAllergy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(familyAllergyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyAllergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamilyAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyAllergy.setId(longCount.incrementAndGet());

        // Create the FamilyAllergy
        FamilyAllergyDTO familyAllergyDTO = familyAllergyMapper.toDto(familyAllergy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyAllergyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(familyAllergyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyAllergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamilyAllergy() throws Exception {
        // Initialize the database
        insertedFamilyAllergy = familyAllergyRepository.saveAndFlush(familyAllergy);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the familyAllergy
        restFamilyAllergyMockMvc
            .perform(delete(ENTITY_API_URL_ID, familyAllergy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return familyAllergyRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected FamilyAllergy getPersistedFamilyAllergy(FamilyAllergy familyAllergy) {
        return familyAllergyRepository.findById(familyAllergy.getId()).orElseThrow();
    }

    protected void assertPersistedFamilyAllergyToMatchAllProperties(FamilyAllergy expectedFamilyAllergy) {
        assertFamilyAllergyAllPropertiesEquals(expectedFamilyAllergy, getPersistedFamilyAllergy(expectedFamilyAllergy));
    }

    protected void assertPersistedFamilyAllergyToMatchUpdatableProperties(FamilyAllergy expectedFamilyAllergy) {
        assertFamilyAllergyAllUpdatablePropertiesEquals(expectedFamilyAllergy, getPersistedFamilyAllergy(expectedFamilyAllergy));
    }
}
