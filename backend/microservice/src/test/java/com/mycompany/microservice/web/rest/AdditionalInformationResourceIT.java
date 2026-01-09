package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.AdditionalInformationAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.AdditionalInformation;
import com.mycompany.microservice.domain.enumeration.BirthStatusType;
import com.mycompany.microservice.domain.enumeration.RiskLevel;
import com.mycompany.microservice.domain.enumeration.RiskLevel;
import com.mycompany.microservice.domain.enumeration.RiskLevel;
import com.mycompany.microservice.domain.enumeration.RiskLevelAlcohol;
import com.mycompany.microservice.domain.enumeration.TypeToilet;
import com.mycompany.microservice.repository.AdditionalInformationRepository;
import com.mycompany.microservice.service.dto.AdditionalInformationDTO;
import com.mycompany.microservice.service.mapper.AdditionalInformationMapper;
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
 * Integration tests for the {@link AdditionalInformationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdditionalInformationResourceIT {

    private static final RiskLevel DEFAULT_SMOKING = RiskLevel.YES;
    private static final RiskLevel UPDATED_SMOKING = RiskLevel.NO;

    private static final RiskLevelAlcohol DEFAULT_ALCOHOL_RISK_LEVEL = RiskLevelAlcohol.YES;
    private static final RiskLevelAlcohol UPDATED_ALCOHOL_RISK_LEVEL = RiskLevelAlcohol.NO;

    private static final Integer DEFAULT_ALCOHOL_GLASSES_PER_DAY = 1;
    private static final Integer UPDATED_ALCOHOL_GLASSES_PER_DAY = 2;

    private static final RiskLevel DEFAULT_DRUG_USE = RiskLevel.YES;
    private static final RiskLevel UPDATED_DRUG_USE = RiskLevel.NO;

    private static final RiskLevel DEFAULT_PHYSICAL_ACTIVITY = RiskLevel.YES;
    private static final RiskLevel UPDATED_PHYSICAL_ACTIVITY = RiskLevel.NO;

    private static final String DEFAULT_EXPOSURE_FACTOR = "AAAAAAAAAA";
    private static final String UPDATED_EXPOSURE_FACTOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPOSURE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPOSURE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final TypeToilet DEFAULT_TYPE_TOILET = TypeToilet.FLUSH;
    private static final TypeToilet UPDATED_TYPE_TOILET = TypeToilet.DOUBLE;

    private static final String DEFAULT_ENVIRONMENTAL_RISK = "AAAAAAAAAA";
    private static final String UPDATED_ENVIRONMENTAL_RISK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CARDIOVASCULAR_DISEASE = false;
    private static final Boolean UPDATED_CARDIOVASCULAR_DISEASE = true;

    private static final Boolean DEFAULT_HYPERTENSION = false;
    private static final Boolean UPDATED_HYPERTENSION = true;

    private static final Boolean DEFAULT_DIABETES = false;
    private static final Boolean UPDATED_DIABETES = true;

    private static final Boolean DEFAULT_STOMACH_DISEASE = false;
    private static final Boolean UPDATED_STOMACH_DISEASE = true;

    private static final Boolean DEFAULT_CHRONIC_LUNG_DISEASE = false;
    private static final Boolean UPDATED_CHRONIC_LUNG_DISEASE = true;

    private static final Boolean DEFAULT_ASTHMA = false;
    private static final Boolean UPDATED_ASTHMA = true;

    private static final Boolean DEFAULT_GOITER = false;
    private static final Boolean UPDATED_GOITER = true;

    private static final Boolean DEFAULT_HEPATITIS = false;
    private static final Boolean UPDATED_HEPATITIS = true;

    private static final Boolean DEFAULT_CONGENITAL_HEART_DISEASE = false;
    private static final Boolean UPDATED_CONGENITAL_HEART_DISEASE = true;

    private static final Boolean DEFAULT_MENTAL_DISORDERS = false;
    private static final Boolean UPDATED_MENTAL_DISORDERS = true;

    private static final Boolean DEFAULT_AUTISM = false;
    private static final Boolean UPDATED_AUTISM = true;

    private static final Boolean DEFAULT_EPILEPSY = false;
    private static final Boolean UPDATED_EPILEPSY = true;

    private static final String DEFAULT_CANCER = "AAAAAAAAAA";
    private static final String UPDATED_CANCER = "BBBBBBBBBB";

    private static final String DEFAULT_TUBERCULOSIS = "AAAAAAAAAA";
    private static final String UPDATED_TUBERCULOSIS = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_DISEASES = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_DISEASES = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACEPTIVE_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACEPTIVE_METHOD = "BBBBBBBBBB";

    private static final Integer DEFAULT_LAST_PREGNANCY = 1;
    private static final Integer UPDATED_LAST_PREGNANCY = 2;

    private static final Integer DEFAULT_NUMBER_OF_PREGNANCIES = 1;
    private static final Integer UPDATED_NUMBER_OF_PREGNANCIES = 2;

    private static final Integer DEFAULT_NUMBER_OF_MISCARRIAGES = 1;
    private static final Integer UPDATED_NUMBER_OF_MISCARRIAGES = 2;

    private static final Integer DEFAULT_NUMBER_OF_ABORTIONS = 1;
    private static final Integer UPDATED_NUMBER_OF_ABORTIONS = 2;

    private static final Integer DEFAULT_NUMBER_OF_BIRTHS = 1;
    private static final Integer UPDATED_NUMBER_OF_BIRTHS = 2;

    private static final Integer DEFAULT_VAGINAL_DELIVERY = 1;
    private static final Integer UPDATED_VAGINAL_DELIVERY = 2;

    private static final Integer DEFAULT_CESAREAN_SECTION = 1;
    private static final Integer UPDATED_CESAREAN_SECTION = 2;

    private static final Integer DEFAULT_DIFFICULT_DELIVERY = 1;
    private static final Integer UPDATED_DIFFICULT_DELIVERY = 2;

    private static final Integer DEFAULT_NUMBER_OF_FULL_TERM_BIRTHS = 1;
    private static final Integer UPDATED_NUMBER_OF_FULL_TERM_BIRTHS = 2;

    private static final Integer DEFAULT_NUMBER_OF_PREMATURE_BIRTHS = 1;
    private static final Integer UPDATED_NUMBER_OF_PREMATURE_BIRTHS = 2;

    private static final Integer DEFAULT_NUMBER_OF_CHILDREN_ALIVE = 1;
    private static final Integer UPDATED_NUMBER_OF_CHILDREN_ALIVE = 2;

    private static final String DEFAULT_GYNECOLOGICAL_DISEASES = "AAAAAAAAAA";
    private static final String UPDATED_GYNECOLOGICAL_DISEASES = "BBBBBBBBBB";

    private static final BirthStatusType DEFAULT_BIRTH_STATUS = BirthStatusType.NORMAL;
    private static final BirthStatusType UPDATED_BIRTH_STATUS = BirthStatusType.CSECTION;

    private static final Double DEFAULT_BIRTH_WEIGHT = 1D;
    private static final Double UPDATED_BIRTH_WEIGHT = 2D;

    private static final Double DEFAULT_BIRTH_HEIGHT = 1D;
    private static final Double UPDATED_BIRTH_HEIGHT = 2D;

    private static final String DEFAULT_BIRTH_DEFECT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_DEFECT_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_BIRTH_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_BIRTH_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_HEALTH_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_HEALTH_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/additional-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdditionalInformationRepository additionalInformationRepository;

    @Autowired
    private AdditionalInformationMapper additionalInformationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdditionalInformationMockMvc;

    private AdditionalInformation additionalInformation;

    private AdditionalInformation insertedAdditionalInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdditionalInformation createEntity(EntityManager em) {
        AdditionalInformation additionalInformation = new AdditionalInformation()
            .smoking(DEFAULT_SMOKING)
            .alcoholRiskLevel(DEFAULT_ALCOHOL_RISK_LEVEL)
            .alcoholGlassesPerDay(DEFAULT_ALCOHOL_GLASSES_PER_DAY)
            .drugUse(DEFAULT_DRUG_USE)
            .physicalActivity(DEFAULT_PHYSICAL_ACTIVITY)
            .exposureFactor(DEFAULT_EXPOSURE_FACTOR)
            .exposureDate(DEFAULT_EXPOSURE_DATE)
            .typeToilet(DEFAULT_TYPE_TOILET)
            .environmentalRisk(DEFAULT_ENVIRONMENTAL_RISK)
            .cardiovascularDisease(DEFAULT_CARDIOVASCULAR_DISEASE)
            .hypertension(DEFAULT_HYPERTENSION)
            .diabetes(DEFAULT_DIABETES)
            .stomachDisease(DEFAULT_STOMACH_DISEASE)
            .chronicLungDisease(DEFAULT_CHRONIC_LUNG_DISEASE)
            .asthma(DEFAULT_ASTHMA)
            .goiter(DEFAULT_GOITER)
            .hepatitis(DEFAULT_HEPATITIS)
            .congenitalHeartDisease(DEFAULT_CONGENITAL_HEART_DISEASE)
            .mentalDisorders(DEFAULT_MENTAL_DISORDERS)
            .autism(DEFAULT_AUTISM)
            .epilepsy(DEFAULT_EPILEPSY)
            .cancer(DEFAULT_CANCER)
            .tuberculosis(DEFAULT_TUBERCULOSIS)
            .otherDiseases(DEFAULT_OTHER_DISEASES)
            .contraceptiveMethod(DEFAULT_CONTRACEPTIVE_METHOD)
            .lastPregnancy(DEFAULT_LAST_PREGNANCY)
            .numberOfPregnancies(DEFAULT_NUMBER_OF_PREGNANCIES)
            .numberOfMiscarriages(DEFAULT_NUMBER_OF_MISCARRIAGES)
            .numberOfAbortions(DEFAULT_NUMBER_OF_ABORTIONS)
            .numberOfBirths(DEFAULT_NUMBER_OF_BIRTHS)
            .vaginalDelivery(DEFAULT_VAGINAL_DELIVERY)
            .cesareanSection(DEFAULT_CESAREAN_SECTION)
            .difficultDelivery(DEFAULT_DIFFICULT_DELIVERY)
            .numberOfFullTermBirths(DEFAULT_NUMBER_OF_FULL_TERM_BIRTHS)
            .numberOfPrematureBirths(DEFAULT_NUMBER_OF_PREMATURE_BIRTHS)
            .numberOfChildrenAlive(DEFAULT_NUMBER_OF_CHILDREN_ALIVE)
            .gynecologicalDiseases(DEFAULT_GYNECOLOGICAL_DISEASES)
            .birthStatus(DEFAULT_BIRTH_STATUS)
            .birthWeight(DEFAULT_BIRTH_WEIGHT)
            .birthHeight(DEFAULT_BIRTH_HEIGHT)
            .birthDefectNote(DEFAULT_BIRTH_DEFECT_NOTE)
            .otherBirthNote(DEFAULT_OTHER_BIRTH_NOTE)
            .otherHealthNote(DEFAULT_OTHER_HEALTH_NOTE);
        return additionalInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdditionalInformation createUpdatedEntity(EntityManager em) {
        AdditionalInformation updatedAdditionalInformation = new AdditionalInformation()
            .smoking(UPDATED_SMOKING)
            .alcoholRiskLevel(UPDATED_ALCOHOL_RISK_LEVEL)
            .alcoholGlassesPerDay(UPDATED_ALCOHOL_GLASSES_PER_DAY)
            .drugUse(UPDATED_DRUG_USE)
            .physicalActivity(UPDATED_PHYSICAL_ACTIVITY)
            .exposureFactor(UPDATED_EXPOSURE_FACTOR)
            .exposureDate(UPDATED_EXPOSURE_DATE)
            .typeToilet(UPDATED_TYPE_TOILET)
            .environmentalRisk(UPDATED_ENVIRONMENTAL_RISK)
            .cardiovascularDisease(UPDATED_CARDIOVASCULAR_DISEASE)
            .hypertension(UPDATED_HYPERTENSION)
            .diabetes(UPDATED_DIABETES)
            .stomachDisease(UPDATED_STOMACH_DISEASE)
            .chronicLungDisease(UPDATED_CHRONIC_LUNG_DISEASE)
            .asthma(UPDATED_ASTHMA)
            .goiter(UPDATED_GOITER)
            .hepatitis(UPDATED_HEPATITIS)
            .congenitalHeartDisease(UPDATED_CONGENITAL_HEART_DISEASE)
            .mentalDisorders(UPDATED_MENTAL_DISORDERS)
            .autism(UPDATED_AUTISM)
            .epilepsy(UPDATED_EPILEPSY)
            .cancer(UPDATED_CANCER)
            .tuberculosis(UPDATED_TUBERCULOSIS)
            .otherDiseases(UPDATED_OTHER_DISEASES)
            .contraceptiveMethod(UPDATED_CONTRACEPTIVE_METHOD)
            .lastPregnancy(UPDATED_LAST_PREGNANCY)
            .numberOfPregnancies(UPDATED_NUMBER_OF_PREGNANCIES)
            .numberOfMiscarriages(UPDATED_NUMBER_OF_MISCARRIAGES)
            .numberOfAbortions(UPDATED_NUMBER_OF_ABORTIONS)
            .numberOfBirths(UPDATED_NUMBER_OF_BIRTHS)
            .vaginalDelivery(UPDATED_VAGINAL_DELIVERY)
            .cesareanSection(UPDATED_CESAREAN_SECTION)
            .difficultDelivery(UPDATED_DIFFICULT_DELIVERY)
            .numberOfFullTermBirths(UPDATED_NUMBER_OF_FULL_TERM_BIRTHS)
            .numberOfPrematureBirths(UPDATED_NUMBER_OF_PREMATURE_BIRTHS)
            .numberOfChildrenAlive(UPDATED_NUMBER_OF_CHILDREN_ALIVE)
            .gynecologicalDiseases(UPDATED_GYNECOLOGICAL_DISEASES)
            .birthStatus(UPDATED_BIRTH_STATUS)
            .birthWeight(UPDATED_BIRTH_WEIGHT)
            .birthHeight(UPDATED_BIRTH_HEIGHT)
            .birthDefectNote(UPDATED_BIRTH_DEFECT_NOTE)
            .otherBirthNote(UPDATED_OTHER_BIRTH_NOTE)
            .otherHealthNote(UPDATED_OTHER_HEALTH_NOTE);
        return updatedAdditionalInformation;
    }

    @BeforeEach
    void initTest() {
        additionalInformation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAdditionalInformation != null) {
            additionalInformationRepository.delete(insertedAdditionalInformation);
            insertedAdditionalInformation = null;
        }
    }

    @Test
    @Transactional
    void createAdditionalInformation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AdditionalInformation
        AdditionalInformationDTO additionalInformationDTO = additionalInformationMapper.toDto(additionalInformation);
        var returnedAdditionalInformationDTO = om.readValue(
            restAdditionalInformationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalInformationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdditionalInformationDTO.class
        );

        // Validate the AdditionalInformation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdditionalInformation = additionalInformationMapper.toEntity(returnedAdditionalInformationDTO);
        assertAdditionalInformationUpdatableFieldsEquals(
            returnedAdditionalInformation,
            getPersistedAdditionalInformation(returnedAdditionalInformation)
        );

        insertedAdditionalInformation = returnedAdditionalInformation;
    }

    @Test
    @Transactional
    void createAdditionalInformationWithExistingId() throws Exception {
        // Create the AdditionalInformation with an existing ID
        additionalInformation.setId(1L);
        AdditionalInformationDTO additionalInformationDTO = additionalInformationMapper.toDto(additionalInformation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdditionalInformationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalInformationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdditionalInformation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBirthStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        additionalInformation.setBirthStatus(null);

        // Create the AdditionalInformation, which fails.
        AdditionalInformationDTO additionalInformationDTO = additionalInformationMapper.toDto(additionalInformation);

        restAdditionalInformationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalInformationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdditionalInformations() throws Exception {
        // Initialize the database
        insertedAdditionalInformation = additionalInformationRepository.saveAndFlush(additionalInformation);

        // Get all the additionalInformationList
        restAdditionalInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(additionalInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].smoking").value(hasItem(DEFAULT_SMOKING.toString())))
            .andExpect(jsonPath("$.[*].alcoholRiskLevel").value(hasItem(DEFAULT_ALCOHOL_RISK_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].alcoholGlassesPerDay").value(hasItem(DEFAULT_ALCOHOL_GLASSES_PER_DAY)))
            .andExpect(jsonPath("$.[*].drugUse").value(hasItem(DEFAULT_DRUG_USE.toString())))
            .andExpect(jsonPath("$.[*].physicalActivity").value(hasItem(DEFAULT_PHYSICAL_ACTIVITY.toString())))
            .andExpect(jsonPath("$.[*].exposureFactor").value(hasItem(DEFAULT_EXPOSURE_FACTOR)))
            .andExpect(jsonPath("$.[*].exposureDate").value(hasItem(DEFAULT_EXPOSURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].typeToilet").value(hasItem(DEFAULT_TYPE_TOILET.toString())))
            .andExpect(jsonPath("$.[*].environmentalRisk").value(hasItem(DEFAULT_ENVIRONMENTAL_RISK)))
            .andExpect(jsonPath("$.[*].cardiovascularDisease").value(hasItem(DEFAULT_CARDIOVASCULAR_DISEASE)))
            .andExpect(jsonPath("$.[*].hypertension").value(hasItem(DEFAULT_HYPERTENSION)))
            .andExpect(jsonPath("$.[*].diabetes").value(hasItem(DEFAULT_DIABETES)))
            .andExpect(jsonPath("$.[*].stomachDisease").value(hasItem(DEFAULT_STOMACH_DISEASE)))
            .andExpect(jsonPath("$.[*].chronicLungDisease").value(hasItem(DEFAULT_CHRONIC_LUNG_DISEASE)))
            .andExpect(jsonPath("$.[*].asthma").value(hasItem(DEFAULT_ASTHMA)))
            .andExpect(jsonPath("$.[*].goiter").value(hasItem(DEFAULT_GOITER)))
            .andExpect(jsonPath("$.[*].hepatitis").value(hasItem(DEFAULT_HEPATITIS)))
            .andExpect(jsonPath("$.[*].congenitalHeartDisease").value(hasItem(DEFAULT_CONGENITAL_HEART_DISEASE)))
            .andExpect(jsonPath("$.[*].mentalDisorders").value(hasItem(DEFAULT_MENTAL_DISORDERS)))
            .andExpect(jsonPath("$.[*].autism").value(hasItem(DEFAULT_AUTISM)))
            .andExpect(jsonPath("$.[*].epilepsy").value(hasItem(DEFAULT_EPILEPSY)))
            .andExpect(jsonPath("$.[*].cancer").value(hasItem(DEFAULT_CANCER)))
            .andExpect(jsonPath("$.[*].tuberculosis").value(hasItem(DEFAULT_TUBERCULOSIS)))
            .andExpect(jsonPath("$.[*].otherDiseases").value(hasItem(DEFAULT_OTHER_DISEASES)))
            .andExpect(jsonPath("$.[*].contraceptiveMethod").value(hasItem(DEFAULT_CONTRACEPTIVE_METHOD)))
            .andExpect(jsonPath("$.[*].lastPregnancy").value(hasItem(DEFAULT_LAST_PREGNANCY)))
            .andExpect(jsonPath("$.[*].numberOfPregnancies").value(hasItem(DEFAULT_NUMBER_OF_PREGNANCIES)))
            .andExpect(jsonPath("$.[*].numberOfMiscarriages").value(hasItem(DEFAULT_NUMBER_OF_MISCARRIAGES)))
            .andExpect(jsonPath("$.[*].numberOfAbortions").value(hasItem(DEFAULT_NUMBER_OF_ABORTIONS)))
            .andExpect(jsonPath("$.[*].numberOfBirths").value(hasItem(DEFAULT_NUMBER_OF_BIRTHS)))
            .andExpect(jsonPath("$.[*].vaginalDelivery").value(hasItem(DEFAULT_VAGINAL_DELIVERY)))
            .andExpect(jsonPath("$.[*].cesareanSection").value(hasItem(DEFAULT_CESAREAN_SECTION)))
            .andExpect(jsonPath("$.[*].difficultDelivery").value(hasItem(DEFAULT_DIFFICULT_DELIVERY)))
            .andExpect(jsonPath("$.[*].numberOfFullTermBirths").value(hasItem(DEFAULT_NUMBER_OF_FULL_TERM_BIRTHS)))
            .andExpect(jsonPath("$.[*].numberOfPrematureBirths").value(hasItem(DEFAULT_NUMBER_OF_PREMATURE_BIRTHS)))
            .andExpect(jsonPath("$.[*].numberOfChildrenAlive").value(hasItem(DEFAULT_NUMBER_OF_CHILDREN_ALIVE)))
            .andExpect(jsonPath("$.[*].gynecologicalDiseases").value(hasItem(DEFAULT_GYNECOLOGICAL_DISEASES)))
            .andExpect(jsonPath("$.[*].birthStatus").value(hasItem(DEFAULT_BIRTH_STATUS.toString())))
            .andExpect(jsonPath("$.[*].birthWeight").value(hasItem(DEFAULT_BIRTH_WEIGHT)))
            .andExpect(jsonPath("$.[*].birthHeight").value(hasItem(DEFAULT_BIRTH_HEIGHT)))
            .andExpect(jsonPath("$.[*].birthDefectNote").value(hasItem(DEFAULT_BIRTH_DEFECT_NOTE)))
            .andExpect(jsonPath("$.[*].otherBirthNote").value(hasItem(DEFAULT_OTHER_BIRTH_NOTE)))
            .andExpect(jsonPath("$.[*].otherHealthNote").value(hasItem(DEFAULT_OTHER_HEALTH_NOTE)));
    }

    @Test
    @Transactional
    void getAdditionalInformation() throws Exception {
        // Initialize the database
        insertedAdditionalInformation = additionalInformationRepository.saveAndFlush(additionalInformation);

        // Get the additionalInformation
        restAdditionalInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, additionalInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(additionalInformation.getId().intValue()))
            .andExpect(jsonPath("$.smoking").value(DEFAULT_SMOKING.toString()))
            .andExpect(jsonPath("$.alcoholRiskLevel").value(DEFAULT_ALCOHOL_RISK_LEVEL.toString()))
            .andExpect(jsonPath("$.alcoholGlassesPerDay").value(DEFAULT_ALCOHOL_GLASSES_PER_DAY))
            .andExpect(jsonPath("$.drugUse").value(DEFAULT_DRUG_USE.toString()))
            .andExpect(jsonPath("$.physicalActivity").value(DEFAULT_PHYSICAL_ACTIVITY.toString()))
            .andExpect(jsonPath("$.exposureFactor").value(DEFAULT_EXPOSURE_FACTOR))
            .andExpect(jsonPath("$.exposureDate").value(DEFAULT_EXPOSURE_DATE.toString()))
            .andExpect(jsonPath("$.typeToilet").value(DEFAULT_TYPE_TOILET.toString()))
            .andExpect(jsonPath("$.environmentalRisk").value(DEFAULT_ENVIRONMENTAL_RISK))
            .andExpect(jsonPath("$.cardiovascularDisease").value(DEFAULT_CARDIOVASCULAR_DISEASE))
            .andExpect(jsonPath("$.hypertension").value(DEFAULT_HYPERTENSION))
            .andExpect(jsonPath("$.diabetes").value(DEFAULT_DIABETES))
            .andExpect(jsonPath("$.stomachDisease").value(DEFAULT_STOMACH_DISEASE))
            .andExpect(jsonPath("$.chronicLungDisease").value(DEFAULT_CHRONIC_LUNG_DISEASE))
            .andExpect(jsonPath("$.asthma").value(DEFAULT_ASTHMA))
            .andExpect(jsonPath("$.goiter").value(DEFAULT_GOITER))
            .andExpect(jsonPath("$.hepatitis").value(DEFAULT_HEPATITIS))
            .andExpect(jsonPath("$.congenitalHeartDisease").value(DEFAULT_CONGENITAL_HEART_DISEASE))
            .andExpect(jsonPath("$.mentalDisorders").value(DEFAULT_MENTAL_DISORDERS))
            .andExpect(jsonPath("$.autism").value(DEFAULT_AUTISM))
            .andExpect(jsonPath("$.epilepsy").value(DEFAULT_EPILEPSY))
            .andExpect(jsonPath("$.cancer").value(DEFAULT_CANCER))
            .andExpect(jsonPath("$.tuberculosis").value(DEFAULT_TUBERCULOSIS))
            .andExpect(jsonPath("$.otherDiseases").value(DEFAULT_OTHER_DISEASES))
            .andExpect(jsonPath("$.contraceptiveMethod").value(DEFAULT_CONTRACEPTIVE_METHOD))
            .andExpect(jsonPath("$.lastPregnancy").value(DEFAULT_LAST_PREGNANCY))
            .andExpect(jsonPath("$.numberOfPregnancies").value(DEFAULT_NUMBER_OF_PREGNANCIES))
            .andExpect(jsonPath("$.numberOfMiscarriages").value(DEFAULT_NUMBER_OF_MISCARRIAGES))
            .andExpect(jsonPath("$.numberOfAbortions").value(DEFAULT_NUMBER_OF_ABORTIONS))
            .andExpect(jsonPath("$.numberOfBirths").value(DEFAULT_NUMBER_OF_BIRTHS))
            .andExpect(jsonPath("$.vaginalDelivery").value(DEFAULT_VAGINAL_DELIVERY))
            .andExpect(jsonPath("$.cesareanSection").value(DEFAULT_CESAREAN_SECTION))
            .andExpect(jsonPath("$.difficultDelivery").value(DEFAULT_DIFFICULT_DELIVERY))
            .andExpect(jsonPath("$.numberOfFullTermBirths").value(DEFAULT_NUMBER_OF_FULL_TERM_BIRTHS))
            .andExpect(jsonPath("$.numberOfPrematureBirths").value(DEFAULT_NUMBER_OF_PREMATURE_BIRTHS))
            .andExpect(jsonPath("$.numberOfChildrenAlive").value(DEFAULT_NUMBER_OF_CHILDREN_ALIVE))
            .andExpect(jsonPath("$.gynecologicalDiseases").value(DEFAULT_GYNECOLOGICAL_DISEASES))
            .andExpect(jsonPath("$.birthStatus").value(DEFAULT_BIRTH_STATUS.toString()))
            .andExpect(jsonPath("$.birthWeight").value(DEFAULT_BIRTH_WEIGHT))
            .andExpect(jsonPath("$.birthHeight").value(DEFAULT_BIRTH_HEIGHT))
            .andExpect(jsonPath("$.birthDefectNote").value(DEFAULT_BIRTH_DEFECT_NOTE))
            .andExpect(jsonPath("$.otherBirthNote").value(DEFAULT_OTHER_BIRTH_NOTE))
            .andExpect(jsonPath("$.otherHealthNote").value(DEFAULT_OTHER_HEALTH_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingAdditionalInformation() throws Exception {
        // Get the additionalInformation
        restAdditionalInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdditionalInformation() throws Exception {
        // Initialize the database
        insertedAdditionalInformation = additionalInformationRepository.saveAndFlush(additionalInformation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the additionalInformation
        AdditionalInformation updatedAdditionalInformation = additionalInformationRepository
            .findById(additionalInformation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAdditionalInformation are not directly saved in db
        em.detach(updatedAdditionalInformation);
        updatedAdditionalInformation
            .smoking(UPDATED_SMOKING)
            .alcoholRiskLevel(UPDATED_ALCOHOL_RISK_LEVEL)
            .alcoholGlassesPerDay(UPDATED_ALCOHOL_GLASSES_PER_DAY)
            .drugUse(UPDATED_DRUG_USE)
            .physicalActivity(UPDATED_PHYSICAL_ACTIVITY)
            .exposureFactor(UPDATED_EXPOSURE_FACTOR)
            .exposureDate(UPDATED_EXPOSURE_DATE)
            .typeToilet(UPDATED_TYPE_TOILET)
            .environmentalRisk(UPDATED_ENVIRONMENTAL_RISK)
            .cardiovascularDisease(UPDATED_CARDIOVASCULAR_DISEASE)
            .hypertension(UPDATED_HYPERTENSION)
            .diabetes(UPDATED_DIABETES)
            .stomachDisease(UPDATED_STOMACH_DISEASE)
            .chronicLungDisease(UPDATED_CHRONIC_LUNG_DISEASE)
            .asthma(UPDATED_ASTHMA)
            .goiter(UPDATED_GOITER)
            .hepatitis(UPDATED_HEPATITIS)
            .congenitalHeartDisease(UPDATED_CONGENITAL_HEART_DISEASE)
            .mentalDisorders(UPDATED_MENTAL_DISORDERS)
            .autism(UPDATED_AUTISM)
            .epilepsy(UPDATED_EPILEPSY)
            .cancer(UPDATED_CANCER)
            .tuberculosis(UPDATED_TUBERCULOSIS)
            .otherDiseases(UPDATED_OTHER_DISEASES)
            .contraceptiveMethod(UPDATED_CONTRACEPTIVE_METHOD)
            .lastPregnancy(UPDATED_LAST_PREGNANCY)
            .numberOfPregnancies(UPDATED_NUMBER_OF_PREGNANCIES)
            .numberOfMiscarriages(UPDATED_NUMBER_OF_MISCARRIAGES)
            .numberOfAbortions(UPDATED_NUMBER_OF_ABORTIONS)
            .numberOfBirths(UPDATED_NUMBER_OF_BIRTHS)
            .vaginalDelivery(UPDATED_VAGINAL_DELIVERY)
            .cesareanSection(UPDATED_CESAREAN_SECTION)
            .difficultDelivery(UPDATED_DIFFICULT_DELIVERY)
            .numberOfFullTermBirths(UPDATED_NUMBER_OF_FULL_TERM_BIRTHS)
            .numberOfPrematureBirths(UPDATED_NUMBER_OF_PREMATURE_BIRTHS)
            .numberOfChildrenAlive(UPDATED_NUMBER_OF_CHILDREN_ALIVE)
            .gynecologicalDiseases(UPDATED_GYNECOLOGICAL_DISEASES)
            .birthStatus(UPDATED_BIRTH_STATUS)
            .birthWeight(UPDATED_BIRTH_WEIGHT)
            .birthHeight(UPDATED_BIRTH_HEIGHT)
            .birthDefectNote(UPDATED_BIRTH_DEFECT_NOTE)
            .otherBirthNote(UPDATED_OTHER_BIRTH_NOTE)
            .otherHealthNote(UPDATED_OTHER_HEALTH_NOTE);
        AdditionalInformationDTO additionalInformationDTO = additionalInformationMapper.toDto(updatedAdditionalInformation);

        restAdditionalInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, additionalInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(additionalInformationDTO))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalInformation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdditionalInformationToMatchAllProperties(updatedAdditionalInformation);
    }

    @Test
    @Transactional
    void putNonExistingAdditionalInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalInformation.setId(longCount.incrementAndGet());

        // Create the AdditionalInformation
        AdditionalInformationDTO additionalInformationDTO = additionalInformationMapper.toDto(additionalInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdditionalInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, additionalInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(additionalInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalInformation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdditionalInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalInformation.setId(longCount.incrementAndGet());

        // Create the AdditionalInformation
        AdditionalInformationDTO additionalInformationDTO = additionalInformationMapper.toDto(additionalInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(additionalInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalInformation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdditionalInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalInformation.setId(longCount.incrementAndGet());

        // Create the AdditionalInformation
        AdditionalInformationDTO additionalInformationDTO = additionalInformationMapper.toDto(additionalInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalInformationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalInformationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdditionalInformation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdditionalInformationWithPatch() throws Exception {
        // Initialize the database
        insertedAdditionalInformation = additionalInformationRepository.saveAndFlush(additionalInformation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the additionalInformation using partial update
        AdditionalInformation partialUpdatedAdditionalInformation = new AdditionalInformation();
        partialUpdatedAdditionalInformation.setId(additionalInformation.getId());

        partialUpdatedAdditionalInformation
            .smoking(UPDATED_SMOKING)
            .alcoholRiskLevel(UPDATED_ALCOHOL_RISK_LEVEL)
            .alcoholGlassesPerDay(UPDATED_ALCOHOL_GLASSES_PER_DAY)
            .physicalActivity(UPDATED_PHYSICAL_ACTIVITY)
            .exposureFactor(UPDATED_EXPOSURE_FACTOR)
            .environmentalRisk(UPDATED_ENVIRONMENTAL_RISK)
            .diabetes(UPDATED_DIABETES)
            .goiter(UPDATED_GOITER)
            .mentalDisorders(UPDATED_MENTAL_DISORDERS)
            .autism(UPDATED_AUTISM)
            .epilepsy(UPDATED_EPILEPSY)
            .cancer(UPDATED_CANCER)
            .otherDiseases(UPDATED_OTHER_DISEASES)
            .lastPregnancy(UPDATED_LAST_PREGNANCY)
            .numberOfAbortions(UPDATED_NUMBER_OF_ABORTIONS)
            .difficultDelivery(UPDATED_DIFFICULT_DELIVERY)
            .numberOfPrematureBirths(UPDATED_NUMBER_OF_PREMATURE_BIRTHS)
            .numberOfChildrenAlive(UPDATED_NUMBER_OF_CHILDREN_ALIVE)
            .birthWeight(UPDATED_BIRTH_WEIGHT)
            .birthHeight(UPDATED_BIRTH_HEIGHT)
            .birthDefectNote(UPDATED_BIRTH_DEFECT_NOTE)
            .otherHealthNote(UPDATED_OTHER_HEALTH_NOTE);

        restAdditionalInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdditionalInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdditionalInformation))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalInformation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdditionalInformationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdditionalInformation, additionalInformation),
            getPersistedAdditionalInformation(additionalInformation)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdditionalInformationWithPatch() throws Exception {
        // Initialize the database
        insertedAdditionalInformation = additionalInformationRepository.saveAndFlush(additionalInformation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the additionalInformation using partial update
        AdditionalInformation partialUpdatedAdditionalInformation = new AdditionalInformation();
        partialUpdatedAdditionalInformation.setId(additionalInformation.getId());

        partialUpdatedAdditionalInformation
            .smoking(UPDATED_SMOKING)
            .alcoholRiskLevel(UPDATED_ALCOHOL_RISK_LEVEL)
            .alcoholGlassesPerDay(UPDATED_ALCOHOL_GLASSES_PER_DAY)
            .drugUse(UPDATED_DRUG_USE)
            .physicalActivity(UPDATED_PHYSICAL_ACTIVITY)
            .exposureFactor(UPDATED_EXPOSURE_FACTOR)
            .exposureDate(UPDATED_EXPOSURE_DATE)
            .typeToilet(UPDATED_TYPE_TOILET)
            .environmentalRisk(UPDATED_ENVIRONMENTAL_RISK)
            .cardiovascularDisease(UPDATED_CARDIOVASCULAR_DISEASE)
            .hypertension(UPDATED_HYPERTENSION)
            .diabetes(UPDATED_DIABETES)
            .stomachDisease(UPDATED_STOMACH_DISEASE)
            .chronicLungDisease(UPDATED_CHRONIC_LUNG_DISEASE)
            .asthma(UPDATED_ASTHMA)
            .goiter(UPDATED_GOITER)
            .hepatitis(UPDATED_HEPATITIS)
            .congenitalHeartDisease(UPDATED_CONGENITAL_HEART_DISEASE)
            .mentalDisorders(UPDATED_MENTAL_DISORDERS)
            .autism(UPDATED_AUTISM)
            .epilepsy(UPDATED_EPILEPSY)
            .cancer(UPDATED_CANCER)
            .tuberculosis(UPDATED_TUBERCULOSIS)
            .otherDiseases(UPDATED_OTHER_DISEASES)
            .contraceptiveMethod(UPDATED_CONTRACEPTIVE_METHOD)
            .lastPregnancy(UPDATED_LAST_PREGNANCY)
            .numberOfPregnancies(UPDATED_NUMBER_OF_PREGNANCIES)
            .numberOfMiscarriages(UPDATED_NUMBER_OF_MISCARRIAGES)
            .numberOfAbortions(UPDATED_NUMBER_OF_ABORTIONS)
            .numberOfBirths(UPDATED_NUMBER_OF_BIRTHS)
            .vaginalDelivery(UPDATED_VAGINAL_DELIVERY)
            .cesareanSection(UPDATED_CESAREAN_SECTION)
            .difficultDelivery(UPDATED_DIFFICULT_DELIVERY)
            .numberOfFullTermBirths(UPDATED_NUMBER_OF_FULL_TERM_BIRTHS)
            .numberOfPrematureBirths(UPDATED_NUMBER_OF_PREMATURE_BIRTHS)
            .numberOfChildrenAlive(UPDATED_NUMBER_OF_CHILDREN_ALIVE)
            .gynecologicalDiseases(UPDATED_GYNECOLOGICAL_DISEASES)
            .birthStatus(UPDATED_BIRTH_STATUS)
            .birthWeight(UPDATED_BIRTH_WEIGHT)
            .birthHeight(UPDATED_BIRTH_HEIGHT)
            .birthDefectNote(UPDATED_BIRTH_DEFECT_NOTE)
            .otherBirthNote(UPDATED_OTHER_BIRTH_NOTE)
            .otherHealthNote(UPDATED_OTHER_HEALTH_NOTE);

        restAdditionalInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdditionalInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdditionalInformation))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalInformation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdditionalInformationUpdatableFieldsEquals(
            partialUpdatedAdditionalInformation,
            getPersistedAdditionalInformation(partialUpdatedAdditionalInformation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAdditionalInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalInformation.setId(longCount.incrementAndGet());

        // Create the AdditionalInformation
        AdditionalInformationDTO additionalInformationDTO = additionalInformationMapper.toDto(additionalInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdditionalInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, additionalInformationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(additionalInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalInformation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdditionalInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalInformation.setId(longCount.incrementAndGet());

        // Create the AdditionalInformation
        AdditionalInformationDTO additionalInformationDTO = additionalInformationMapper.toDto(additionalInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(additionalInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalInformation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdditionalInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalInformation.setId(longCount.incrementAndGet());

        // Create the AdditionalInformation
        AdditionalInformationDTO additionalInformationDTO = additionalInformationMapper.toDto(additionalInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalInformationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(additionalInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdditionalInformation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdditionalInformation() throws Exception {
        // Initialize the database
        insertedAdditionalInformation = additionalInformationRepository.saveAndFlush(additionalInformation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the additionalInformation
        restAdditionalInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, additionalInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return additionalInformationRepository.count();
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

    protected AdditionalInformation getPersistedAdditionalInformation(AdditionalInformation additionalInformation) {
        return additionalInformationRepository.findById(additionalInformation.getId()).orElseThrow();
    }

    protected void assertPersistedAdditionalInformationToMatchAllProperties(AdditionalInformation expectedAdditionalInformation) {
        assertAdditionalInformationAllPropertiesEquals(
            expectedAdditionalInformation,
            getPersistedAdditionalInformation(expectedAdditionalInformation)
        );
    }

    protected void assertPersistedAdditionalInformationToMatchUpdatableProperties(AdditionalInformation expectedAdditionalInformation) {
        assertAdditionalInformationAllUpdatablePropertiesEquals(
            expectedAdditionalInformation,
            getPersistedAdditionalInformation(expectedAdditionalInformation)
        );
    }
}
