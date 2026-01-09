package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.VaccinationForBabyAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.VaccinationForBaby;
import com.mycompany.microservice.domain.enumeration.VaccineType;
import com.mycompany.microservice.repository.VaccinationForBabyRepository;
import com.mycompany.microservice.service.dto.VaccinationForBabyDTO;
import com.mycompany.microservice.service.mapper.VaccinationForBabyMapper;
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
 * Integration tests for the {@link VaccinationForBabyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccinationForBabyResourceIT {

    private static final VaccineType DEFAULT_VACCINE = VaccineType.BCG;
    private static final VaccineType UPDATED_VACCINE = VaccineType.VGB_SO_SINH;

    private static final Integer DEFAULT_NUMBER_USE = 1;
    private static final Integer UPDATED_NUMBER_USE = 2;

    private static final String ENTITY_API_URL = "/api/vaccination-for-babies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VaccinationForBabyRepository vaccinationForBabyRepository;

    @Autowired
    private VaccinationForBabyMapper vaccinationForBabyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccinationForBabyMockMvc;

    private VaccinationForBaby vaccinationForBaby;

    private VaccinationForBaby insertedVaccinationForBaby;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccinationForBaby createEntity(EntityManager em) {
        VaccinationForBaby vaccinationForBaby = new VaccinationForBaby().vaccine(DEFAULT_VACCINE).numberUse(DEFAULT_NUMBER_USE);
        return vaccinationForBaby;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccinationForBaby createUpdatedEntity(EntityManager em) {
        VaccinationForBaby updatedVaccinationForBaby = new VaccinationForBaby().vaccine(UPDATED_VACCINE).numberUse(UPDATED_NUMBER_USE);
        return updatedVaccinationForBaby;
    }

    @BeforeEach
    void initTest() {
        vaccinationForBaby = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedVaccinationForBaby != null) {
            vaccinationForBabyRepository.delete(insertedVaccinationForBaby);
            insertedVaccinationForBaby = null;
        }
    }

    @Test
    @Transactional
    void createVaccinationForBaby() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VaccinationForBaby
        VaccinationForBabyDTO vaccinationForBabyDTO = vaccinationForBabyMapper.toDto(vaccinationForBaby);
        var returnedVaccinationForBabyDTO = om.readValue(
            restVaccinationForBabyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationForBabyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VaccinationForBabyDTO.class
        );

        // Validate the VaccinationForBaby in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVaccinationForBaby = vaccinationForBabyMapper.toEntity(returnedVaccinationForBabyDTO);
        assertVaccinationForBabyUpdatableFieldsEquals(
            returnedVaccinationForBaby,
            getPersistedVaccinationForBaby(returnedVaccinationForBaby)
        );

        insertedVaccinationForBaby = returnedVaccinationForBaby;
    }

    @Test
    @Transactional
    void createVaccinationForBabyWithExistingId() throws Exception {
        // Create the VaccinationForBaby with an existing ID
        vaccinationForBaby.setId(1L);
        VaccinationForBabyDTO vaccinationForBabyDTO = vaccinationForBabyMapper.toDto(vaccinationForBaby);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccinationForBabyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationForBabyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VaccinationForBaby in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVaccinationForBabies() throws Exception {
        // Initialize the database
        insertedVaccinationForBaby = vaccinationForBabyRepository.saveAndFlush(vaccinationForBaby);

        // Get all the vaccinationForBabyList
        restVaccinationForBabyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccinationForBaby.getId().intValue())))
            .andExpect(jsonPath("$.[*].vaccine").value(hasItem(DEFAULT_VACCINE.toString())))
            .andExpect(jsonPath("$.[*].numberUse").value(hasItem(DEFAULT_NUMBER_USE)));
    }

    @Test
    @Transactional
    void getVaccinationForBaby() throws Exception {
        // Initialize the database
        insertedVaccinationForBaby = vaccinationForBabyRepository.saveAndFlush(vaccinationForBaby);

        // Get the vaccinationForBaby
        restVaccinationForBabyMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccinationForBaby.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccinationForBaby.getId().intValue()))
            .andExpect(jsonPath("$.vaccine").value(DEFAULT_VACCINE.toString()))
            .andExpect(jsonPath("$.numberUse").value(DEFAULT_NUMBER_USE));
    }

    @Test
    @Transactional
    void getNonExistingVaccinationForBaby() throws Exception {
        // Get the vaccinationForBaby
        restVaccinationForBabyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVaccinationForBaby() throws Exception {
        // Initialize the database
        insertedVaccinationForBaby = vaccinationForBabyRepository.saveAndFlush(vaccinationForBaby);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccinationForBaby
        VaccinationForBaby updatedVaccinationForBaby = vaccinationForBabyRepository.findById(vaccinationForBaby.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVaccinationForBaby are not directly saved in db
        em.detach(updatedVaccinationForBaby);
        updatedVaccinationForBaby.vaccine(UPDATED_VACCINE).numberUse(UPDATED_NUMBER_USE);
        VaccinationForBabyDTO vaccinationForBabyDTO = vaccinationForBabyMapper.toDto(updatedVaccinationForBaby);

        restVaccinationForBabyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccinationForBabyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vaccinationForBabyDTO))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationForBaby in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVaccinationForBabyToMatchAllProperties(updatedVaccinationForBaby);
    }

    @Test
    @Transactional
    void putNonExistingVaccinationForBaby() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationForBaby.setId(longCount.incrementAndGet());

        // Create the VaccinationForBaby
        VaccinationForBabyDTO vaccinationForBabyDTO = vaccinationForBabyMapper.toDto(vaccinationForBaby);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationForBabyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccinationForBabyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vaccinationForBabyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationForBaby in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccinationForBaby() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationForBaby.setId(longCount.incrementAndGet());

        // Create the VaccinationForBaby
        VaccinationForBabyDTO vaccinationForBabyDTO = vaccinationForBabyMapper.toDto(vaccinationForBaby);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationForBabyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vaccinationForBabyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationForBaby in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccinationForBaby() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationForBaby.setId(longCount.incrementAndGet());

        // Create the VaccinationForBaby
        VaccinationForBabyDTO vaccinationForBabyDTO = vaccinationForBabyMapper.toDto(vaccinationForBaby);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationForBabyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationForBabyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccinationForBaby in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccinationForBabyWithPatch() throws Exception {
        // Initialize the database
        insertedVaccinationForBaby = vaccinationForBabyRepository.saveAndFlush(vaccinationForBaby);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccinationForBaby using partial update
        VaccinationForBaby partialUpdatedVaccinationForBaby = new VaccinationForBaby();
        partialUpdatedVaccinationForBaby.setId(vaccinationForBaby.getId());

        partialUpdatedVaccinationForBaby.vaccine(UPDATED_VACCINE).numberUse(UPDATED_NUMBER_USE);

        restVaccinationForBabyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccinationForBaby.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVaccinationForBaby))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationForBaby in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVaccinationForBabyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVaccinationForBaby, vaccinationForBaby),
            getPersistedVaccinationForBaby(vaccinationForBaby)
        );
    }

    @Test
    @Transactional
    void fullUpdateVaccinationForBabyWithPatch() throws Exception {
        // Initialize the database
        insertedVaccinationForBaby = vaccinationForBabyRepository.saveAndFlush(vaccinationForBaby);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccinationForBaby using partial update
        VaccinationForBaby partialUpdatedVaccinationForBaby = new VaccinationForBaby();
        partialUpdatedVaccinationForBaby.setId(vaccinationForBaby.getId());

        partialUpdatedVaccinationForBaby.vaccine(UPDATED_VACCINE).numberUse(UPDATED_NUMBER_USE);

        restVaccinationForBabyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccinationForBaby.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVaccinationForBaby))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationForBaby in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVaccinationForBabyUpdatableFieldsEquals(
            partialUpdatedVaccinationForBaby,
            getPersistedVaccinationForBaby(partialUpdatedVaccinationForBaby)
        );
    }

    @Test
    @Transactional
    void patchNonExistingVaccinationForBaby() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationForBaby.setId(longCount.incrementAndGet());

        // Create the VaccinationForBaby
        VaccinationForBabyDTO vaccinationForBabyDTO = vaccinationForBabyMapper.toDto(vaccinationForBaby);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationForBabyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccinationForBabyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vaccinationForBabyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationForBaby in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccinationForBaby() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationForBaby.setId(longCount.incrementAndGet());

        // Create the VaccinationForBaby
        VaccinationForBabyDTO vaccinationForBabyDTO = vaccinationForBabyMapper.toDto(vaccinationForBaby);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationForBabyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vaccinationForBabyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationForBaby in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccinationForBaby() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationForBaby.setId(longCount.incrementAndGet());

        // Create the VaccinationForBaby
        VaccinationForBabyDTO vaccinationForBabyDTO = vaccinationForBabyMapper.toDto(vaccinationForBaby);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationForBabyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vaccinationForBabyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccinationForBaby in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccinationForBaby() throws Exception {
        // Initialize the database
        insertedVaccinationForBaby = vaccinationForBabyRepository.saveAndFlush(vaccinationForBaby);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vaccinationForBaby
        restVaccinationForBabyMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccinationForBaby.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vaccinationForBabyRepository.count();
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

    protected VaccinationForBaby getPersistedVaccinationForBaby(VaccinationForBaby vaccinationForBaby) {
        return vaccinationForBabyRepository.findById(vaccinationForBaby.getId()).orElseThrow();
    }

    protected void assertPersistedVaccinationForBabyToMatchAllProperties(VaccinationForBaby expectedVaccinationForBaby) {
        assertVaccinationForBabyAllPropertiesEquals(expectedVaccinationForBaby, getPersistedVaccinationForBaby(expectedVaccinationForBaby));
    }

    protected void assertPersistedVaccinationForBabyToMatchUpdatableProperties(VaccinationForBaby expectedVaccinationForBaby) {
        assertVaccinationForBabyAllUpdatablePropertiesEquals(
            expectedVaccinationForBaby,
            getPersistedVaccinationForBaby(expectedVaccinationForBaby)
        );
    }
}
