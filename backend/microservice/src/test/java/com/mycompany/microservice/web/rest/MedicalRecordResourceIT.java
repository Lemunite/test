package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.MedicalRecordAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.MedicalRecord;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.repository.MedicalRecordRepository;
import com.mycompany.microservice.service.dto.MedicalRecordDTO;
import com.mycompany.microservice.service.mapper.MedicalRecordMapper;
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
 * Integration tests for the {@link MedicalRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicalRecordResourceIT {

    private static final LocalDate DEFAULT_EXAMINATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXAMINATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MEDICAL_HISTORY = "AAAAAAAAAA";
    private static final String UPDATED_MEDICAL_HISTORY = "BBBBBBBBBB";

    private static final Double DEFAULT_LEFT_EYE_NO_GLASS = 1D;
    private static final Double UPDATED_LEFT_EYE_NO_GLASS = 2D;

    private static final Double DEFAULT_RIGHT_EYE_NO_GLASS = 1D;
    private static final Double UPDATED_RIGHT_EYE_NO_GLASS = 2D;

    private static final Double DEFAULT_LEFT_EYE_WITH_GLASS = 1D;
    private static final Double UPDATED_LEFT_EYE_WITH_GLASS = 2D;

    private static final Double DEFAULT_RIGHT_EYE_WITH_GLASS = 1D;
    private static final Double UPDATED_RIGHT_EYE_WITH_GLASS = 2D;

    private static final Integer DEFAULT_PULSE = 1;
    private static final Integer UPDATED_PULSE = 2;

    private static final Double DEFAULT_TEMPERATURE = 1D;
    private static final Double UPDATED_TEMPERATURE = 2D;

    private static final String DEFAULT_BLOOD_PRESSURE = "AAAAAAAAAA";
    private static final String UPDATED_BLOOD_PRESSURE = "BBBBBBBBBB";

    private static final Integer DEFAULT_RESPIRATORY_RATE = 1;
    private static final Integer UPDATED_RESPIRATORY_RATE = 2;

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;

    private static final Double DEFAULT_HEIGHT = 1D;
    private static final Double UPDATED_HEIGHT = 2D;

    private static final Double DEFAULT_BMI = 1D;
    private static final Double UPDATED_BMI = 2D;

    private static final Double DEFAULT_WAIST = 1D;
    private static final Double UPDATED_WAIST = 2D;

    private static final String DEFAULT_SKIN_MUCOSA = "AAAAAAAAAA";
    private static final String UPDATED_SKIN_MUCOSA = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER = "AAAAAAAAAA";
    private static final String UPDATED_OTHER = "BBBBBBBBBB";

    private static final String DEFAULT_DISEASE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISEASE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISEASE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DISEASE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ADVICE = "AAAAAAAAAA";
    private static final String UPDATED_ADVICE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCTER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCTER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medical-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicalRecordMockMvc;

    private MedicalRecord medicalRecord;

    private MedicalRecord insertedMedicalRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalRecord createEntity(EntityManager em) {
        MedicalRecord medicalRecord = new MedicalRecord()
            .examinationDate(DEFAULT_EXAMINATION_DATE)
            .medicalHistory(DEFAULT_MEDICAL_HISTORY)
            .leftEyeNoGlass(DEFAULT_LEFT_EYE_NO_GLASS)
            .rightEyeNoGlass(DEFAULT_RIGHT_EYE_NO_GLASS)
            .leftEyeWithGlass(DEFAULT_LEFT_EYE_WITH_GLASS)
            .rightEyeWithGlass(DEFAULT_RIGHT_EYE_WITH_GLASS)
            .pulse(DEFAULT_PULSE)
            .temperature(DEFAULT_TEMPERATURE)
            .bloodPressure(DEFAULT_BLOOD_PRESSURE)
            .respiratoryRate(DEFAULT_RESPIRATORY_RATE)
            .weight(DEFAULT_WEIGHT)
            .height(DEFAULT_HEIGHT)
            .bmi(DEFAULT_BMI)
            .waist(DEFAULT_WAIST)
            .skinMucosa(DEFAULT_SKIN_MUCOSA)
            .other(DEFAULT_OTHER)
            .diseaseName(DEFAULT_DISEASE_NAME)
            .diseaseCode(DEFAULT_DISEASE_CODE)
            .advice(DEFAULT_ADVICE)
            .docterName(DEFAULT_DOCTER_NAME);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        medicalRecord.setPatient(patient);
        return medicalRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalRecord createUpdatedEntity(EntityManager em) {
        MedicalRecord updatedMedicalRecord = new MedicalRecord()
            .examinationDate(UPDATED_EXAMINATION_DATE)
            .medicalHistory(UPDATED_MEDICAL_HISTORY)
            .leftEyeNoGlass(UPDATED_LEFT_EYE_NO_GLASS)
            .rightEyeNoGlass(UPDATED_RIGHT_EYE_NO_GLASS)
            .leftEyeWithGlass(UPDATED_LEFT_EYE_WITH_GLASS)
            .rightEyeWithGlass(UPDATED_RIGHT_EYE_WITH_GLASS)
            .pulse(UPDATED_PULSE)
            .temperature(UPDATED_TEMPERATURE)
            .bloodPressure(UPDATED_BLOOD_PRESSURE)
            .respiratoryRate(UPDATED_RESPIRATORY_RATE)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .bmi(UPDATED_BMI)
            .waist(UPDATED_WAIST)
            .skinMucosa(UPDATED_SKIN_MUCOSA)
            .other(UPDATED_OTHER)
            .diseaseName(UPDATED_DISEASE_NAME)
            .diseaseCode(UPDATED_DISEASE_CODE)
            .advice(UPDATED_ADVICE)
            .docterName(UPDATED_DOCTER_NAME);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedMedicalRecord.setPatient(patient);
        return updatedMedicalRecord;
    }

    @BeforeEach
    void initTest() {
        medicalRecord = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMedicalRecord != null) {
            medicalRecordRepository.delete(insertedMedicalRecord);
            insertedMedicalRecord = null;
        }
    }

    @Test
    @Transactional
    void createMedicalRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);
        var returnedMedicalRecordDTO = om.readValue(
            restMedicalRecordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicalRecordDTO.class
        );

        // Validate the MedicalRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalRecord = medicalRecordMapper.toEntity(returnedMedicalRecordDTO);
        assertMedicalRecordUpdatableFieldsEquals(returnedMedicalRecord, getPersistedMedicalRecord(returnedMedicalRecord));

        insertedMedicalRecord = returnedMedicalRecord;
    }

    @Test
    @Transactional
    void createMedicalRecordWithExistingId() throws Exception {
        // Create the MedicalRecord with an existing ID
        medicalRecord.setId(1L);
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExaminationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalRecord.setExaminationDate(null);

        // Create the MedicalRecord, which fails.
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        restMedicalRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiseaseNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalRecord.setDiseaseName(null);

        // Create the MedicalRecord, which fails.
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        restMedicalRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocterNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalRecord.setDocterName(null);

        // Create the MedicalRecord, which fails.
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        restMedicalRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicalRecords() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList
        restMedicalRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].examinationDate").value(hasItem(DEFAULT_EXAMINATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].medicalHistory").value(hasItem(DEFAULT_MEDICAL_HISTORY)))
            .andExpect(jsonPath("$.[*].leftEyeNoGlass").value(hasItem(DEFAULT_LEFT_EYE_NO_GLASS)))
            .andExpect(jsonPath("$.[*].rightEyeNoGlass").value(hasItem(DEFAULT_RIGHT_EYE_NO_GLASS)))
            .andExpect(jsonPath("$.[*].leftEyeWithGlass").value(hasItem(DEFAULT_LEFT_EYE_WITH_GLASS)))
            .andExpect(jsonPath("$.[*].rightEyeWithGlass").value(hasItem(DEFAULT_RIGHT_EYE_WITH_GLASS)))
            .andExpect(jsonPath("$.[*].pulse").value(hasItem(DEFAULT_PULSE)))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE)))
            .andExpect(jsonPath("$.[*].bloodPressure").value(hasItem(DEFAULT_BLOOD_PRESSURE)))
            .andExpect(jsonPath("$.[*].respiratoryRate").value(hasItem(DEFAULT_RESPIRATORY_RATE)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].bmi").value(hasItem(DEFAULT_BMI)))
            .andExpect(jsonPath("$.[*].waist").value(hasItem(DEFAULT_WAIST)))
            .andExpect(jsonPath("$.[*].skinMucosa").value(hasItem(DEFAULT_SKIN_MUCOSA)))
            .andExpect(jsonPath("$.[*].other").value(hasItem(DEFAULT_OTHER)))
            .andExpect(jsonPath("$.[*].diseaseName").value(hasItem(DEFAULT_DISEASE_NAME)))
            .andExpect(jsonPath("$.[*].diseaseCode").value(hasItem(DEFAULT_DISEASE_CODE)))
            .andExpect(jsonPath("$.[*].advice").value(hasItem(DEFAULT_ADVICE)))
            .andExpect(jsonPath("$.[*].docterName").value(hasItem(DEFAULT_DOCTER_NAME)));
    }

    @Test
    @Transactional
    void getMedicalRecord() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get the medicalRecord
        restMedicalRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, medicalRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicalRecord.getId().intValue()))
            .andExpect(jsonPath("$.examinationDate").value(DEFAULT_EXAMINATION_DATE.toString()))
            .andExpect(jsonPath("$.medicalHistory").value(DEFAULT_MEDICAL_HISTORY))
            .andExpect(jsonPath("$.leftEyeNoGlass").value(DEFAULT_LEFT_EYE_NO_GLASS))
            .andExpect(jsonPath("$.rightEyeNoGlass").value(DEFAULT_RIGHT_EYE_NO_GLASS))
            .andExpect(jsonPath("$.leftEyeWithGlass").value(DEFAULT_LEFT_EYE_WITH_GLASS))
            .andExpect(jsonPath("$.rightEyeWithGlass").value(DEFAULT_RIGHT_EYE_WITH_GLASS))
            .andExpect(jsonPath("$.pulse").value(DEFAULT_PULSE))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE))
            .andExpect(jsonPath("$.bloodPressure").value(DEFAULT_BLOOD_PRESSURE))
            .andExpect(jsonPath("$.respiratoryRate").value(DEFAULT_RESPIRATORY_RATE))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.bmi").value(DEFAULT_BMI))
            .andExpect(jsonPath("$.waist").value(DEFAULT_WAIST))
            .andExpect(jsonPath("$.skinMucosa").value(DEFAULT_SKIN_MUCOSA))
            .andExpect(jsonPath("$.other").value(DEFAULT_OTHER))
            .andExpect(jsonPath("$.diseaseName").value(DEFAULT_DISEASE_NAME))
            .andExpect(jsonPath("$.diseaseCode").value(DEFAULT_DISEASE_CODE))
            .andExpect(jsonPath("$.advice").value(DEFAULT_ADVICE))
            .andExpect(jsonPath("$.docterName").value(DEFAULT_DOCTER_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMedicalRecord() throws Exception {
        // Get the medicalRecord
        restMedicalRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicalRecord() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord
        MedicalRecord updatedMedicalRecord = medicalRecordRepository.findById(medicalRecord.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicalRecord are not directly saved in db
        em.detach(updatedMedicalRecord);
        updatedMedicalRecord
            .examinationDate(UPDATED_EXAMINATION_DATE)
            .medicalHistory(UPDATED_MEDICAL_HISTORY)
            .leftEyeNoGlass(UPDATED_LEFT_EYE_NO_GLASS)
            .rightEyeNoGlass(UPDATED_RIGHT_EYE_NO_GLASS)
            .leftEyeWithGlass(UPDATED_LEFT_EYE_WITH_GLASS)
            .rightEyeWithGlass(UPDATED_RIGHT_EYE_WITH_GLASS)
            .pulse(UPDATED_PULSE)
            .temperature(UPDATED_TEMPERATURE)
            .bloodPressure(UPDATED_BLOOD_PRESSURE)
            .respiratoryRate(UPDATED_RESPIRATORY_RATE)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .bmi(UPDATED_BMI)
            .waist(UPDATED_WAIST)
            .skinMucosa(UPDATED_SKIN_MUCOSA)
            .other(UPDATED_OTHER)
            .diseaseName(UPDATED_DISEASE_NAME)
            .diseaseCode(UPDATED_DISEASE_CODE)
            .advice(UPDATED_ADVICE)
            .docterName(UPDATED_DOCTER_NAME);
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(updatedMedicalRecord);

        restMedicalRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalRecordToMatchAllProperties(updatedMedicalRecord);
    }

    @Test
    @Transactional
    void putNonExistingMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicalRecordWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord using partial update
        MedicalRecord partialUpdatedMedicalRecord = new MedicalRecord();
        partialUpdatedMedicalRecord.setId(medicalRecord.getId());

        partialUpdatedMedicalRecord
            .medicalHistory(UPDATED_MEDICAL_HISTORY)
            .leftEyeNoGlass(UPDATED_LEFT_EYE_NO_GLASS)
            .rightEyeWithGlass(UPDATED_RIGHT_EYE_WITH_GLASS)
            .bloodPressure(UPDATED_BLOOD_PRESSURE)
            .respiratoryRate(UPDATED_RESPIRATORY_RATE)
            .weight(UPDATED_WEIGHT)
            .skinMucosa(UPDATED_SKIN_MUCOSA)
            .diseaseName(UPDATED_DISEASE_NAME)
            .diseaseCode(UPDATED_DISEASE_CODE)
            .advice(UPDATED_ADVICE);

        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalRecord))
            )
            .andExpect(status().isOk());

        // Validate the MedicalRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalRecord, medicalRecord),
            getPersistedMedicalRecord(medicalRecord)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicalRecordWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalRecord using partial update
        MedicalRecord partialUpdatedMedicalRecord = new MedicalRecord();
        partialUpdatedMedicalRecord.setId(medicalRecord.getId());

        partialUpdatedMedicalRecord
            .examinationDate(UPDATED_EXAMINATION_DATE)
            .medicalHistory(UPDATED_MEDICAL_HISTORY)
            .leftEyeNoGlass(UPDATED_LEFT_EYE_NO_GLASS)
            .rightEyeNoGlass(UPDATED_RIGHT_EYE_NO_GLASS)
            .leftEyeWithGlass(UPDATED_LEFT_EYE_WITH_GLASS)
            .rightEyeWithGlass(UPDATED_RIGHT_EYE_WITH_GLASS)
            .pulse(UPDATED_PULSE)
            .temperature(UPDATED_TEMPERATURE)
            .bloodPressure(UPDATED_BLOOD_PRESSURE)
            .respiratoryRate(UPDATED_RESPIRATORY_RATE)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .bmi(UPDATED_BMI)
            .waist(UPDATED_WAIST)
            .skinMucosa(UPDATED_SKIN_MUCOSA)
            .other(UPDATED_OTHER)
            .diseaseName(UPDATED_DISEASE_NAME)
            .diseaseCode(UPDATED_DISEASE_CODE)
            .advice(UPDATED_ADVICE)
            .docterName(UPDATED_DOCTER_NAME);

        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalRecord))
            )
            .andExpect(status().isOk());

        // Validate the MedicalRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalRecordUpdatableFieldsEquals(partialUpdatedMedicalRecord, getPersistedMedicalRecord(partialUpdatedMedicalRecord));
    }

    @Test
    @Transactional
    void patchNonExistingMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicalRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicalRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalRecord.setId(longCount.incrementAndGet());

        // Create the MedicalRecord
        MedicalRecordDTO medicalRecordDTO = medicalRecordMapper.toDto(medicalRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalRecordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicalRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicalRecord() throws Exception {
        // Initialize the database
        insertedMedicalRecord = medicalRecordRepository.saveAndFlush(medicalRecord);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalRecord
        restMedicalRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicalRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalRecordRepository.count();
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

    protected MedicalRecord getPersistedMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.findById(medicalRecord.getId()).orElseThrow();
    }

    protected void assertPersistedMedicalRecordToMatchAllProperties(MedicalRecord expectedMedicalRecord) {
        assertMedicalRecordAllPropertiesEquals(expectedMedicalRecord, getPersistedMedicalRecord(expectedMedicalRecord));
    }

    protected void assertPersistedMedicalRecordToMatchUpdatableProperties(MedicalRecord expectedMedicalRecord) {
        assertMedicalRecordAllUpdatablePropertiesEquals(expectedMedicalRecord, getPersistedMedicalRecord(expectedMedicalRecord));
    }
}
