package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.OrganExaminationAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.OrganExamination;
import com.mycompany.microservice.repository.OrganExaminationRepository;
import com.mycompany.microservice.service.dto.OrganExaminationDTO;
import com.mycompany.microservice.service.mapper.OrganExaminationMapper;
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
 * Integration tests for the {@link OrganExaminationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganExaminationResourceIT {

    private static final String DEFAULT_CARDIOVASCULAR = "AAAAAAAAAA";
    private static final String UPDATED_CARDIOVASCULAR = "BBBBBBBBBB";

    private static final String DEFAULT_RESPIRATORY = "AAAAAAAAAA";
    private static final String UPDATED_RESPIRATORY = "BBBBBBBBBB";

    private static final String DEFAULT_DIGESTIVE = "AAAAAAAAAA";
    private static final String UPDATED_DIGESTIVE = "BBBBBBBBBB";

    private static final String DEFAULT_URINARY = "AAAAAAAAAA";
    private static final String UPDATED_URINARY = "BBBBBBBBBB";

    private static final String DEFAULT_MUSCULOSKELETAL = "AAAAAAAAAA";
    private static final String UPDATED_MUSCULOSKELETAL = "BBBBBBBBBB";

    private static final String DEFAULT_ENDOCRINE = "AAAAAAAAAA";
    private static final String UPDATED_ENDOCRINE = "BBBBBBBBBB";

    private static final String DEFAULT_NEUROLOGICAL = "AAAAAAAAAA";
    private static final String UPDATED_NEUROLOGICAL = "BBBBBBBBBB";

    private static final String DEFAULT_PSYCHIATRIC = "AAAAAAAAAA";
    private static final String UPDATED_PSYCHIATRIC = "BBBBBBBBBB";

    private static final String DEFAULT_SURGERY = "AAAAAAAAAA";
    private static final String UPDATED_SURGERY = "BBBBBBBBBB";

    private static final String DEFAULT_OBSTETRICS_AND_GYNECOLOGY = "AAAAAAAAAA";
    private static final String UPDATED_OBSTETRICS_AND_GYNECOLOGY = "BBBBBBBBBB";

    private static final String DEFAULT_OTOLARYNGOLOGY = "AAAAAAAAAA";
    private static final String UPDATED_OTOLARYNGOLOGY = "BBBBBBBBBB";

    private static final String DEFAULT_DENTISTRY_AND_MAXILLOFACIAL_SURGERY = "AAAAAAAAAA";
    private static final String UPDATED_DENTISTRY_AND_MAXILLOFACIAL_SURGERY = "BBBBBBBBBB";

    private static final String DEFAULT_EYE = "AAAAAAAAAA";
    private static final String UPDATED_EYE = "BBBBBBBBBB";

    private static final String DEFAULT_DERMATOLOGY = "AAAAAAAAAA";
    private static final String UPDATED_DERMATOLOGY = "BBBBBBBBBB";

    private static final String DEFAULT_NUTRITION = "AAAAAAAAAA";
    private static final String UPDATED_NUTRITION = "BBBBBBBBBB";

    private static final String DEFAULT_EXERCISE = "AAAAAAAAAA";
    private static final String UPDATED_EXERCISE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER = "AAAAAAAAAA";
    private static final String UPDATED_OTHER = "BBBBBBBBBB";

    private static final String DEFAULT_DEVELOPMENT_ASSESSMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEVELOPMENT_ASSESSMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organ-examinations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrganExaminationRepository organExaminationRepository;

    @Autowired
    private OrganExaminationMapper organExaminationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganExaminationMockMvc;

    private OrganExamination organExamination;

    private OrganExamination insertedOrganExamination;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganExamination createEntity() {
        return new OrganExamination()
            .cardiovascular(DEFAULT_CARDIOVASCULAR)
            .respiratory(DEFAULT_RESPIRATORY)
            .digestive(DEFAULT_DIGESTIVE)
            .urinary(DEFAULT_URINARY)
            .musculoskeletal(DEFAULT_MUSCULOSKELETAL)
            .endocrine(DEFAULT_ENDOCRINE)
            .neurological(DEFAULT_NEUROLOGICAL)
            .psychiatric(DEFAULT_PSYCHIATRIC)
            .surgery(DEFAULT_SURGERY)
            .obstetricsAndGynecology(DEFAULT_OBSTETRICS_AND_GYNECOLOGY)
            .otolaryngology(DEFAULT_OTOLARYNGOLOGY)
            .dentistryAndMaxillofacialSurgery(DEFAULT_DENTISTRY_AND_MAXILLOFACIAL_SURGERY)
            .eye(DEFAULT_EYE)
            .dermatology(DEFAULT_DERMATOLOGY)
            .nutrition(DEFAULT_NUTRITION)
            .exercise(DEFAULT_EXERCISE)
            .other(DEFAULT_OTHER)
            .developmentAssessment(DEFAULT_DEVELOPMENT_ASSESSMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganExamination createUpdatedEntity() {
        return new OrganExamination()
            .cardiovascular(UPDATED_CARDIOVASCULAR)
            .respiratory(UPDATED_RESPIRATORY)
            .digestive(UPDATED_DIGESTIVE)
            .urinary(UPDATED_URINARY)
            .musculoskeletal(UPDATED_MUSCULOSKELETAL)
            .endocrine(UPDATED_ENDOCRINE)
            .neurological(UPDATED_NEUROLOGICAL)
            .psychiatric(UPDATED_PSYCHIATRIC)
            .surgery(UPDATED_SURGERY)
            .obstetricsAndGynecology(UPDATED_OBSTETRICS_AND_GYNECOLOGY)
            .otolaryngology(UPDATED_OTOLARYNGOLOGY)
            .dentistryAndMaxillofacialSurgery(UPDATED_DENTISTRY_AND_MAXILLOFACIAL_SURGERY)
            .eye(UPDATED_EYE)
            .dermatology(UPDATED_DERMATOLOGY)
            .nutrition(UPDATED_NUTRITION)
            .exercise(UPDATED_EXERCISE)
            .other(UPDATED_OTHER)
            .developmentAssessment(UPDATED_DEVELOPMENT_ASSESSMENT);
    }

    @BeforeEach
    void initTest() {
        organExamination = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOrganExamination != null) {
            organExaminationRepository.delete(insertedOrganExamination);
            insertedOrganExamination = null;
        }
    }

    @Test
    @Transactional
    void createOrganExamination() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrganExamination
        OrganExaminationDTO organExaminationDTO = organExaminationMapper.toDto(organExamination);
        var returnedOrganExaminationDTO = om.readValue(
            restOrganExaminationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organExaminationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrganExaminationDTO.class
        );

        // Validate the OrganExamination in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrganExamination = organExaminationMapper.toEntity(returnedOrganExaminationDTO);
        assertOrganExaminationUpdatableFieldsEquals(returnedOrganExamination, getPersistedOrganExamination(returnedOrganExamination));

        insertedOrganExamination = returnedOrganExamination;
    }

    @Test
    @Transactional
    void createOrganExaminationWithExistingId() throws Exception {
        // Create the OrganExamination with an existing ID
        organExamination.setId(1L);
        OrganExaminationDTO organExaminationDTO = organExaminationMapper.toDto(organExamination);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganExaminationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organExaminationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrganExamination in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrganExaminations() throws Exception {
        // Initialize the database
        insertedOrganExamination = organExaminationRepository.saveAndFlush(organExamination);

        // Get all the organExaminationList
        restOrganExaminationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organExamination.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardiovascular").value(hasItem(DEFAULT_CARDIOVASCULAR)))
            .andExpect(jsonPath("$.[*].respiratory").value(hasItem(DEFAULT_RESPIRATORY)))
            .andExpect(jsonPath("$.[*].digestive").value(hasItem(DEFAULT_DIGESTIVE)))
            .andExpect(jsonPath("$.[*].urinary").value(hasItem(DEFAULT_URINARY)))
            .andExpect(jsonPath("$.[*].musculoskeletal").value(hasItem(DEFAULT_MUSCULOSKELETAL)))
            .andExpect(jsonPath("$.[*].endocrine").value(hasItem(DEFAULT_ENDOCRINE)))
            .andExpect(jsonPath("$.[*].neurological").value(hasItem(DEFAULT_NEUROLOGICAL)))
            .andExpect(jsonPath("$.[*].psychiatric").value(hasItem(DEFAULT_PSYCHIATRIC)))
            .andExpect(jsonPath("$.[*].surgery").value(hasItem(DEFAULT_SURGERY)))
            .andExpect(jsonPath("$.[*].obstetricsAndGynecology").value(hasItem(DEFAULT_OBSTETRICS_AND_GYNECOLOGY)))
            .andExpect(jsonPath("$.[*].otolaryngology").value(hasItem(DEFAULT_OTOLARYNGOLOGY)))
            .andExpect(jsonPath("$.[*].dentistryAndMaxillofacialSurgery").value(hasItem(DEFAULT_DENTISTRY_AND_MAXILLOFACIAL_SURGERY)))
            .andExpect(jsonPath("$.[*].eye").value(hasItem(DEFAULT_EYE)))
            .andExpect(jsonPath("$.[*].dermatology").value(hasItem(DEFAULT_DERMATOLOGY)))
            .andExpect(jsonPath("$.[*].nutrition").value(hasItem(DEFAULT_NUTRITION)))
            .andExpect(jsonPath("$.[*].exercise").value(hasItem(DEFAULT_EXERCISE)))
            .andExpect(jsonPath("$.[*].other").value(hasItem(DEFAULT_OTHER)))
            .andExpect(jsonPath("$.[*].developmentAssessment").value(hasItem(DEFAULT_DEVELOPMENT_ASSESSMENT)));
    }

    @Test
    @Transactional
    void getOrganExamination() throws Exception {
        // Initialize the database
        insertedOrganExamination = organExaminationRepository.saveAndFlush(organExamination);

        // Get the organExamination
        restOrganExaminationMockMvc
            .perform(get(ENTITY_API_URL_ID, organExamination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organExamination.getId().intValue()))
            .andExpect(jsonPath("$.cardiovascular").value(DEFAULT_CARDIOVASCULAR))
            .andExpect(jsonPath("$.respiratory").value(DEFAULT_RESPIRATORY))
            .andExpect(jsonPath("$.digestive").value(DEFAULT_DIGESTIVE))
            .andExpect(jsonPath("$.urinary").value(DEFAULT_URINARY))
            .andExpect(jsonPath("$.musculoskeletal").value(DEFAULT_MUSCULOSKELETAL))
            .andExpect(jsonPath("$.endocrine").value(DEFAULT_ENDOCRINE))
            .andExpect(jsonPath("$.neurological").value(DEFAULT_NEUROLOGICAL))
            .andExpect(jsonPath("$.psychiatric").value(DEFAULT_PSYCHIATRIC))
            .andExpect(jsonPath("$.surgery").value(DEFAULT_SURGERY))
            .andExpect(jsonPath("$.obstetricsAndGynecology").value(DEFAULT_OBSTETRICS_AND_GYNECOLOGY))
            .andExpect(jsonPath("$.otolaryngology").value(DEFAULT_OTOLARYNGOLOGY))
            .andExpect(jsonPath("$.dentistryAndMaxillofacialSurgery").value(DEFAULT_DENTISTRY_AND_MAXILLOFACIAL_SURGERY))
            .andExpect(jsonPath("$.eye").value(DEFAULT_EYE))
            .andExpect(jsonPath("$.dermatology").value(DEFAULT_DERMATOLOGY))
            .andExpect(jsonPath("$.nutrition").value(DEFAULT_NUTRITION))
            .andExpect(jsonPath("$.exercise").value(DEFAULT_EXERCISE))
            .andExpect(jsonPath("$.other").value(DEFAULT_OTHER))
            .andExpect(jsonPath("$.developmentAssessment").value(DEFAULT_DEVELOPMENT_ASSESSMENT));
    }

    @Test
    @Transactional
    void getNonExistingOrganExamination() throws Exception {
        // Get the organExamination
        restOrganExaminationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrganExamination() throws Exception {
        // Initialize the database
        insertedOrganExamination = organExaminationRepository.saveAndFlush(organExamination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organExamination
        OrganExamination updatedOrganExamination = organExaminationRepository.findById(organExamination.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrganExamination are not directly saved in db
        em.detach(updatedOrganExamination);
        updatedOrganExamination
            .cardiovascular(UPDATED_CARDIOVASCULAR)
            .respiratory(UPDATED_RESPIRATORY)
            .digestive(UPDATED_DIGESTIVE)
            .urinary(UPDATED_URINARY)
            .musculoskeletal(UPDATED_MUSCULOSKELETAL)
            .endocrine(UPDATED_ENDOCRINE)
            .neurological(UPDATED_NEUROLOGICAL)
            .psychiatric(UPDATED_PSYCHIATRIC)
            .surgery(UPDATED_SURGERY)
            .obstetricsAndGynecology(UPDATED_OBSTETRICS_AND_GYNECOLOGY)
            .otolaryngology(UPDATED_OTOLARYNGOLOGY)
            .dentistryAndMaxillofacialSurgery(UPDATED_DENTISTRY_AND_MAXILLOFACIAL_SURGERY)
            .eye(UPDATED_EYE)
            .dermatology(UPDATED_DERMATOLOGY)
            .nutrition(UPDATED_NUTRITION)
            .exercise(UPDATED_EXERCISE)
            .other(UPDATED_OTHER)
            .developmentAssessment(UPDATED_DEVELOPMENT_ASSESSMENT);
        OrganExaminationDTO organExaminationDTO = organExaminationMapper.toDto(updatedOrganExamination);

        restOrganExaminationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organExaminationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organExaminationDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrganExamination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrganExaminationToMatchAllProperties(updatedOrganExamination);
    }

    @Test
    @Transactional
    void putNonExistingOrganExamination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organExamination.setId(longCount.incrementAndGet());

        // Create the OrganExamination
        OrganExaminationDTO organExaminationDTO = organExaminationMapper.toDto(organExamination);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganExaminationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organExaminationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organExaminationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganExamination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganExamination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organExamination.setId(longCount.incrementAndGet());

        // Create the OrganExamination
        OrganExaminationDTO organExaminationDTO = organExaminationMapper.toDto(organExamination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganExaminationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organExaminationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganExamination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganExamination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organExamination.setId(longCount.incrementAndGet());

        // Create the OrganExamination
        OrganExaminationDTO organExaminationDTO = organExaminationMapper.toDto(organExamination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganExaminationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organExaminationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganExamination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganExaminationWithPatch() throws Exception {
        // Initialize the database
        insertedOrganExamination = organExaminationRepository.saveAndFlush(organExamination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organExamination using partial update
        OrganExamination partialUpdatedOrganExamination = new OrganExamination();
        partialUpdatedOrganExamination.setId(organExamination.getId());

        partialUpdatedOrganExamination
            .cardiovascular(UPDATED_CARDIOVASCULAR)
            .respiratory(UPDATED_RESPIRATORY)
            .digestive(UPDATED_DIGESTIVE)
            .urinary(UPDATED_URINARY)
            .neurological(UPDATED_NEUROLOGICAL)
            .surgery(UPDATED_SURGERY)
            .otolaryngology(UPDATED_OTOLARYNGOLOGY)
            .eye(UPDATED_EYE)
            .dermatology(UPDATED_DERMATOLOGY)
            .other(UPDATED_OTHER)
            .developmentAssessment(UPDATED_DEVELOPMENT_ASSESSMENT);

        restOrganExaminationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganExamination.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrganExamination))
            )
            .andExpect(status().isOk());

        // Validate the OrganExamination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrganExaminationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrganExamination, organExamination),
            getPersistedOrganExamination(organExamination)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrganExaminationWithPatch() throws Exception {
        // Initialize the database
        insertedOrganExamination = organExaminationRepository.saveAndFlush(organExamination);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organExamination using partial update
        OrganExamination partialUpdatedOrganExamination = new OrganExamination();
        partialUpdatedOrganExamination.setId(organExamination.getId());

        partialUpdatedOrganExamination
            .cardiovascular(UPDATED_CARDIOVASCULAR)
            .respiratory(UPDATED_RESPIRATORY)
            .digestive(UPDATED_DIGESTIVE)
            .urinary(UPDATED_URINARY)
            .musculoskeletal(UPDATED_MUSCULOSKELETAL)
            .endocrine(UPDATED_ENDOCRINE)
            .neurological(UPDATED_NEUROLOGICAL)
            .psychiatric(UPDATED_PSYCHIATRIC)
            .surgery(UPDATED_SURGERY)
            .obstetricsAndGynecology(UPDATED_OBSTETRICS_AND_GYNECOLOGY)
            .otolaryngology(UPDATED_OTOLARYNGOLOGY)
            .dentistryAndMaxillofacialSurgery(UPDATED_DENTISTRY_AND_MAXILLOFACIAL_SURGERY)
            .eye(UPDATED_EYE)
            .dermatology(UPDATED_DERMATOLOGY)
            .nutrition(UPDATED_NUTRITION)
            .exercise(UPDATED_EXERCISE)
            .other(UPDATED_OTHER)
            .developmentAssessment(UPDATED_DEVELOPMENT_ASSESSMENT);

        restOrganExaminationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganExamination.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrganExamination))
            )
            .andExpect(status().isOk());

        // Validate the OrganExamination in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrganExaminationUpdatableFieldsEquals(
            partialUpdatedOrganExamination,
            getPersistedOrganExamination(partialUpdatedOrganExamination)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOrganExamination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organExamination.setId(longCount.incrementAndGet());

        // Create the OrganExamination
        OrganExaminationDTO organExaminationDTO = organExaminationMapper.toDto(organExamination);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganExaminationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organExaminationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(organExaminationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganExamination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganExamination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organExamination.setId(longCount.incrementAndGet());

        // Create the OrganExamination
        OrganExaminationDTO organExaminationDTO = organExaminationMapper.toDto(organExamination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganExaminationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(organExaminationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganExamination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganExamination() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organExamination.setId(longCount.incrementAndGet());

        // Create the OrganExamination
        OrganExaminationDTO organExaminationDTO = organExaminationMapper.toDto(organExamination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganExaminationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(organExaminationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganExamination in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganExamination() throws Exception {
        // Initialize the database
        insertedOrganExamination = organExaminationRepository.saveAndFlush(organExamination);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the organExamination
        restOrganExaminationMockMvc
            .perform(delete(ENTITY_API_URL_ID, organExamination.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return organExaminationRepository.count();
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

    protected OrganExamination getPersistedOrganExamination(OrganExamination organExamination) {
        return organExaminationRepository.findById(organExamination.getId()).orElseThrow();
    }

    protected void assertPersistedOrganExaminationToMatchAllProperties(OrganExamination expectedOrganExamination) {
        assertOrganExaminationAllPropertiesEquals(expectedOrganExamination, getPersistedOrganExamination(expectedOrganExamination));
    }

    protected void assertPersistedOrganExaminationToMatchUpdatableProperties(OrganExamination expectedOrganExamination) {
        assertOrganExaminationAllUpdatablePropertiesEquals(
            expectedOrganExamination,
            getPersistedOrganExamination(expectedOrganExamination)
        );
    }
}
