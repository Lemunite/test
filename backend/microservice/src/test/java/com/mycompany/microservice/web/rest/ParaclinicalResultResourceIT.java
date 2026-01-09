package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.ParaclinicalResultAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.MedicalRecord;
import com.mycompany.microservice.domain.ParaclinicalResult;
import com.mycompany.microservice.repository.ParaclinicalResultRepository;
import com.mycompany.microservice.service.dto.ParaclinicalResultDTO;
import com.mycompany.microservice.service.mapper.ParaclinicalResultMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ParaclinicalResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParaclinicalResultResourceIT {

    private static final String DEFAULT_TEST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RESULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESULT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/paraclinical-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ParaclinicalResultRepository paraclinicalResultRepository;

    @Autowired
    private ParaclinicalResultMapper paraclinicalResultMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParaclinicalResultMockMvc;

    private ParaclinicalResult paraclinicalResult;

    private ParaclinicalResult insertedParaclinicalResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParaclinicalResult createEntity(EntityManager em) {
        ParaclinicalResult paraclinicalResult = new ParaclinicalResult()
            .testName(DEFAULT_TEST_NAME)
            .result(DEFAULT_RESULT)
            .resultDate(DEFAULT_RESULT_DATE);
        // Add required entity
        MedicalRecord medicalRecord;
        if (TestUtil.findAll(em, MedicalRecord.class).isEmpty()) {
            medicalRecord = MedicalRecordResourceIT.createEntity(em);
            em.persist(medicalRecord);
            em.flush();
        } else {
            medicalRecord = TestUtil.findAll(em, MedicalRecord.class).get(0);
        }
        paraclinicalResult.setMedicalRecord(medicalRecord);
        return paraclinicalResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParaclinicalResult createUpdatedEntity(EntityManager em) {
        ParaclinicalResult updatedParaclinicalResult = new ParaclinicalResult()
            .testName(UPDATED_TEST_NAME)
            .result(UPDATED_RESULT)
            .resultDate(UPDATED_RESULT_DATE);
        // Add required entity
        MedicalRecord medicalRecord;
        if (TestUtil.findAll(em, MedicalRecord.class).isEmpty()) {
            medicalRecord = MedicalRecordResourceIT.createUpdatedEntity(em);
            em.persist(medicalRecord);
            em.flush();
        } else {
            medicalRecord = TestUtil.findAll(em, MedicalRecord.class).get(0);
        }
        updatedParaclinicalResult.setMedicalRecord(medicalRecord);
        return updatedParaclinicalResult;
    }

    @BeforeEach
    void initTest() {
        paraclinicalResult = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedParaclinicalResult != null) {
            paraclinicalResultRepository.delete(insertedParaclinicalResult);
            insertedParaclinicalResult = null;
        }
    }

    @Test
    @Transactional
    void createParaclinicalResult() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ParaclinicalResult
        ParaclinicalResultDTO paraclinicalResultDTO = paraclinicalResultMapper.toDto(paraclinicalResult);
        var returnedParaclinicalResultDTO = om.readValue(
            restParaclinicalResultMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paraclinicalResultDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ParaclinicalResultDTO.class
        );

        // Validate the ParaclinicalResult in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedParaclinicalResult = paraclinicalResultMapper.toEntity(returnedParaclinicalResultDTO);
        assertParaclinicalResultUpdatableFieldsEquals(
            returnedParaclinicalResult,
            getPersistedParaclinicalResult(returnedParaclinicalResult)
        );

        insertedParaclinicalResult = returnedParaclinicalResult;
    }

    @Test
    @Transactional
    void createParaclinicalResultWithExistingId() throws Exception {
        // Create the ParaclinicalResult with an existing ID
        paraclinicalResult.setId(1L);
        ParaclinicalResultDTO paraclinicalResultDTO = paraclinicalResultMapper.toDto(paraclinicalResult);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParaclinicalResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paraclinicalResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParaclinicalResult in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTestNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paraclinicalResult.setTestName(null);

        // Create the ParaclinicalResult, which fails.
        ParaclinicalResultDTO paraclinicalResultDTO = paraclinicalResultMapper.toDto(paraclinicalResult);

        restParaclinicalResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paraclinicalResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParaclinicalResults() throws Exception {
        // Initialize the database
        insertedParaclinicalResult = paraclinicalResultRepository.saveAndFlush(paraclinicalResult);

        // Get all the paraclinicalResultList
        restParaclinicalResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paraclinicalResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].testName").value(hasItem(DEFAULT_TEST_NAME)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].resultDate").value(hasItem(DEFAULT_RESULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getParaclinicalResult() throws Exception {
        // Initialize the database
        insertedParaclinicalResult = paraclinicalResultRepository.saveAndFlush(paraclinicalResult);

        // Get the paraclinicalResult
        restParaclinicalResultMockMvc
            .perform(get(ENTITY_API_URL_ID, paraclinicalResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paraclinicalResult.getId().intValue()))
            .andExpect(jsonPath("$.testName").value(DEFAULT_TEST_NAME))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT))
            .andExpect(jsonPath("$.resultDate").value(DEFAULT_RESULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingParaclinicalResult() throws Exception {
        // Get the paraclinicalResult
        restParaclinicalResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParaclinicalResult() throws Exception {
        // Initialize the database
        insertedParaclinicalResult = paraclinicalResultRepository.saveAndFlush(paraclinicalResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paraclinicalResult
        ParaclinicalResult updatedParaclinicalResult = paraclinicalResultRepository.findById(paraclinicalResult.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParaclinicalResult are not directly saved in db
        em.detach(updatedParaclinicalResult);
        updatedParaclinicalResult.testName(UPDATED_TEST_NAME).result(UPDATED_RESULT).resultDate(UPDATED_RESULT_DATE);
        ParaclinicalResultDTO paraclinicalResultDTO = paraclinicalResultMapper.toDto(updatedParaclinicalResult);

        restParaclinicalResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paraclinicalResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paraclinicalResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the ParaclinicalResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedParaclinicalResultToMatchAllProperties(updatedParaclinicalResult);
    }

    @Test
    @Transactional
    void putNonExistingParaclinicalResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paraclinicalResult.setId(longCount.incrementAndGet());

        // Create the ParaclinicalResult
        ParaclinicalResultDTO paraclinicalResultDTO = paraclinicalResultMapper.toDto(paraclinicalResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParaclinicalResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paraclinicalResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paraclinicalResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParaclinicalResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParaclinicalResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paraclinicalResult.setId(longCount.incrementAndGet());

        // Create the ParaclinicalResult
        ParaclinicalResultDTO paraclinicalResultDTO = paraclinicalResultMapper.toDto(paraclinicalResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParaclinicalResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paraclinicalResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParaclinicalResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParaclinicalResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paraclinicalResult.setId(longCount.incrementAndGet());

        // Create the ParaclinicalResult
        ParaclinicalResultDTO paraclinicalResultDTO = paraclinicalResultMapper.toDto(paraclinicalResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParaclinicalResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paraclinicalResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParaclinicalResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParaclinicalResultWithPatch() throws Exception {
        // Initialize the database
        insertedParaclinicalResult = paraclinicalResultRepository.saveAndFlush(paraclinicalResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paraclinicalResult using partial update
        ParaclinicalResult partialUpdatedParaclinicalResult = new ParaclinicalResult();
        partialUpdatedParaclinicalResult.setId(paraclinicalResult.getId());

        partialUpdatedParaclinicalResult.testName(UPDATED_TEST_NAME).result(UPDATED_RESULT).resultDate(UPDATED_RESULT_DATE);

        restParaclinicalResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParaclinicalResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParaclinicalResult))
            )
            .andExpect(status().isOk());

        // Validate the ParaclinicalResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParaclinicalResultUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedParaclinicalResult, paraclinicalResult),
            getPersistedParaclinicalResult(paraclinicalResult)
        );
    }

    @Test
    @Transactional
    void fullUpdateParaclinicalResultWithPatch() throws Exception {
        // Initialize the database
        insertedParaclinicalResult = paraclinicalResultRepository.saveAndFlush(paraclinicalResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paraclinicalResult using partial update
        ParaclinicalResult partialUpdatedParaclinicalResult = new ParaclinicalResult();
        partialUpdatedParaclinicalResult.setId(paraclinicalResult.getId());

        partialUpdatedParaclinicalResult.testName(UPDATED_TEST_NAME).result(UPDATED_RESULT).resultDate(UPDATED_RESULT_DATE);

        restParaclinicalResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParaclinicalResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParaclinicalResult))
            )
            .andExpect(status().isOk());

        // Validate the ParaclinicalResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParaclinicalResultUpdatableFieldsEquals(
            partialUpdatedParaclinicalResult,
            getPersistedParaclinicalResult(partialUpdatedParaclinicalResult)
        );
    }

    @Test
    @Transactional
    void patchNonExistingParaclinicalResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paraclinicalResult.setId(longCount.incrementAndGet());

        // Create the ParaclinicalResult
        ParaclinicalResultDTO paraclinicalResultDTO = paraclinicalResultMapper.toDto(paraclinicalResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParaclinicalResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paraclinicalResultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paraclinicalResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParaclinicalResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParaclinicalResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paraclinicalResult.setId(longCount.incrementAndGet());

        // Create the ParaclinicalResult
        ParaclinicalResultDTO paraclinicalResultDTO = paraclinicalResultMapper.toDto(paraclinicalResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParaclinicalResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paraclinicalResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParaclinicalResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParaclinicalResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paraclinicalResult.setId(longCount.incrementAndGet());

        // Create the ParaclinicalResult
        ParaclinicalResultDTO paraclinicalResultDTO = paraclinicalResultMapper.toDto(paraclinicalResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParaclinicalResultMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paraclinicalResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParaclinicalResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParaclinicalResult() throws Exception {
        // Initialize the database
        insertedParaclinicalResult = paraclinicalResultRepository.saveAndFlush(paraclinicalResult);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paraclinicalResult
        restParaclinicalResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, paraclinicalResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paraclinicalResultRepository.count();
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

    protected ParaclinicalResult getPersistedParaclinicalResult(ParaclinicalResult paraclinicalResult) {
        return paraclinicalResultRepository.findById(paraclinicalResult.getId()).orElseThrow();
    }

    protected void assertPersistedParaclinicalResultToMatchAllProperties(ParaclinicalResult expectedParaclinicalResult) {
        assertParaclinicalResultAllPropertiesEquals(expectedParaclinicalResult, getPersistedParaclinicalResult(expectedParaclinicalResult));
    }

    protected void assertPersistedParaclinicalResultToMatchUpdatableProperties(ParaclinicalResult expectedParaclinicalResult) {
        assertParaclinicalResultAllUpdatablePropertiesEquals(
            expectedParaclinicalResult,
            getPersistedParaclinicalResult(expectedParaclinicalResult)
        );
    }
}
