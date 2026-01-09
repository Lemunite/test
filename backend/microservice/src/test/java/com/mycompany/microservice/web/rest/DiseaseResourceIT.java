package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.DiseaseAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.Disease;
import com.mycompany.microservice.domain.FamilyDisease;
import com.mycompany.microservice.domain.enumeration.DiseaseName;
import com.mycompany.microservice.repository.DiseaseRepository;
import com.mycompany.microservice.service.dto.DiseaseDTO;
import com.mycompany.microservice.service.mapper.DiseaseMapper;
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
 * Integration tests for the {@link DiseaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiseaseResourceIT {

    private static final DiseaseName DEFAULT_NAME = DiseaseName.HEART;
    private static final DiseaseName UPDATED_NAME = DiseaseName.BLOOD_PRESSURE;

    private static final String DEFAULT_SPECIFIC_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SPECIFIC_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/diseases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiseaseMockMvc;

    private Disease disease;

    private Disease insertedDisease;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disease createEntity(EntityManager em) {
        Disease disease = new Disease().name(DEFAULT_NAME).specificType(DEFAULT_SPECIFIC_TYPE).description(DEFAULT_DESCRIPTION);
        // Add required entity
        FamilyDisease familyDisease;
        if (TestUtil.findAll(em, FamilyDisease.class).isEmpty()) {
            familyDisease = FamilyDiseaseResourceIT.createEntity(em);
            em.persist(familyDisease);
            em.flush();
        } else {
            familyDisease = TestUtil.findAll(em, FamilyDisease.class).get(0);
        }
        disease.setFamilyDisease(familyDisease);
        return disease;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disease createUpdatedEntity(EntityManager em) {
        Disease updatedDisease = new Disease().name(UPDATED_NAME).specificType(UPDATED_SPECIFIC_TYPE).description(UPDATED_DESCRIPTION);
        // Add required entity
        FamilyDisease familyDisease;
        if (TestUtil.findAll(em, FamilyDisease.class).isEmpty()) {
            familyDisease = FamilyDiseaseResourceIT.createUpdatedEntity(em);
            em.persist(familyDisease);
            em.flush();
        } else {
            familyDisease = TestUtil.findAll(em, FamilyDisease.class).get(0);
        }
        updatedDisease.setFamilyDisease(familyDisease);
        return updatedDisease;
    }

    @BeforeEach
    void initTest() {
        disease = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDisease != null) {
            diseaseRepository.delete(insertedDisease);
            insertedDisease = null;
        }
    }

    @Test
    @Transactional
    void createDisease() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Disease
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);
        var returnedDiseaseDTO = om.readValue(
            restDiseaseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diseaseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DiseaseDTO.class
        );

        // Validate the Disease in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDisease = diseaseMapper.toEntity(returnedDiseaseDTO);
        assertDiseaseUpdatableFieldsEquals(returnedDisease, getPersistedDisease(returnedDisease));

        insertedDisease = returnedDisease;
    }

    @Test
    @Transactional
    void createDiseaseWithExistingId() throws Exception {
        // Create the Disease with an existing ID
        disease.setId(1L);
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiseaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diseaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Disease in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disease.setName(null);

        // Create the Disease, which fails.
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        restDiseaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diseaseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDiseases() throws Exception {
        // Initialize the database
        insertedDisease = diseaseRepository.saveAndFlush(disease);

        // Get all the diseaseList
        restDiseaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disease.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].specificType").value(hasItem(DEFAULT_SPECIFIC_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDisease() throws Exception {
        // Initialize the database
        insertedDisease = diseaseRepository.saveAndFlush(disease);

        // Get the disease
        restDiseaseMockMvc
            .perform(get(ENTITY_API_URL_ID, disease.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disease.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.specificType").value(DEFAULT_SPECIFIC_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDisease() throws Exception {
        // Get the disease
        restDiseaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisease() throws Exception {
        // Initialize the database
        insertedDisease = diseaseRepository.saveAndFlush(disease);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disease
        Disease updatedDisease = diseaseRepository.findById(disease.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDisease are not directly saved in db
        em.detach(updatedDisease);
        updatedDisease.name(UPDATED_NAME).specificType(UPDATED_SPECIFIC_TYPE).description(UPDATED_DESCRIPTION);
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(updatedDisease);

        restDiseaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diseaseDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diseaseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Disease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDiseaseToMatchAllProperties(updatedDisease);
    }

    @Test
    @Transactional
    void putNonExistingDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disease.setId(longCount.incrementAndGet());

        // Create the Disease
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiseaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diseaseDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diseaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disease.setId(longCount.incrementAndGet());

        // Create the Disease
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiseaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diseaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disease.setId(longCount.incrementAndGet());

        // Create the Disease
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiseaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diseaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiseaseWithPatch() throws Exception {
        // Initialize the database
        insertedDisease = diseaseRepository.saveAndFlush(disease);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disease using partial update
        Disease partialUpdatedDisease = new Disease();
        partialUpdatedDisease.setId(disease.getId());

        restDiseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisease.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisease))
            )
            .andExpect(status().isOk());

        // Validate the Disease in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiseaseUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDisease, disease), getPersistedDisease(disease));
    }

    @Test
    @Transactional
    void fullUpdateDiseaseWithPatch() throws Exception {
        // Initialize the database
        insertedDisease = diseaseRepository.saveAndFlush(disease);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disease using partial update
        Disease partialUpdatedDisease = new Disease();
        partialUpdatedDisease.setId(disease.getId());

        partialUpdatedDisease.name(UPDATED_NAME).specificType(UPDATED_SPECIFIC_TYPE).description(UPDATED_DESCRIPTION);

        restDiseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisease.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisease))
            )
            .andExpect(status().isOk());

        // Validate the Disease in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiseaseUpdatableFieldsEquals(partialUpdatedDisease, getPersistedDisease(partialUpdatedDisease));
    }

    @Test
    @Transactional
    void patchNonExistingDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disease.setId(longCount.incrementAndGet());

        // Create the Disease
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, diseaseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(diseaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disease.setId(longCount.incrementAndGet());

        // Create the Disease
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiseaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(diseaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisease() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disease.setId(longCount.incrementAndGet());

        // Create the Disease
        DiseaseDTO diseaseDTO = diseaseMapper.toDto(disease);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiseaseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(diseaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disease in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisease() throws Exception {
        // Initialize the database
        insertedDisease = diseaseRepository.saveAndFlush(disease);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the disease
        restDiseaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, disease.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return diseaseRepository.count();
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

    protected Disease getPersistedDisease(Disease disease) {
        return diseaseRepository.findById(disease.getId()).orElseThrow();
    }

    protected void assertPersistedDiseaseToMatchAllProperties(Disease expectedDisease) {
        assertDiseaseAllPropertiesEquals(expectedDisease, getPersistedDisease(expectedDisease));
    }

    protected void assertPersistedDiseaseToMatchUpdatableProperties(Disease expectedDisease) {
        assertDiseaseAllUpdatablePropertiesEquals(expectedDisease, getPersistedDisease(expectedDisease));
    }
}
