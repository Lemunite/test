package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.SurgeryHistoryAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.domain.SurgeryHistory;
import com.mycompany.microservice.repository.SurgeryHistoryRepository;
import com.mycompany.microservice.service.dto.SurgeryHistoryDTO;
import com.mycompany.microservice.service.mapper.SurgeryHistoryMapper;
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
 * Integration tests for the {@link SurgeryHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SurgeryHistoryResourceIT {

    private static final String DEFAULT_BODY_PART = "AAAAAAAAAA";
    private static final String UPDATED_BODY_PART = "BBBBBBBBBB";

    private static final Integer DEFAULT_SURGERY_YEAR = 1;
    private static final Integer UPDATED_SURGERY_YEAR = 2;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/surgery-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SurgeryHistoryRepository surgeryHistoryRepository;

    @Autowired
    private SurgeryHistoryMapper surgeryHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSurgeryHistoryMockMvc;

    private SurgeryHistory surgeryHistory;

    private SurgeryHistory insertedSurgeryHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SurgeryHistory createEntity(EntityManager em) {
        SurgeryHistory surgeryHistory = new SurgeryHistory()
            .bodyPart(DEFAULT_BODY_PART)
            .surgeryYear(DEFAULT_SURGERY_YEAR)
            .note(DEFAULT_NOTE);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        surgeryHistory.setPatient(patient);
        return surgeryHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SurgeryHistory createUpdatedEntity(EntityManager em) {
        SurgeryHistory updatedSurgeryHistory = new SurgeryHistory()
            .bodyPart(UPDATED_BODY_PART)
            .surgeryYear(UPDATED_SURGERY_YEAR)
            .note(UPDATED_NOTE);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedSurgeryHistory.setPatient(patient);
        return updatedSurgeryHistory;
    }

    @BeforeEach
    void initTest() {
        surgeryHistory = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSurgeryHistory != null) {
            surgeryHistoryRepository.delete(insertedSurgeryHistory);
            insertedSurgeryHistory = null;
        }
    }

    @Test
    @Transactional
    void createSurgeryHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SurgeryHistory
        SurgeryHistoryDTO surgeryHistoryDTO = surgeryHistoryMapper.toDto(surgeryHistory);
        var returnedSurgeryHistoryDTO = om.readValue(
            restSurgeryHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surgeryHistoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SurgeryHistoryDTO.class
        );

        // Validate the SurgeryHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSurgeryHistory = surgeryHistoryMapper.toEntity(returnedSurgeryHistoryDTO);
        assertSurgeryHistoryUpdatableFieldsEquals(returnedSurgeryHistory, getPersistedSurgeryHistory(returnedSurgeryHistory));

        insertedSurgeryHistory = returnedSurgeryHistory;
    }

    @Test
    @Transactional
    void createSurgeryHistoryWithExistingId() throws Exception {
        // Create the SurgeryHistory with an existing ID
        surgeryHistory.setId(1L);
        SurgeryHistoryDTO surgeryHistoryDTO = surgeryHistoryMapper.toDto(surgeryHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurgeryHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surgeryHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SurgeryHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSurgeryHistories() throws Exception {
        // Initialize the database
        insertedSurgeryHistory = surgeryHistoryRepository.saveAndFlush(surgeryHistory);

        // Get all the surgeryHistoryList
        restSurgeryHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surgeryHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].bodyPart").value(hasItem(DEFAULT_BODY_PART)))
            .andExpect(jsonPath("$.[*].surgeryYear").value(hasItem(DEFAULT_SURGERY_YEAR)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getSurgeryHistory() throws Exception {
        // Initialize the database
        insertedSurgeryHistory = surgeryHistoryRepository.saveAndFlush(surgeryHistory);

        // Get the surgeryHistory
        restSurgeryHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, surgeryHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(surgeryHistory.getId().intValue()))
            .andExpect(jsonPath("$.bodyPart").value(DEFAULT_BODY_PART))
            .andExpect(jsonPath("$.surgeryYear").value(DEFAULT_SURGERY_YEAR))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingSurgeryHistory() throws Exception {
        // Get the surgeryHistory
        restSurgeryHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSurgeryHistory() throws Exception {
        // Initialize the database
        insertedSurgeryHistory = surgeryHistoryRepository.saveAndFlush(surgeryHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the surgeryHistory
        SurgeryHistory updatedSurgeryHistory = surgeryHistoryRepository.findById(surgeryHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSurgeryHistory are not directly saved in db
        em.detach(updatedSurgeryHistory);
        updatedSurgeryHistory.bodyPart(UPDATED_BODY_PART).surgeryYear(UPDATED_SURGERY_YEAR).note(UPDATED_NOTE);
        SurgeryHistoryDTO surgeryHistoryDTO = surgeryHistoryMapper.toDto(updatedSurgeryHistory);

        restSurgeryHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surgeryHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(surgeryHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the SurgeryHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSurgeryHistoryToMatchAllProperties(updatedSurgeryHistory);
    }

    @Test
    @Transactional
    void putNonExistingSurgeryHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surgeryHistory.setId(longCount.incrementAndGet());

        // Create the SurgeryHistory
        SurgeryHistoryDTO surgeryHistoryDTO = surgeryHistoryMapper.toDto(surgeryHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurgeryHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surgeryHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(surgeryHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurgeryHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSurgeryHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surgeryHistory.setId(longCount.incrementAndGet());

        // Create the SurgeryHistory
        SurgeryHistoryDTO surgeryHistoryDTO = surgeryHistoryMapper.toDto(surgeryHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurgeryHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(surgeryHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurgeryHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSurgeryHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surgeryHistory.setId(longCount.incrementAndGet());

        // Create the SurgeryHistory
        SurgeryHistoryDTO surgeryHistoryDTO = surgeryHistoryMapper.toDto(surgeryHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurgeryHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surgeryHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SurgeryHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSurgeryHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedSurgeryHistory = surgeryHistoryRepository.saveAndFlush(surgeryHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the surgeryHistory using partial update
        SurgeryHistory partialUpdatedSurgeryHistory = new SurgeryHistory();
        partialUpdatedSurgeryHistory.setId(surgeryHistory.getId());

        partialUpdatedSurgeryHistory.bodyPart(UPDATED_BODY_PART).surgeryYear(UPDATED_SURGERY_YEAR).note(UPDATED_NOTE);

        restSurgeryHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurgeryHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSurgeryHistory))
            )
            .andExpect(status().isOk());

        // Validate the SurgeryHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSurgeryHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSurgeryHistory, surgeryHistory),
            getPersistedSurgeryHistory(surgeryHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateSurgeryHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedSurgeryHistory = surgeryHistoryRepository.saveAndFlush(surgeryHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the surgeryHistory using partial update
        SurgeryHistory partialUpdatedSurgeryHistory = new SurgeryHistory();
        partialUpdatedSurgeryHistory.setId(surgeryHistory.getId());

        partialUpdatedSurgeryHistory.bodyPart(UPDATED_BODY_PART).surgeryYear(UPDATED_SURGERY_YEAR).note(UPDATED_NOTE);

        restSurgeryHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurgeryHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSurgeryHistory))
            )
            .andExpect(status().isOk());

        // Validate the SurgeryHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSurgeryHistoryUpdatableFieldsEquals(partialUpdatedSurgeryHistory, getPersistedSurgeryHistory(partialUpdatedSurgeryHistory));
    }

    @Test
    @Transactional
    void patchNonExistingSurgeryHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surgeryHistory.setId(longCount.incrementAndGet());

        // Create the SurgeryHistory
        SurgeryHistoryDTO surgeryHistoryDTO = surgeryHistoryMapper.toDto(surgeryHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurgeryHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, surgeryHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(surgeryHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurgeryHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSurgeryHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surgeryHistory.setId(longCount.incrementAndGet());

        // Create the SurgeryHistory
        SurgeryHistoryDTO surgeryHistoryDTO = surgeryHistoryMapper.toDto(surgeryHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurgeryHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(surgeryHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SurgeryHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSurgeryHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surgeryHistory.setId(longCount.incrementAndGet());

        // Create the SurgeryHistory
        SurgeryHistoryDTO surgeryHistoryDTO = surgeryHistoryMapper.toDto(surgeryHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurgeryHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(surgeryHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SurgeryHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSurgeryHistory() throws Exception {
        // Initialize the database
        insertedSurgeryHistory = surgeryHistoryRepository.saveAndFlush(surgeryHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the surgeryHistory
        restSurgeryHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, surgeryHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return surgeryHistoryRepository.count();
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

    protected SurgeryHistory getPersistedSurgeryHistory(SurgeryHistory surgeryHistory) {
        return surgeryHistoryRepository.findById(surgeryHistory.getId()).orElseThrow();
    }

    protected void assertPersistedSurgeryHistoryToMatchAllProperties(SurgeryHistory expectedSurgeryHistory) {
        assertSurgeryHistoryAllPropertiesEquals(expectedSurgeryHistory, getPersistedSurgeryHistory(expectedSurgeryHistory));
    }

    protected void assertPersistedSurgeryHistoryToMatchUpdatableProperties(SurgeryHistory expectedSurgeryHistory) {
        assertSurgeryHistoryAllUpdatablePropertiesEquals(expectedSurgeryHistory, getPersistedSurgeryHistory(expectedSurgeryHistory));
    }
}
