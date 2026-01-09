package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.PregnancyTetanusAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.PregnancyTetanus;
import com.mycompany.microservice.domain.enumeration.TetanusDose;
import com.mycompany.microservice.repository.PregnancyTetanusRepository;
import com.mycompany.microservice.service.dto.PregnancyTetanusDTO;
import com.mycompany.microservice.service.mapper.PregnancyTetanusMapper;
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
 * Integration tests for the {@link PregnancyTetanusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PregnancyTetanusResourceIT {

    private static final TetanusDose DEFAULT_DOSE = TetanusDose.UV1;
    private static final TetanusDose UPDATED_DOSE = TetanusDose.UV2;

    private static final Boolean DEFAULT_NOT_INJECTED = false;
    private static final Boolean UPDATED_NOT_INJECTED = true;

    private static final LocalDate DEFAULT_INJECTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INJECTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_PREGNANCY_MONTH = 1;
    private static final Integer UPDATED_PREGNANCY_MONTH = 2;

    private static final String DEFAULT_REACTION = "AAAAAAAAAA";
    private static final String UPDATED_REACTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NEXT_APPOINTMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_APPOINTMENT = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NUMBER_OF_DOSES_RECEIVED = 1;
    private static final Integer UPDATED_NUMBER_OF_DOSES_RECEIVED = 2;

    private static final String ENTITY_API_URL = "/api/pregnancy-tetanuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PregnancyTetanusRepository pregnancyTetanusRepository;

    @Autowired
    private PregnancyTetanusMapper pregnancyTetanusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPregnancyTetanusMockMvc;

    private PregnancyTetanus pregnancyTetanus;

    private PregnancyTetanus insertedPregnancyTetanus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PregnancyTetanus createEntity() {
        return new PregnancyTetanus()
            .dose(DEFAULT_DOSE)
            .notInjected(DEFAULT_NOT_INJECTED)
            .injectionDate(DEFAULT_INJECTION_DATE)
            .pregnancyMonth(DEFAULT_PREGNANCY_MONTH)
            .reaction(DEFAULT_REACTION)
            .nextAppointment(DEFAULT_NEXT_APPOINTMENT)
            .numberOfDosesReceived(DEFAULT_NUMBER_OF_DOSES_RECEIVED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PregnancyTetanus createUpdatedEntity() {
        return new PregnancyTetanus()
            .dose(UPDATED_DOSE)
            .notInjected(UPDATED_NOT_INJECTED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .pregnancyMonth(UPDATED_PREGNANCY_MONTH)
            .reaction(UPDATED_REACTION)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT)
            .numberOfDosesReceived(UPDATED_NUMBER_OF_DOSES_RECEIVED);
    }

    @BeforeEach
    void initTest() {
        pregnancyTetanus = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPregnancyTetanus != null) {
            pregnancyTetanusRepository.delete(insertedPregnancyTetanus);
            insertedPregnancyTetanus = null;
        }
    }

    @Test
    @Transactional
    void createPregnancyTetanus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PregnancyTetanus
        PregnancyTetanusDTO pregnancyTetanusDTO = pregnancyTetanusMapper.toDto(pregnancyTetanus);
        var returnedPregnancyTetanusDTO = om.readValue(
            restPregnancyTetanusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pregnancyTetanusDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PregnancyTetanusDTO.class
        );

        // Validate the PregnancyTetanus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPregnancyTetanus = pregnancyTetanusMapper.toEntity(returnedPregnancyTetanusDTO);
        assertPregnancyTetanusUpdatableFieldsEquals(returnedPregnancyTetanus, getPersistedPregnancyTetanus(returnedPregnancyTetanus));

        insertedPregnancyTetanus = returnedPregnancyTetanus;
    }

    @Test
    @Transactional
    void createPregnancyTetanusWithExistingId() throws Exception {
        // Create the PregnancyTetanus with an existing ID
        pregnancyTetanus.setId(1L);
        PregnancyTetanusDTO pregnancyTetanusDTO = pregnancyTetanusMapper.toDto(pregnancyTetanus);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPregnancyTetanusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pregnancyTetanusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PregnancyTetanus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDoseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pregnancyTetanus.setDose(null);

        // Create the PregnancyTetanus, which fails.
        PregnancyTetanusDTO pregnancyTetanusDTO = pregnancyTetanusMapper.toDto(pregnancyTetanus);

        restPregnancyTetanusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pregnancyTetanusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPregnancyTetanuses() throws Exception {
        // Initialize the database
        insertedPregnancyTetanus = pregnancyTetanusRepository.saveAndFlush(pregnancyTetanus);

        // Get all the pregnancyTetanusList
        restPregnancyTetanusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pregnancyTetanus.getId().intValue())))
            .andExpect(jsonPath("$.[*].dose").value(hasItem(DEFAULT_DOSE.toString())))
            .andExpect(jsonPath("$.[*].notInjected").value(hasItem(DEFAULT_NOT_INJECTED)))
            .andExpect(jsonPath("$.[*].injectionDate").value(hasItem(DEFAULT_INJECTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].pregnancyMonth").value(hasItem(DEFAULT_PREGNANCY_MONTH)))
            .andExpect(jsonPath("$.[*].reaction").value(hasItem(DEFAULT_REACTION)))
            .andExpect(jsonPath("$.[*].nextAppointment").value(hasItem(DEFAULT_NEXT_APPOINTMENT.toString())))
            .andExpect(jsonPath("$.[*].numberOfDosesReceived").value(hasItem(DEFAULT_NUMBER_OF_DOSES_RECEIVED)));
    }

    @Test
    @Transactional
    void getPregnancyTetanus() throws Exception {
        // Initialize the database
        insertedPregnancyTetanus = pregnancyTetanusRepository.saveAndFlush(pregnancyTetanus);

        // Get the pregnancyTetanus
        restPregnancyTetanusMockMvc
            .perform(get(ENTITY_API_URL_ID, pregnancyTetanus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pregnancyTetanus.getId().intValue()))
            .andExpect(jsonPath("$.dose").value(DEFAULT_DOSE.toString()))
            .andExpect(jsonPath("$.notInjected").value(DEFAULT_NOT_INJECTED))
            .andExpect(jsonPath("$.injectionDate").value(DEFAULT_INJECTION_DATE.toString()))
            .andExpect(jsonPath("$.pregnancyMonth").value(DEFAULT_PREGNANCY_MONTH))
            .andExpect(jsonPath("$.reaction").value(DEFAULT_REACTION))
            .andExpect(jsonPath("$.nextAppointment").value(DEFAULT_NEXT_APPOINTMENT.toString()))
            .andExpect(jsonPath("$.numberOfDosesReceived").value(DEFAULT_NUMBER_OF_DOSES_RECEIVED));
    }

    @Test
    @Transactional
    void getNonExistingPregnancyTetanus() throws Exception {
        // Get the pregnancyTetanus
        restPregnancyTetanusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPregnancyTetanus() throws Exception {
        // Initialize the database
        insertedPregnancyTetanus = pregnancyTetanusRepository.saveAndFlush(pregnancyTetanus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pregnancyTetanus
        PregnancyTetanus updatedPregnancyTetanus = pregnancyTetanusRepository.findById(pregnancyTetanus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPregnancyTetanus are not directly saved in db
        em.detach(updatedPregnancyTetanus);
        updatedPregnancyTetanus
            .dose(UPDATED_DOSE)
            .notInjected(UPDATED_NOT_INJECTED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .pregnancyMonth(UPDATED_PREGNANCY_MONTH)
            .reaction(UPDATED_REACTION)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT)
            .numberOfDosesReceived(UPDATED_NUMBER_OF_DOSES_RECEIVED);
        PregnancyTetanusDTO pregnancyTetanusDTO = pregnancyTetanusMapper.toDto(updatedPregnancyTetanus);

        restPregnancyTetanusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pregnancyTetanusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pregnancyTetanusDTO))
            )
            .andExpect(status().isOk());

        // Validate the PregnancyTetanus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPregnancyTetanusToMatchAllProperties(updatedPregnancyTetanus);
    }

    @Test
    @Transactional
    void putNonExistingPregnancyTetanus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pregnancyTetanus.setId(longCount.incrementAndGet());

        // Create the PregnancyTetanus
        PregnancyTetanusDTO pregnancyTetanusDTO = pregnancyTetanusMapper.toDto(pregnancyTetanus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPregnancyTetanusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pregnancyTetanusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pregnancyTetanusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PregnancyTetanus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPregnancyTetanus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pregnancyTetanus.setId(longCount.incrementAndGet());

        // Create the PregnancyTetanus
        PregnancyTetanusDTO pregnancyTetanusDTO = pregnancyTetanusMapper.toDto(pregnancyTetanus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPregnancyTetanusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pregnancyTetanusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PregnancyTetanus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPregnancyTetanus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pregnancyTetanus.setId(longCount.incrementAndGet());

        // Create the PregnancyTetanus
        PregnancyTetanusDTO pregnancyTetanusDTO = pregnancyTetanusMapper.toDto(pregnancyTetanus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPregnancyTetanusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pregnancyTetanusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PregnancyTetanus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePregnancyTetanusWithPatch() throws Exception {
        // Initialize the database
        insertedPregnancyTetanus = pregnancyTetanusRepository.saveAndFlush(pregnancyTetanus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pregnancyTetanus using partial update
        PregnancyTetanus partialUpdatedPregnancyTetanus = new PregnancyTetanus();
        partialUpdatedPregnancyTetanus.setId(pregnancyTetanus.getId());

        partialUpdatedPregnancyTetanus
            .dose(UPDATED_DOSE)
            .notInjected(UPDATED_NOT_INJECTED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .reaction(UPDATED_REACTION)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT);

        restPregnancyTetanusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPregnancyTetanus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPregnancyTetanus))
            )
            .andExpect(status().isOk());

        // Validate the PregnancyTetanus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPregnancyTetanusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPregnancyTetanus, pregnancyTetanus),
            getPersistedPregnancyTetanus(pregnancyTetanus)
        );
    }

    @Test
    @Transactional
    void fullUpdatePregnancyTetanusWithPatch() throws Exception {
        // Initialize the database
        insertedPregnancyTetanus = pregnancyTetanusRepository.saveAndFlush(pregnancyTetanus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pregnancyTetanus using partial update
        PregnancyTetanus partialUpdatedPregnancyTetanus = new PregnancyTetanus();
        partialUpdatedPregnancyTetanus.setId(pregnancyTetanus.getId());

        partialUpdatedPregnancyTetanus
            .dose(UPDATED_DOSE)
            .notInjected(UPDATED_NOT_INJECTED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .pregnancyMonth(UPDATED_PREGNANCY_MONTH)
            .reaction(UPDATED_REACTION)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT)
            .numberOfDosesReceived(UPDATED_NUMBER_OF_DOSES_RECEIVED);

        restPregnancyTetanusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPregnancyTetanus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPregnancyTetanus))
            )
            .andExpect(status().isOk());

        // Validate the PregnancyTetanus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPregnancyTetanusUpdatableFieldsEquals(
            partialUpdatedPregnancyTetanus,
            getPersistedPregnancyTetanus(partialUpdatedPregnancyTetanus)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPregnancyTetanus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pregnancyTetanus.setId(longCount.incrementAndGet());

        // Create the PregnancyTetanus
        PregnancyTetanusDTO pregnancyTetanusDTO = pregnancyTetanusMapper.toDto(pregnancyTetanus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPregnancyTetanusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pregnancyTetanusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pregnancyTetanusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PregnancyTetanus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPregnancyTetanus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pregnancyTetanus.setId(longCount.incrementAndGet());

        // Create the PregnancyTetanus
        PregnancyTetanusDTO pregnancyTetanusDTO = pregnancyTetanusMapper.toDto(pregnancyTetanus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPregnancyTetanusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pregnancyTetanusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PregnancyTetanus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPregnancyTetanus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pregnancyTetanus.setId(longCount.incrementAndGet());

        // Create the PregnancyTetanus
        PregnancyTetanusDTO pregnancyTetanusDTO = pregnancyTetanusMapper.toDto(pregnancyTetanus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPregnancyTetanusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pregnancyTetanusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PregnancyTetanus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePregnancyTetanus() throws Exception {
        // Initialize the database
        insertedPregnancyTetanus = pregnancyTetanusRepository.saveAndFlush(pregnancyTetanus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pregnancyTetanus
        restPregnancyTetanusMockMvc
            .perform(delete(ENTITY_API_URL_ID, pregnancyTetanus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pregnancyTetanusRepository.count();
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

    protected PregnancyTetanus getPersistedPregnancyTetanus(PregnancyTetanus pregnancyTetanus) {
        return pregnancyTetanusRepository.findById(pregnancyTetanus.getId()).orElseThrow();
    }

    protected void assertPersistedPregnancyTetanusToMatchAllProperties(PregnancyTetanus expectedPregnancyTetanus) {
        assertPregnancyTetanusAllPropertiesEquals(expectedPregnancyTetanus, getPersistedPregnancyTetanus(expectedPregnancyTetanus));
    }

    protected void assertPersistedPregnancyTetanusToMatchUpdatableProperties(PregnancyTetanus expectedPregnancyTetanus) {
        assertPregnancyTetanusAllUpdatablePropertiesEquals(
            expectedPregnancyTetanus,
            getPersistedPregnancyTetanus(expectedPregnancyTetanus)
        );
    }
}
