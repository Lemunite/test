package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.PatientAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.domain.enumeration.Gender;
import com.mycompany.microservice.repository.PatientRepository;
import com.mycompany.microservice.service.dto.PatientDTO;
import com.mycompany.microservice.service.mapper.PatientMapper;
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
 * Integration tests for the {@link PatientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PLACE_OF_BIRTH = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_OF_BIRTH = "BBBBBBBBBB";

    private static final String DEFAULT_BLOOD_TYPE_ABO = "AAAAAAAAAA";
    private static final String UPDATED_BLOOD_TYPE_ABO = "BBBBBBBBBB";

    private static final String DEFAULT_BLOOD_TYPE_RH = "AAAAAAAAAA";
    private static final String UPDATED_BLOOD_TYPE_RH = "BBBBBBBBBB";

    private static final String DEFAULT_ETHNIC = "AAAAAAAAAA";
    private static final String UPDATED_ETHNIC = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String DEFAULT_RELIGION = "AAAAAAAAAA";
    private static final String UPDATED_RELIGION = "BBBBBBBBBB";

    private static final String DEFAULT_JOB = "AAAAAAAAAA";
    private static final String UPDATED_JOB = "BBBBBBBBBB";

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ID_ISSUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ID_ISSUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ID_ISSUE_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_ID_ISSUE_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_HEALTH_INSURANCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HEALTH_INSURANCE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PERMANENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PERMANENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PERMANENT_WARD = "AAAAAAAAAA";
    private static final String UPDATED_PERMANENT_WARD = "BBBBBBBBBB";

    private static final String DEFAULT_PERMANENT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_PERMANENT_DISTRICT = "BBBBBBBBBB";

    private static final String DEFAULT_PERMANENT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PERMANENT_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_WARD = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_WARD = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_DISTRICT = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_LANDLINE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_LANDLINE_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MOTHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MOTHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FATHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FATHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CAREGIVER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CAREGIVER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CAREGIVER_RELATION = "AAAAAAAAAA";
    private static final String UPDATED_CAREGIVER_RELATION = "BBBBBBBBBB";

    private static final String DEFAULT_CAREGIVER_LANDLINE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_CAREGIVER_LANDLINE_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CAREGIVER_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_CAREGIVER_MOBILE_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    private Patient insertedPatient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity() {
        return new Patient()
            .fullName(DEFAULT_FULL_NAME)
            .gender(DEFAULT_GENDER)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .placeOfBirth(DEFAULT_PLACE_OF_BIRTH)
            .bloodTypeAbo(DEFAULT_BLOOD_TYPE_ABO)
            .bloodTypeRh(DEFAULT_BLOOD_TYPE_RH)
            .ethnic(DEFAULT_ETHNIC)
            .nationality(DEFAULT_NATIONALITY)
            .religion(DEFAULT_RELIGION)
            .job(DEFAULT_JOB)
            .idNumber(DEFAULT_ID_NUMBER)
            .idIssueDate(DEFAULT_ID_ISSUE_DATE)
            .idIssuePlace(DEFAULT_ID_ISSUE_PLACE)
            .healthInsuranceNumber(DEFAULT_HEALTH_INSURANCE_NUMBER)
            .permanentAddress(DEFAULT_PERMANENT_ADDRESS)
            .permanentWard(DEFAULT_PERMANENT_WARD)
            .permanentDistrict(DEFAULT_PERMANENT_DISTRICT)
            .permanentProvince(DEFAULT_PERMANENT_PROVINCE)
            .currentAddress(DEFAULT_CURRENT_ADDRESS)
            .currentWard(DEFAULT_CURRENT_WARD)
            .currentDistrict(DEFAULT_CURRENT_DISTRICT)
            .currentProvince(DEFAULT_CURRENT_PROVINCE)
            .landlinePhone(DEFAULT_LANDLINE_PHONE)
            .mobilePhone(DEFAULT_MOBILE_PHONE)
            .email(DEFAULT_EMAIL)
            .motherName(DEFAULT_MOTHER_NAME)
            .fatherName(DEFAULT_FATHER_NAME)
            .caregiverName(DEFAULT_CAREGIVER_NAME)
            .caregiverRelation(DEFAULT_CAREGIVER_RELATION)
            .caregiverLandlinePhone(DEFAULT_CAREGIVER_LANDLINE_PHONE)
            .caregiverMobilePhone(DEFAULT_CAREGIVER_MOBILE_PHONE)
            .familyCode(DEFAULT_FAMILY_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity() {
        return new Patient()
            .fullName(UPDATED_FULL_NAME)
            .gender(UPDATED_GENDER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .bloodTypeAbo(UPDATED_BLOOD_TYPE_ABO)
            .bloodTypeRh(UPDATED_BLOOD_TYPE_RH)
            .ethnic(UPDATED_ETHNIC)
            .nationality(UPDATED_NATIONALITY)
            .religion(UPDATED_RELIGION)
            .job(UPDATED_JOB)
            .idNumber(UPDATED_ID_NUMBER)
            .idIssueDate(UPDATED_ID_ISSUE_DATE)
            .idIssuePlace(UPDATED_ID_ISSUE_PLACE)
            .healthInsuranceNumber(UPDATED_HEALTH_INSURANCE_NUMBER)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .permanentWard(UPDATED_PERMANENT_WARD)
            .permanentDistrict(UPDATED_PERMANENT_DISTRICT)
            .permanentProvince(UPDATED_PERMANENT_PROVINCE)
            .currentAddress(UPDATED_CURRENT_ADDRESS)
            .currentWard(UPDATED_CURRENT_WARD)
            .currentDistrict(UPDATED_CURRENT_DISTRICT)
            .currentProvince(UPDATED_CURRENT_PROVINCE)
            .landlinePhone(UPDATED_LANDLINE_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .email(UPDATED_EMAIL)
            .motherName(UPDATED_MOTHER_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .caregiverName(UPDATED_CAREGIVER_NAME)
            .caregiverRelation(UPDATED_CAREGIVER_RELATION)
            .caregiverLandlinePhone(UPDATED_CAREGIVER_LANDLINE_PHONE)
            .caregiverMobilePhone(UPDATED_CAREGIVER_MOBILE_PHONE)
            .familyCode(UPDATED_FAMILY_CODE);
    }

    @BeforeEach
    void initTest() {
        patient = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPatient != null) {
            patientRepository.delete(insertedPatient);
            insertedPatient = null;
        }
    }

    @Test
    @Transactional
    void createPatient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);
        var returnedPatientDTO = om.readValue(
            restPatientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PatientDTO.class
        );

        // Validate the Patient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPatient = patientMapper.toEntity(returnedPatientDTO);
        assertPatientUpdatableFieldsEquals(returnedPatient, getPersistedPatient(returnedPatient));

        insertedPatient = returnedPatient;
    }

    @Test
    @Transactional
    void createPatientWithExistingId() throws Exception {
        // Create the Patient with an existing ID
        patient.setId(1L);
        PatientDTO patientDTO = patientMapper.toDto(patient);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setFullName(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setGender(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatients() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].placeOfBirth").value(hasItem(DEFAULT_PLACE_OF_BIRTH)))
            .andExpect(jsonPath("$.[*].bloodTypeAbo").value(hasItem(DEFAULT_BLOOD_TYPE_ABO)))
            .andExpect(jsonPath("$.[*].bloodTypeRh").value(hasItem(DEFAULT_BLOOD_TYPE_RH)))
            .andExpect(jsonPath("$.[*].ethnic").value(hasItem(DEFAULT_ETHNIC)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].religion").value(hasItem(DEFAULT_RELIGION)))
            .andExpect(jsonPath("$.[*].job").value(hasItem(DEFAULT_JOB)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].idIssueDate").value(hasItem(DEFAULT_ID_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].idIssuePlace").value(hasItem(DEFAULT_ID_ISSUE_PLACE)))
            .andExpect(jsonPath("$.[*].healthInsuranceNumber").value(hasItem(DEFAULT_HEALTH_INSURANCE_NUMBER)))
            .andExpect(jsonPath("$.[*].permanentAddress").value(hasItem(DEFAULT_PERMANENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].permanentWard").value(hasItem(DEFAULT_PERMANENT_WARD)))
            .andExpect(jsonPath("$.[*].permanentDistrict").value(hasItem(DEFAULT_PERMANENT_DISTRICT)))
            .andExpect(jsonPath("$.[*].permanentProvince").value(hasItem(DEFAULT_PERMANENT_PROVINCE)))
            .andExpect(jsonPath("$.[*].currentAddress").value(hasItem(DEFAULT_CURRENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].currentWard").value(hasItem(DEFAULT_CURRENT_WARD)))
            .andExpect(jsonPath("$.[*].currentDistrict").value(hasItem(DEFAULT_CURRENT_DISTRICT)))
            .andExpect(jsonPath("$.[*].currentProvince").value(hasItem(DEFAULT_CURRENT_PROVINCE)))
            .andExpect(jsonPath("$.[*].landlinePhone").value(hasItem(DEFAULT_LANDLINE_PHONE)))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].motherName").value(hasItem(DEFAULT_MOTHER_NAME)))
            .andExpect(jsonPath("$.[*].fatherName").value(hasItem(DEFAULT_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].caregiverName").value(hasItem(DEFAULT_CAREGIVER_NAME)))
            .andExpect(jsonPath("$.[*].caregiverRelation").value(hasItem(DEFAULT_CAREGIVER_RELATION)))
            .andExpect(jsonPath("$.[*].caregiverLandlinePhone").value(hasItem(DEFAULT_CAREGIVER_LANDLINE_PHONE)))
            .andExpect(jsonPath("$.[*].caregiverMobilePhone").value(hasItem(DEFAULT_CAREGIVER_MOBILE_PHONE)))
            .andExpect(jsonPath("$.[*].familyCode").value(hasItem(DEFAULT_FAMILY_CODE)));
    }

    @Test
    @Transactional
    void getPatient() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc
            .perform(get(ENTITY_API_URL_ID, patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.placeOfBirth").value(DEFAULT_PLACE_OF_BIRTH))
            .andExpect(jsonPath("$.bloodTypeAbo").value(DEFAULT_BLOOD_TYPE_ABO))
            .andExpect(jsonPath("$.bloodTypeRh").value(DEFAULT_BLOOD_TYPE_RH))
            .andExpect(jsonPath("$.ethnic").value(DEFAULT_ETHNIC))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.religion").value(DEFAULT_RELIGION))
            .andExpect(jsonPath("$.job").value(DEFAULT_JOB))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.idIssueDate").value(DEFAULT_ID_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.idIssuePlace").value(DEFAULT_ID_ISSUE_PLACE))
            .andExpect(jsonPath("$.healthInsuranceNumber").value(DEFAULT_HEALTH_INSURANCE_NUMBER))
            .andExpect(jsonPath("$.permanentAddress").value(DEFAULT_PERMANENT_ADDRESS))
            .andExpect(jsonPath("$.permanentWard").value(DEFAULT_PERMANENT_WARD))
            .andExpect(jsonPath("$.permanentDistrict").value(DEFAULT_PERMANENT_DISTRICT))
            .andExpect(jsonPath("$.permanentProvince").value(DEFAULT_PERMANENT_PROVINCE))
            .andExpect(jsonPath("$.currentAddress").value(DEFAULT_CURRENT_ADDRESS))
            .andExpect(jsonPath("$.currentWard").value(DEFAULT_CURRENT_WARD))
            .andExpect(jsonPath("$.currentDistrict").value(DEFAULT_CURRENT_DISTRICT))
            .andExpect(jsonPath("$.currentProvince").value(DEFAULT_CURRENT_PROVINCE))
            .andExpect(jsonPath("$.landlinePhone").value(DEFAULT_LANDLINE_PHONE))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.motherName").value(DEFAULT_MOTHER_NAME))
            .andExpect(jsonPath("$.fatherName").value(DEFAULT_FATHER_NAME))
            .andExpect(jsonPath("$.caregiverName").value(DEFAULT_CAREGIVER_NAME))
            .andExpect(jsonPath("$.caregiverRelation").value(DEFAULT_CAREGIVER_RELATION))
            .andExpect(jsonPath("$.caregiverLandlinePhone").value(DEFAULT_CAREGIVER_LANDLINE_PHONE))
            .andExpect(jsonPath("$.caregiverMobilePhone").value(DEFAULT_CAREGIVER_MOBILE_PHONE))
            .andExpect(jsonPath("$.familyCode").value(DEFAULT_FAMILY_CODE));
    }

    @Test
    @Transactional
    void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPatient() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .fullName(UPDATED_FULL_NAME)
            .gender(UPDATED_GENDER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .bloodTypeAbo(UPDATED_BLOOD_TYPE_ABO)
            .bloodTypeRh(UPDATED_BLOOD_TYPE_RH)
            .ethnic(UPDATED_ETHNIC)
            .nationality(UPDATED_NATIONALITY)
            .religion(UPDATED_RELIGION)
            .job(UPDATED_JOB)
            .idNumber(UPDATED_ID_NUMBER)
            .idIssueDate(UPDATED_ID_ISSUE_DATE)
            .idIssuePlace(UPDATED_ID_ISSUE_PLACE)
            .healthInsuranceNumber(UPDATED_HEALTH_INSURANCE_NUMBER)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .permanentWard(UPDATED_PERMANENT_WARD)
            .permanentDistrict(UPDATED_PERMANENT_DISTRICT)
            .permanentProvince(UPDATED_PERMANENT_PROVINCE)
            .currentAddress(UPDATED_CURRENT_ADDRESS)
            .currentWard(UPDATED_CURRENT_WARD)
            .currentDistrict(UPDATED_CURRENT_DISTRICT)
            .currentProvince(UPDATED_CURRENT_PROVINCE)
            .landlinePhone(UPDATED_LANDLINE_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .email(UPDATED_EMAIL)
            .motherName(UPDATED_MOTHER_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .caregiverName(UPDATED_CAREGIVER_NAME)
            .caregiverRelation(UPDATED_CAREGIVER_RELATION)
            .caregiverLandlinePhone(UPDATED_CAREGIVER_LANDLINE_PHONE)
            .caregiverMobilePhone(UPDATED_CAREGIVER_MOBILE_PHONE)
            .familyCode(UPDATED_FAMILY_CODE);
        PatientDTO patientDTO = patientMapper.toDto(updatedPatient);

        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPatientToMatchAllProperties(updatedPatient);
    }

    @Test
    @Transactional
    void putNonExistingPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .fullName(UPDATED_FULL_NAME)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .ethnic(UPDATED_ETHNIC)
            .religion(UPDATED_RELIGION)
            .job(UPDATED_JOB)
            .idIssueDate(UPDATED_ID_ISSUE_DATE)
            .idIssuePlace(UPDATED_ID_ISSUE_PLACE)
            .healthInsuranceNumber(UPDATED_HEALTH_INSURANCE_NUMBER)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .permanentWard(UPDATED_PERMANENT_WARD)
            .permanentDistrict(UPDATED_PERMANENT_DISTRICT)
            .permanentProvince(UPDATED_PERMANENT_PROVINCE)
            .currentWard(UPDATED_CURRENT_WARD)
            .currentDistrict(UPDATED_CURRENT_DISTRICT)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .motherName(UPDATED_MOTHER_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .caregiverLandlinePhone(UPDATED_CAREGIVER_LANDLINE_PHONE)
            .familyCode(UPDATED_FAMILY_CODE);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatientUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPatient, patient), getPersistedPatient(patient));
    }

    @Test
    @Transactional
    void fullUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .fullName(UPDATED_FULL_NAME)
            .gender(UPDATED_GENDER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .bloodTypeAbo(UPDATED_BLOOD_TYPE_ABO)
            .bloodTypeRh(UPDATED_BLOOD_TYPE_RH)
            .ethnic(UPDATED_ETHNIC)
            .nationality(UPDATED_NATIONALITY)
            .religion(UPDATED_RELIGION)
            .job(UPDATED_JOB)
            .idNumber(UPDATED_ID_NUMBER)
            .idIssueDate(UPDATED_ID_ISSUE_DATE)
            .idIssuePlace(UPDATED_ID_ISSUE_PLACE)
            .healthInsuranceNumber(UPDATED_HEALTH_INSURANCE_NUMBER)
            .permanentAddress(UPDATED_PERMANENT_ADDRESS)
            .permanentWard(UPDATED_PERMANENT_WARD)
            .permanentDistrict(UPDATED_PERMANENT_DISTRICT)
            .permanentProvince(UPDATED_PERMANENT_PROVINCE)
            .currentAddress(UPDATED_CURRENT_ADDRESS)
            .currentWard(UPDATED_CURRENT_WARD)
            .currentDistrict(UPDATED_CURRENT_DISTRICT)
            .currentProvince(UPDATED_CURRENT_PROVINCE)
            .landlinePhone(UPDATED_LANDLINE_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .email(UPDATED_EMAIL)
            .motherName(UPDATED_MOTHER_NAME)
            .fatherName(UPDATED_FATHER_NAME)
            .caregiverName(UPDATED_CAREGIVER_NAME)
            .caregiverRelation(UPDATED_CAREGIVER_RELATION)
            .caregiverLandlinePhone(UPDATED_CAREGIVER_LANDLINE_PHONE)
            .caregiverMobilePhone(UPDATED_CAREGIVER_MOBILE_PHONE)
            .familyCode(UPDATED_FAMILY_CODE);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatientUpdatableFieldsEquals(partialUpdatedPatient, getPersistedPatient(partialUpdatedPatient));
    }

    @Test
    @Transactional
    void patchNonExistingPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatient() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the patient
        restPatientMockMvc
            .perform(delete(ENTITY_API_URL_ID, patient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return patientRepository.count();
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

    protected Patient getPersistedPatient(Patient patient) {
        return patientRepository.findById(patient.getId()).orElseThrow();
    }

    protected void assertPersistedPatientToMatchAllProperties(Patient expectedPatient) {
        assertPatientAllPropertiesEquals(expectedPatient, getPersistedPatient(expectedPatient));
    }

    protected void assertPersistedPatientToMatchUpdatableProperties(Patient expectedPatient) {
        assertPatientAllUpdatablePropertiesEquals(expectedPatient, getPersistedPatient(expectedPatient));
    }
}
