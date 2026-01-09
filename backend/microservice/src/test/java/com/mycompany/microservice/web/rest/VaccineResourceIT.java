package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.VaccineAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.VaccinationForBaby;
import com.mycompany.microservice.domain.Vaccine;
import com.mycompany.microservice.domain.enumeration.VaccineType;
import com.mycompany.microservice.repository.VaccineRepository;
import com.mycompany.microservice.service.dto.VaccineDTO;
import com.mycompany.microservice.service.mapper.VaccineMapper;
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
 * Integration tests for the {@link VaccineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccineResourceIT {

    private static final VaccineType DEFAULT_NAME = VaccineType.BCG;
    private static final VaccineType UPDATED_NAME = VaccineType.VGB_SO_SINH;

    private static final Boolean DEFAULT_NOT_VACCINATED = false;
    private static final Boolean UPDATED_NOT_VACCINATED = true;

    private static final LocalDate DEFAULT_INJECTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INJECTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REACTION = "AAAAAAAAAA";
    private static final String UPDATED_REACTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NEXT_APPOINTMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_APPOINTMENT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/vaccines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private VaccineMapper vaccineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccineMockMvc;

    private Vaccine vaccine;

    private Vaccine insertedVaccine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaccine createEntity(EntityManager em) {
        Vaccine vaccine = new Vaccine()
            .name(DEFAULT_NAME)
            .notVaccinated(DEFAULT_NOT_VACCINATED)
            .injectionDate(DEFAULT_INJECTION_DATE)
            .reaction(DEFAULT_REACTION)
            .nextAppointment(DEFAULT_NEXT_APPOINTMENT);
        // Add required entity
        VaccinationForBaby vaccinationForBaby;
        if (TestUtil.findAll(em, VaccinationForBaby.class).isEmpty()) {
            vaccinationForBaby = VaccinationForBabyResourceIT.createEntity(em);
            em.persist(vaccinationForBaby);
            em.flush();
        } else {
            vaccinationForBaby = TestUtil.findAll(em, VaccinationForBaby.class).get(0);
        }
        vaccine.setVaccinationForBaby(vaccinationForBaby);
        return vaccine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaccine createUpdatedEntity(EntityManager em) {
        Vaccine updatedVaccine = new Vaccine()
            .name(UPDATED_NAME)
            .notVaccinated(UPDATED_NOT_VACCINATED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .reaction(UPDATED_REACTION)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT);
        // Add required entity
        VaccinationForBaby vaccinationForBaby;
        if (TestUtil.findAll(em, VaccinationForBaby.class).isEmpty()) {
            vaccinationForBaby = VaccinationForBabyResourceIT.createUpdatedEntity(em);
            em.persist(vaccinationForBaby);
            em.flush();
        } else {
            vaccinationForBaby = TestUtil.findAll(em, VaccinationForBaby.class).get(0);
        }
        updatedVaccine.setVaccinationForBaby(vaccinationForBaby);
        return updatedVaccine;
    }

    @BeforeEach
    void initTest() {
        vaccine = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedVaccine != null) {
            vaccineRepository.delete(insertedVaccine);
            insertedVaccine = null;
        }
    }

    @Test
    @Transactional
    void createVaccine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);
        var returnedVaccineDTO = om.readValue(
            restVaccineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccineDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VaccineDTO.class
        );

        // Validate the Vaccine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVaccine = vaccineMapper.toEntity(returnedVaccineDTO);
        assertVaccineUpdatableFieldsEquals(returnedVaccine, getPersistedVaccine(returnedVaccine));

        insertedVaccine = returnedVaccine;
    }

    @Test
    @Transactional
    void createVaccineWithExistingId() throws Exception {
        // Create the Vaccine with an existing ID
        vaccine.setId(1L);
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vaccine.setName(null);

        // Create the Vaccine, which fails.
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        restVaccineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccineDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVaccines() throws Exception {
        // Initialize the database
        insertedVaccine = vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList
        restVaccineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].notVaccinated").value(hasItem(DEFAULT_NOT_VACCINATED)))
            .andExpect(jsonPath("$.[*].injectionDate").value(hasItem(DEFAULT_INJECTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].reaction").value(hasItem(DEFAULT_REACTION)))
            .andExpect(jsonPath("$.[*].nextAppointment").value(hasItem(DEFAULT_NEXT_APPOINTMENT.toString())));
    }

    @Test
    @Transactional
    void getVaccine() throws Exception {
        // Initialize the database
        insertedVaccine = vaccineRepository.saveAndFlush(vaccine);

        // Get the vaccine
        restVaccineMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.notVaccinated").value(DEFAULT_NOT_VACCINATED))
            .andExpect(jsonPath("$.injectionDate").value(DEFAULT_INJECTION_DATE.toString()))
            .andExpect(jsonPath("$.reaction").value(DEFAULT_REACTION))
            .andExpect(jsonPath("$.nextAppointment").value(DEFAULT_NEXT_APPOINTMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVaccine() throws Exception {
        // Get the vaccine
        restVaccineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVaccine() throws Exception {
        // Initialize the database
        insertedVaccine = vaccineRepository.saveAndFlush(vaccine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccine
        Vaccine updatedVaccine = vaccineRepository.findById(vaccine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVaccine are not directly saved in db
        em.detach(updatedVaccine);
        updatedVaccine
            .name(UPDATED_NAME)
            .notVaccinated(UPDATED_NOT_VACCINATED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .reaction(UPDATED_REACTION)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT);
        VaccineDTO vaccineDTO = vaccineMapper.toDto(updatedVaccine);

        restVaccineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccineDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vaccine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVaccineToMatchAllProperties(updatedVaccine);
    }

    @Test
    @Transactional
    void putNonExistingVaccine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccine.setId(longCount.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccineDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccine.setId(longCount.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vaccineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccine.setId(longCount.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaccine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccineWithPatch() throws Exception {
        // Initialize the database
        insertedVaccine = vaccineRepository.saveAndFlush(vaccine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccine using partial update
        Vaccine partialUpdatedVaccine = new Vaccine();
        partialUpdatedVaccine.setId(vaccine.getId());

        partialUpdatedVaccine.injectionDate(UPDATED_INJECTION_DATE).reaction(UPDATED_REACTION).nextAppointment(UPDATED_NEXT_APPOINTMENT);

        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVaccine))
            )
            .andExpect(status().isOk());

        // Validate the Vaccine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVaccineUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVaccine, vaccine), getPersistedVaccine(vaccine));
    }

    @Test
    @Transactional
    void fullUpdateVaccineWithPatch() throws Exception {
        // Initialize the database
        insertedVaccine = vaccineRepository.saveAndFlush(vaccine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccine using partial update
        Vaccine partialUpdatedVaccine = new Vaccine();
        partialUpdatedVaccine.setId(vaccine.getId());

        partialUpdatedVaccine
            .name(UPDATED_NAME)
            .notVaccinated(UPDATED_NOT_VACCINATED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .reaction(UPDATED_REACTION)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT);

        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVaccine))
            )
            .andExpect(status().isOk());

        // Validate the Vaccine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVaccineUpdatableFieldsEquals(partialUpdatedVaccine, getPersistedVaccine(partialUpdatedVaccine));
    }

    @Test
    @Transactional
    void patchNonExistingVaccine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccine.setId(longCount.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vaccineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccine.setId(longCount.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vaccineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccine.setId(longCount.incrementAndGet());

        // Create the Vaccine
        VaccineDTO vaccineDTO = vaccineMapper.toDto(vaccine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vaccineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaccine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccine() throws Exception {
        // Initialize the database
        insertedVaccine = vaccineRepository.saveAndFlush(vaccine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vaccine
        restVaccineMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vaccineRepository.count();
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

    protected Vaccine getPersistedVaccine(Vaccine vaccine) {
        return vaccineRepository.findById(vaccine.getId()).orElseThrow();
    }

    protected void assertPersistedVaccineToMatchAllProperties(Vaccine expectedVaccine) {
        assertVaccineAllPropertiesEquals(expectedVaccine, getPersistedVaccine(expectedVaccine));
    }

    protected void assertPersistedVaccineToMatchUpdatableProperties(Vaccine expectedVaccine) {
        assertVaccineAllUpdatablePropertiesEquals(expectedVaccine, getPersistedVaccine(expectedVaccine));
    }
}
