package com.mycompany.microservice.web.rest;

import static com.mycompany.microservice.domain.DisabilityAsserts.*;
import static com.mycompany.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.microservice.IntegrationTest;
import com.mycompany.microservice.domain.Disability;
import com.mycompany.microservice.domain.Patient;
import com.mycompany.microservice.domain.enumeration.DisabilityType;
import com.mycompany.microservice.repository.DisabilityRepository;
import com.mycompany.microservice.service.dto.DisabilityDTO;
import com.mycompany.microservice.service.mapper.DisabilityMapper;
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
 * Integration tests for the {@link DisabilityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisabilityResourceIT {

    private static final DisabilityType DEFAULT_TYPE = DisabilityType.HEARING;
    private static final DisabilityType UPDATED_TYPE = DisabilityType.VISION;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/disabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DisabilityRepository disabilityRepository;

    @Autowired
    private DisabilityMapper disabilityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisabilityMockMvc;

    private Disability disability;

    private Disability insertedDisability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disability createEntity(EntityManager em) {
        Disability disability = new Disability().type(DEFAULT_TYPE).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        disability.setPatient(patient);
        return disability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disability createUpdatedEntity(EntityManager em) {
        Disability updatedDisability = new Disability().type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity();
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedDisability.setPatient(patient);
        return updatedDisability;
    }

    @BeforeEach
    void initTest() {
        disability = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDisability != null) {
            disabilityRepository.delete(insertedDisability);
            insertedDisability = null;
        }
    }

    @Test
    @Transactional
    void createDisability() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);
        var returnedDisabilityDTO = om.readValue(
            restDisabilityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disabilityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DisabilityDTO.class
        );

        // Validate the Disability in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDisability = disabilityMapper.toEntity(returnedDisabilityDTO);
        assertDisabilityUpdatableFieldsEquals(returnedDisability, getPersistedDisability(returnedDisability));

        insertedDisability = returnedDisability;
    }

    @Test
    @Transactional
    void createDisabilityWithExistingId() throws Exception {
        // Create the Disability with an existing ID
        disability.setId(1L);
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disabilityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Disability in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disability.setType(null);

        // Create the Disability, which fails.
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        restDisabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disabilityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDisabilities() throws Exception {
        // Initialize the database
        insertedDisability = disabilityRepository.saveAndFlush(disability);

        // Get all the disabilityList
        restDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disability.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDisability() throws Exception {
        // Initialize the database
        insertedDisability = disabilityRepository.saveAndFlush(disability);

        // Get the disability
        restDisabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, disability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disability.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDisability() throws Exception {
        // Get the disability
        restDisabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisability() throws Exception {
        // Initialize the database
        insertedDisability = disabilityRepository.saveAndFlush(disability);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disability
        Disability updatedDisability = disabilityRepository.findById(disability.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDisability are not directly saved in db
        em.detach(updatedDisability);
        updatedDisability.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(updatedDisability);

        restDisabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disabilityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Disability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDisabilityToMatchAllProperties(updatedDisability);
    }

    @Test
    @Transactional
    void putNonExistingDisability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disability.setId(longCount.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disability.setId(longCount.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disability.setId(longCount.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disabilityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisabilityWithPatch() throws Exception {
        // Initialize the database
        insertedDisability = disabilityRepository.saveAndFlush(disability);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disability using partial update
        Disability partialUpdatedDisability = new Disability();
        partialUpdatedDisability.setId(disability.getId());

        partialUpdatedDisability.description(UPDATED_DESCRIPTION);

        restDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisability.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisability))
            )
            .andExpect(status().isOk());

        // Validate the Disability in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisabilityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDisability, disability),
            getPersistedDisability(disability)
        );
    }

    @Test
    @Transactional
    void fullUpdateDisabilityWithPatch() throws Exception {
        // Initialize the database
        insertedDisability = disabilityRepository.saveAndFlush(disability);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disability using partial update
        Disability partialUpdatedDisability = new Disability();
        partialUpdatedDisability.setId(disability.getId());

        partialUpdatedDisability.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisability.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisability))
            )
            .andExpect(status().isOk());

        // Validate the Disability in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisabilityUpdatableFieldsEquals(partialUpdatedDisability, getPersistedDisability(partialUpdatedDisability));
    }

    @Test
    @Transactional
    void patchNonExistingDisability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disability.setId(longCount.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disabilityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disability.setId(longCount.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisability() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disability.setId(longCount.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(disabilityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disability in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisability() throws Exception {
        // Initialize the database
        insertedDisability = disabilityRepository.saveAndFlush(disability);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the disability
        restDisabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, disability.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return disabilityRepository.count();
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

    protected Disability getPersistedDisability(Disability disability) {
        return disabilityRepository.findById(disability.getId()).orElseThrow();
    }

    protected void assertPersistedDisabilityToMatchAllProperties(Disability expectedDisability) {
        assertDisabilityAllPropertiesEquals(expectedDisability, getPersistedDisability(expectedDisability));
    }

    protected void assertPersistedDisabilityToMatchUpdatableProperties(Disability expectedDisability) {
        assertDisabilityAllUpdatablePropertiesEquals(expectedDisability, getPersistedDisability(expectedDisability));
    }
}
