package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.AllergyAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.Allergy;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.domain.enumeration.AllergyType;
import com.mycompany.microservice.repository.AllergyRepository;
import com.mycompany.microservice.service.dto.AllergyDTO;
import com.mycompany.microservice.service.mapper.AllergyMapper;
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
 * Integration tests for the {@link AllergyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AllergyResourceIT {

    private static final AllergyType DEFAULT_TYPE = AllergyType.DRUGS;
    private static final AllergyType UPDATED_TYPE = AllergyType.CHEMICALS;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/allergies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AllergyRepository allergyRepository;

    @Autowired
    private AllergyMapper allergyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAllergyMockMvc;

    private Allergy allergy;

    private Allergy insertedAllergy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allergy createEntity(EntityManager em) {
        Allergy allergy = new Allergy().type(DEFAULT_TYPE).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        allergy.setPatient(patient);
        return allergy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allergy createUpdatedEntity(EntityManager em) {
        Allergy updatedAllergy = new Allergy().type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedAllergy.setPatient(patient);
        return updatedAllergy;
    }

    @BeforeEach
    void initTest() {
        allergy = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAllergy != null) {
            allergyRepository.delete(insertedAllergy);
            insertedAllergy = null;
        }
    }

    @Test
    @Transactional
    void createAllergy() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Allergy
        AllergyDTO allergyDTO = allergyMapper.toDto(allergy);
        var returnedAllergyDTO = om.readValue(
            restAllergyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AllergyDTO.class
        );

        // Validate the Allergy in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAllergy = allergyMapper.toEntity(returnedAllergyDTO);
        assertAllergyUpdatableFieldsEquals(returnedAllergy, getPersistedAllergy(returnedAllergy));

        insertedAllergy = returnedAllergy;
    }

    @Test
    @Transactional
    void createAllergyWithExistingId() throws Exception {
        // Create the Allergy with an existing ID
        allergy.setId(1L);
        AllergyDTO allergyDTO = allergyMapper.toDto(allergy);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllergyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Allergy in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        allergy.setType(null);

        // Create the Allergy, which fails.
        AllergyDTO allergyDTO = allergyMapper.toDto(allergy);

        restAllergyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAllergies() throws Exception {
        // Initialize the database
        insertedAllergy = allergyRepository.saveAndFlush(allergy);

        // Get all the allergyList
        restAllergyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allergy.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getAllergy() throws Exception {
        // Initialize the database
        insertedAllergy = allergyRepository.saveAndFlush(allergy);

        // Get the allergy
        restAllergyMockMvc
            .perform(get(ENTITY_API_URL_ID, allergy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allergy.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingAllergy() throws Exception {
        // Get the allergy
        restAllergyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAllergy() throws Exception {
        // Initialize the database
        insertedAllergy = allergyRepository.saveAndFlush(allergy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allergy
        Allergy updatedAllergy = allergyRepository.findById(allergy.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAllergy are not directly saved in db
        em.detach(updatedAllergy);
        updatedAllergy.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);
        AllergyDTO allergyDTO = allergyMapper.toDto(updatedAllergy);

        restAllergyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allergyDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Allergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAllergyToMatchAllProperties(updatedAllergy);
    }

    @Test
    @Transactional
    void putNonExistingAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergy.setId(longCount.incrementAndGet());

        // Create the Allergy
        AllergyDTO allergyDTO = allergyMapper.toDto(allergy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allergyDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergy.setId(longCount.incrementAndGet());

        // Create the Allergy
        AllergyDTO allergyDTO = allergyMapper.toDto(allergy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allergyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergy.setId(longCount.incrementAndGet());

        // Create the Allergy
        AllergyDTO allergyDTO = allergyMapper.toDto(allergy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Allergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAllergyWithPatch() throws Exception {
        // Initialize the database
        insertedAllergy = allergyRepository.saveAndFlush(allergy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allergy using partial update
        Allergy partialUpdatedAllergy = new Allergy();
        partialUpdatedAllergy.setId(allergy.getId());

        restAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllergy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllergy))
            )
            .andExpect(status().isOk());

        // Validate the Allergy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllergyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAllergy, allergy), getPersistedAllergy(allergy));
    }

    @Test
    @Transactional
    void fullUpdateAllergyWithPatch() throws Exception {
        // Initialize the database
        insertedAllergy = allergyRepository.saveAndFlush(allergy);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allergy using partial update
        Allergy partialUpdatedAllergy = new Allergy();
        partialUpdatedAllergy.setId(allergy.getId());

        partialUpdatedAllergy.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllergy.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllergy))
            )
            .andExpect(status().isOk());

        // Validate the Allergy in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllergyUpdatableFieldsEquals(partialUpdatedAllergy, getPersistedAllergy(partialUpdatedAllergy));
    }

    @Test
    @Transactional
    void patchNonExistingAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergy.setId(longCount.incrementAndGet());

        // Create the Allergy
        AllergyDTO allergyDTO = allergyMapper.toDto(allergy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, allergyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allergyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergy.setId(longCount.incrementAndGet());

        // Create the Allergy
        AllergyDTO allergyDTO = allergyMapper.toDto(allergy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allergyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAllergy() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergy.setId(longCount.incrementAndGet());

        // Create the Allergy
        AllergyDTO allergyDTO = allergyMapper.toDto(allergy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(allergyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Allergy in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAllergy() throws Exception {
        // Initialize the database
        insertedAllergy = allergyRepository.saveAndFlush(allergy);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the allergy
        restAllergyMockMvc
            .perform(delete(ENTITY_API_URL_ID, allergy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return allergyRepository.count();
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

    protected Allergy getPersistedAllergy(Allergy allergy) {
        return allergyRepository.findById(allergy.getId()).orElseThrow();
    }

    protected void assertPersistedAllergyToMatchAllProperties(Allergy expectedAllergy) {
        assertAllergyAllPropertiesEquals(expectedAllergy, getPersistedAllergy(expectedAllergy));
    }

    protected void assertPersistedAllergyToMatchUpdatableProperties(Allergy expectedAllergy) {
        assertAllergyAllUpdatablePropertiesEquals(expectedAllergy, getPersistedAllergy(expectedAllergy));
    }
}
