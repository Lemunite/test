package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.VaccinationTCMRAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.domain.VaccinationTCMR;
import com.mycompany.microservice.domain.enumeration.VaccineTCMRType;
import com.mycompany.microservice.repository.VaccinationTCMRRepository;
import com.mycompany.microservice.service.dto.VaccinationTCMRDTO;
import com.mycompany.microservice.service.mapper.VaccinationTCMRMapper;
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
 * Integration tests for the {@link VaccinationTCMRResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccinationTCMRResourceIT {

    private static final VaccineTCMRType DEFAULT_VACCINE = VaccineTCMRType.TA_1;
    private static final VaccineTCMRType UPDATED_VACCINE = VaccineTCMRType.TA_2;

    private static final Boolean DEFAULT_NOT_VACCINATED = false;
    private static final Boolean UPDATED_NOT_VACCINATED = true;

    private static final Boolean DEFAULT_VACCINATED = false;
    private static final Boolean UPDATED_VACCINATED = true;

    private static final LocalDate DEFAULT_INJECTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INJECTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REACTION = "AAAAAAAAAA";
    private static final String UPDATED_REACTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NEXT_APPOINTMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NEXT_APPOINTMENT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/vaccination-tcmrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VaccinationTCMRRepository vaccinationTCMRRepository;

    @Autowired
    private VaccinationTCMRMapper vaccinationTCMRMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccinationTCMRMockMvc;

    private VaccinationTCMR vaccinationTCMR;

    private VaccinationTCMR insertedVaccinationTCMR;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccinationTCMR createEntity(EntityManager em) {
        VaccinationTCMR vaccinationTCMR = new VaccinationTCMR()
            .vaccine(DEFAULT_VACCINE)
            .notVaccinated(DEFAULT_NOT_VACCINATED)
            .vaccinated(DEFAULT_VACCINATED)
            .injectionDate(DEFAULT_INJECTION_DATE)
            .reaction(DEFAULT_REACTION)
            .nextAppointment(DEFAULT_NEXT_APPOINTMENT);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        vaccinationTCMR.setPatient(patient);
        return vaccinationTCMR;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VaccinationTCMR createUpdatedEntity(EntityManager em) {
        VaccinationTCMR updatedVaccinationTCMR = new VaccinationTCMR()
            .vaccine(UPDATED_VACCINE)
            .notVaccinated(UPDATED_NOT_VACCINATED)
            .vaccinated(UPDATED_VACCINATED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .reaction(UPDATED_REACTION)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedVaccinationTCMR.setPatient(patient);
        return updatedVaccinationTCMR;
    }

    @BeforeEach
    void initTest() {
        vaccinationTCMR = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedVaccinationTCMR != null) {
            vaccinationTCMRRepository.delete(insertedVaccinationTCMR);
            insertedVaccinationTCMR = null;
        }
    }

    @Test
    @Transactional
    void createVaccinationTCMR() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VaccinationTCMR
        VaccinationTCMRDTO vaccinationTCMRDTO = vaccinationTCMRMapper.toDto(vaccinationTCMR);
        var returnedVaccinationTCMRDTO = om.readValue(
            restVaccinationTCMRMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationTCMRDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VaccinationTCMRDTO.class
        );

        // Validate the VaccinationTCMR in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVaccinationTCMR = vaccinationTCMRMapper.toEntity(returnedVaccinationTCMRDTO);
        assertVaccinationTCMRUpdatableFieldsEquals(returnedVaccinationTCMR, getPersistedVaccinationTCMR(returnedVaccinationTCMR));

        insertedVaccinationTCMR = returnedVaccinationTCMR;
    }

    @Test
    @Transactional
    void createVaccinationTCMRWithExistingId() throws Exception {
        // Create the VaccinationTCMR with an existing ID
        vaccinationTCMR.setId(1L);
        VaccinationTCMRDTO vaccinationTCMRDTO = vaccinationTCMRMapper.toDto(vaccinationTCMR);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccinationTCMRMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationTCMRDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VaccinationTCMR in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVaccineIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vaccinationTCMR.setVaccine(null);

        // Create the VaccinationTCMR, which fails.
        VaccinationTCMRDTO vaccinationTCMRDTO = vaccinationTCMRMapper.toDto(vaccinationTCMR);

        restVaccinationTCMRMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationTCMRDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVaccinationTCMRS() throws Exception {
        // Initialize the database
        insertedVaccinationTCMR = vaccinationTCMRRepository.saveAndFlush(vaccinationTCMR);

        // Get all the vaccinationTCMRList
        restVaccinationTCMRMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccinationTCMR.getId().intValue())))
            .andExpect(jsonPath("$.[*].vaccine").value(hasItem(DEFAULT_VACCINE.toString())))
            .andExpect(jsonPath("$.[*].notVaccinated").value(hasItem(DEFAULT_NOT_VACCINATED)))
            .andExpect(jsonPath("$.[*].vaccinated").value(hasItem(DEFAULT_VACCINATED)))
            .andExpect(jsonPath("$.[*].injectionDate").value(hasItem(DEFAULT_INJECTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].reaction").value(hasItem(DEFAULT_REACTION)))
            .andExpect(jsonPath("$.[*].nextAppointment").value(hasItem(DEFAULT_NEXT_APPOINTMENT.toString())));
    }

    @Test
    @Transactional
    void getVaccinationTCMR() throws Exception {
        // Initialize the database
        insertedVaccinationTCMR = vaccinationTCMRRepository.saveAndFlush(vaccinationTCMR);

        // Get the vaccinationTCMR
        restVaccinationTCMRMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccinationTCMR.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccinationTCMR.getId().intValue()))
            .andExpect(jsonPath("$.vaccine").value(DEFAULT_VACCINE.toString()))
            .andExpect(jsonPath("$.notVaccinated").value(DEFAULT_NOT_VACCINATED))
            .andExpect(jsonPath("$.vaccinated").value(DEFAULT_VACCINATED))
            .andExpect(jsonPath("$.injectionDate").value(DEFAULT_INJECTION_DATE.toString()))
            .andExpect(jsonPath("$.reaction").value(DEFAULT_REACTION))
            .andExpect(jsonPath("$.nextAppointment").value(DEFAULT_NEXT_APPOINTMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVaccinationTCMR() throws Exception {
        // Get the vaccinationTCMR
        restVaccinationTCMRMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVaccinationTCMR() throws Exception {
        // Initialize the database
        insertedVaccinationTCMR = vaccinationTCMRRepository.saveAndFlush(vaccinationTCMR);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccinationTCMR
        VaccinationTCMR updatedVaccinationTCMR = vaccinationTCMRRepository.findById(vaccinationTCMR.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVaccinationTCMR are not directly saved in db
        em.detach(updatedVaccinationTCMR);
        updatedVaccinationTCMR
            .vaccine(UPDATED_VACCINE)
            .notVaccinated(UPDATED_NOT_VACCINATED)
            .vaccinated(UPDATED_VACCINATED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .reaction(UPDATED_REACTION)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT);
        VaccinationTCMRDTO vaccinationTCMRDTO = vaccinationTCMRMapper.toDto(updatedVaccinationTCMR);

        restVaccinationTCMRMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccinationTCMRDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vaccinationTCMRDTO))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationTCMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVaccinationTCMRToMatchAllProperties(updatedVaccinationTCMR);
    }

    @Test
    @Transactional
    void putNonExistingVaccinationTCMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationTCMR.setId(longCount.incrementAndGet());

        // Create the VaccinationTCMR
        VaccinationTCMRDTO vaccinationTCMRDTO = vaccinationTCMRMapper.toDto(vaccinationTCMR);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationTCMRMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccinationTCMRDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vaccinationTCMRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationTCMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccinationTCMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationTCMR.setId(longCount.incrementAndGet());

        // Create the VaccinationTCMR
        VaccinationTCMRDTO vaccinationTCMRDTO = vaccinationTCMRMapper.toDto(vaccinationTCMR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationTCMRMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vaccinationTCMRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationTCMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccinationTCMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationTCMR.setId(longCount.incrementAndGet());

        // Create the VaccinationTCMR
        VaccinationTCMRDTO vaccinationTCMRDTO = vaccinationTCMRMapper.toDto(vaccinationTCMR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationTCMRMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vaccinationTCMRDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccinationTCMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccinationTCMRWithPatch() throws Exception {
        // Initialize the database
        insertedVaccinationTCMR = vaccinationTCMRRepository.saveAndFlush(vaccinationTCMR);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccinationTCMR using partial update
        VaccinationTCMR partialUpdatedVaccinationTCMR = new VaccinationTCMR();
        partialUpdatedVaccinationTCMR.setId(vaccinationTCMR.getId());

        partialUpdatedVaccinationTCMR
            .vaccine(UPDATED_VACCINE)
            .vaccinated(UPDATED_VACCINATED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT);

        restVaccinationTCMRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccinationTCMR.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVaccinationTCMR))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationTCMR in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVaccinationTCMRUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVaccinationTCMR, vaccinationTCMR),
            getPersistedVaccinationTCMR(vaccinationTCMR)
        );
    }

    @Test
    @Transactional
    void fullUpdateVaccinationTCMRWithPatch() throws Exception {
        // Initialize the database
        insertedVaccinationTCMR = vaccinationTCMRRepository.saveAndFlush(vaccinationTCMR);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vaccinationTCMR using partial update
        VaccinationTCMR partialUpdatedVaccinationTCMR = new VaccinationTCMR();
        partialUpdatedVaccinationTCMR.setId(vaccinationTCMR.getId());

        partialUpdatedVaccinationTCMR
            .vaccine(UPDATED_VACCINE)
            .notVaccinated(UPDATED_NOT_VACCINATED)
            .vaccinated(UPDATED_VACCINATED)
            .injectionDate(UPDATED_INJECTION_DATE)
            .reaction(UPDATED_REACTION)
            .nextAppointment(UPDATED_NEXT_APPOINTMENT);

        restVaccinationTCMRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccinationTCMR.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVaccinationTCMR))
            )
            .andExpect(status().isOk());

        // Validate the VaccinationTCMR in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVaccinationTCMRUpdatableFieldsEquals(
            partialUpdatedVaccinationTCMR,
            getPersistedVaccinationTCMR(partialUpdatedVaccinationTCMR)
        );
    }

    @Test
    @Transactional
    void patchNonExistingVaccinationTCMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationTCMR.setId(longCount.incrementAndGet());

        // Create the VaccinationTCMR
        VaccinationTCMRDTO vaccinationTCMRDTO = vaccinationTCMRMapper.toDto(vaccinationTCMR);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationTCMRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccinationTCMRDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vaccinationTCMRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationTCMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccinationTCMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationTCMR.setId(longCount.incrementAndGet());

        // Create the VaccinationTCMR
        VaccinationTCMRDTO vaccinationTCMRDTO = vaccinationTCMRMapper.toDto(vaccinationTCMR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationTCMRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vaccinationTCMRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VaccinationTCMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccinationTCMR() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vaccinationTCMR.setId(longCount.incrementAndGet());

        // Create the VaccinationTCMR
        VaccinationTCMRDTO vaccinationTCMRDTO = vaccinationTCMRMapper.toDto(vaccinationTCMR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationTCMRMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vaccinationTCMRDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VaccinationTCMR in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccinationTCMR() throws Exception {
        // Initialize the database
        insertedVaccinationTCMR = vaccinationTCMRRepository.saveAndFlush(vaccinationTCMR);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vaccinationTCMR
        restVaccinationTCMRMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccinationTCMR.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vaccinationTCMRRepository.count();
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

    protected VaccinationTCMR getPersistedVaccinationTCMR(VaccinationTCMR vaccinationTCMR) {
        return vaccinationTCMRRepository.findById(vaccinationTCMR.getId()).orElseThrow();
    }

    protected void assertPersistedVaccinationTCMRToMatchAllProperties(VaccinationTCMR expectedVaccinationTCMR) {
        assertVaccinationTCMRAllPropertiesEquals(expectedVaccinationTCMR, getPersistedVaccinationTCMR(expectedVaccinationTCMR));
    }

    protected void assertPersistedVaccinationTCMRToMatchUpdatableProperties(VaccinationTCMR expectedVaccinationTCMR) {
        assertVaccinationTCMRAllUpdatablePropertiesEquals(expectedVaccinationTCMR, getPersistedVaccinationTCMR(expectedVaccinationTCMR));
    }
}
