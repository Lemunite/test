package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.FamilyDiseaseAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.FamilyDisease;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.repository.FamilyDiseaseRepository;
import com.mycompany.microservice.service.dto.FamilyDiseaseDTO;
import com.mycompany.microservice.service.mapper.FamilyDiseaseMapper;
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
 * Integration tests for the {@link FamilyDiseaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamilyDiseaseResourceIT {

    private static final String DEFAULT_AFFECTED_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_AFFECTED_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIONSHIP_TO_PATIENT = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP_TO_PATIENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/family-diseases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FamilyDiseaseRepository familyDiseaseRepository;

    @Autowired
    private FamilyDiseaseMapper familyDiseaseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilyDiseaseMockMvc;

    private FamilyDisease familyDisease;

    private FamilyDisease insertedFamilyDisease;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyDisease createEntity(EntityManager em) {
        FamilyDisease familyDisease = new FamilyDisease()
            .affectedPerson(DEFAULT_AFFECTED_PERSON)
            .relationshipToPatient(DEFAULT_RELATIONSHIP_TO_PATIENT);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        familyDisease.setPatient(patient);
        return familyDisease;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyDisease createUpdatedEntity(EntityManager em) {
        FamilyDisease updatedFamilyDisease = new FamilyDisease()
            .affectedPerson(UPDATED_AFFECTED_PERSON)
            .relationshipToPatient(UPDATED_RELATIONSHIP_TO_PATIENT);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedFamilyDisease.setPatient(patient);
        return updatedFamilyDisease;
    }

    @BeforeEach
    void initTest() {
        familyDisease = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFamilyDisease != null) {
            familyDiseaseRepository.delete(insertedFamilyDisease);
            insertedFamilyDisease = null;
        }
    }

    @Test
    @Transactional
    void createFamilyDisease() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FamilyDisease
        FamilyDiseaseDTO familyDiseaseDTO = familyDiseaseMapper.toDto(familyDisease);
        var returnedFamilyDiseaseDTO = om.readValue(
            restFamilyDiseaseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyDiseaseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FamilyDiseaseDTO.class
        );

        // Validate the FamilyDisease in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFamilyDisease = familyDiseaseMapper.toEntity(returnedFamilyDiseaseDTO);
        assertFamilyDiseaseUpdatableFieldsEquals(returnedFamilyDisease, getPersistedFamilyDisease(returnedFamilyDisease));

        insertedFamilyDisease = returnedFamilyDisease;
    }

    @Test
    @Transactional
    void createFamilyDiseaseWithExistingId() throws Exception {
        // Create the FamilyDisease with an existing ID
        familyDisease.setId(1L);
        FamilyDiseaseDTO familyDiseaseDTO = familyDiseaseMapper.toDto(familyDisease);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyDiseaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyDiseaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FamilyDisease in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAffectedPersonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        familyDisease.setAffectedPerson(null);

        // Create the FamilyDisease, which fails.
        FamilyDiseaseDTO familyDiseaseDTO = familyDiseaseMapper.toDto(familyDisease);

        restFamilyDiseaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyDiseaseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFamilyDiseases() throws Exception {
        // Initialize the database
        insertedFamilyDisease = familyDiseaseRepository.saveAndFlush(familyDisease);

        // Get all the familyDiseaseList
        restFamilyDiseaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyDisease.getId().intValue())))
            .andExpect(jsonPath("$.[*].affectedPerson").value(hasItem(DEFAULT_AFFECTED_PERSON)))
            .andExpect(jsonPath("$.[*].relationshipToPatient").value(hasItem(DEFAULT_RELATIONSHIP_TO_PATIENT)));
    }

    @Test
    @Transactional
    void getFamilyDisease() throws Exception {
        // Initialize the database
        insertedFamilyDisease = familyDiseaseRepository.saveAndFlush(familyDisease);

        // Get the familyDisease
        restFamilyDiseaseMockMvc
            .perform(get(ENTITY_API_URL_ID, familyDisease.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(familyDisease.getId().intValue()))
            .andExpect(jsonPath("$.affectedPerson").value(DEFAULT_AFFECTED_PERSON))
            .andExpect(jsonPath("$.relationshipToPatient").value(DEFAULT_RELATIONSHIP_TO_PATIENT));
    }

    @Test
    @Transactional
    void getNonExistingFamilyDisease() throws Exception {
        // Get the familyDisease
        restFamilyDiseaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFamilyDisease() throws Exception {
        // Initialize the database
        insertedFamilyDisease = familyDiseaseRepository.saveAndFlush(familyDisease);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the familyDisease
        FamilyDisease updatedFamilyDisease = familyDiseaseRepository.findById(familyDisease.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFamilyDisease are not directly saved in db
        em.detach(updatedFamilyDisease);
        updatedFamilyDisease.affectedPerson(UPDATED_AFFECTED_PERSON).relationshipToPatient(UPDATED_RELATIONSHIP_TO_PATIENT);
        FamilyDiseaseDTO familyDiseaseDTO = familyDiseaseMapper.toDto(updatedFamilyDisease);

        restFamilyDiseaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyDiseaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(familyDiseaseDTO))
            )
            .andExpect(status().isOk());

        // Validate the FamilyDisease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFamilyDiseaseToMatchAllProperties(updatedFamilyDisease);
    }

    @Test
    @Transactional
    void putNonExistingFamilyDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyDisease.setId(longCount.incrementAndGet());

        // Create the FamilyDisease
        FamilyDiseaseDTO familyDiseaseDTO = familyDiseaseMapper.toDto(familyDisease);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyDiseaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyDiseaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(familyDiseaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyDisease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamilyDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyDisease.setId(longCount.incrementAndGet());

        // Create the FamilyDisease
        FamilyDiseaseDTO familyDiseaseDTO = familyDiseaseMapper.toDto(familyDisease);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyDiseaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(familyDiseaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyDisease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamilyDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyDisease.setId(longCount.incrementAndGet());

        // Create the FamilyDisease
        FamilyDiseaseDTO familyDiseaseDTO = familyDiseaseMapper.toDto(familyDisease);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyDiseaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyDiseaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyDisease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilyDiseaseWithPatch() throws Exception {
        // Initialize the database
        insertedFamilyDisease = familyDiseaseRepository.saveAndFlush(familyDisease);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the familyDisease using partial update
        FamilyDisease partialUpdatedFamilyDisease = new FamilyDisease();
        partialUpdatedFamilyDisease.setId(familyDisease.getId());

        partialUpdatedFamilyDisease.relationshipToPatient(UPDATED_RELATIONSHIP_TO_PATIENT);

        restFamilyDiseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyDisease.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFamilyDisease))
            )
            .andExpect(status().isOk());

        // Validate the FamilyDisease in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFamilyDiseaseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFamilyDisease, familyDisease),
            getPersistedFamilyDisease(familyDisease)
        );
    }

    @Test
    @Transactional
    void fullUpdateFamilyDiseaseWithPatch() throws Exception {
        // Initialize the database
        insertedFamilyDisease = familyDiseaseRepository.saveAndFlush(familyDisease);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the familyDisease using partial update
        FamilyDisease partialUpdatedFamilyDisease = new FamilyDisease();
        partialUpdatedFamilyDisease.setId(familyDisease.getId());

        partialUpdatedFamilyDisease.affectedPerson(UPDATED_AFFECTED_PERSON).relationshipToPatient(UPDATED_RELATIONSHIP_TO_PATIENT);

        restFamilyDiseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyDisease.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFamilyDisease))
            )
            .andExpect(status().isOk());

        // Validate the FamilyDisease in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFamilyDiseaseUpdatableFieldsEquals(partialUpdatedFamilyDisease, getPersistedFamilyDisease(partialUpdatedFamilyDisease));
    }

    @Test
    @Transactional
    void patchNonExistingFamilyDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyDisease.setId(longCount.incrementAndGet());

        // Create the FamilyDisease
        FamilyDiseaseDTO familyDiseaseDTO = familyDiseaseMapper.toDto(familyDisease);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyDiseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familyDiseaseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(familyDiseaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyDisease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamilyDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyDisease.setId(longCount.incrementAndGet());

        // Create the FamilyDisease
        FamilyDiseaseDTO familyDiseaseDTO = familyDiseaseMapper.toDto(familyDisease);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyDiseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(familyDiseaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyDisease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamilyDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyDisease.setId(longCount.incrementAndGet());

        // Create the FamilyDisease
        FamilyDiseaseDTO familyDiseaseDTO = familyDiseaseMapper.toDto(familyDisease);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyDiseaseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(familyDiseaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyDisease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamilyDisease() throws Exception {
        // Initialize the database
        insertedFamilyDisease = familyDiseaseRepository.saveAndFlush(familyDisease);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the familyDisease
        restFamilyDiseaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, familyDisease.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return familyDiseaseRepository.count();
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

    protected FamilyDisease getPersistedFamilyDisease(FamilyDisease familyDisease) {
        return familyDiseaseRepository.findById(familyDisease.getId()).orElseThrow();
    }

    protected void assertPersistedFamilyDiseaseToMatchAllProperties(FamilyDisease expectedFamilyDisease) {
        assertFamilyDiseaseAllPropertiesEquals(expectedFamilyDisease, getPersistedFamilyDisease(expectedFamilyDisease));
    }

    protected void assertPersistedFamilyDiseaseToMatchUpdatableProperties(FamilyDisease expectedFamilyDisease) {
        assertFamilyDiseaseAllUpdatablePropertiesEquals(expectedFamilyDisease, getPersistedFamilyDisease(expectedFamilyDisease));
    }
}
