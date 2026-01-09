package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.FamilyAllergy;
import com.mycompany.microservice.repository.FamilyAllergyRepository;
import com.mycompany.microservice.service.dto.FamilyAllergyDTO;
import com.mycompany.microservice.service.mapper.FamilyAllergyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.FamilyAllergy}.
 */
@Service
@Transactional
public class FamilyAllergyService {

    private static final Logger LOG = LoggerFactory.getLogger(FamilyAllergyService.class);

    private final FamilyAllergyRepository familyAllergyRepository;

    private final FamilyAllergyMapper familyAllergyMapper;

    public FamilyAllergyService(FamilyAllergyRepository familyAllergyRepository, FamilyAllergyMapper familyAllergyMapper) {
        this.familyAllergyRepository = familyAllergyRepository;
        this.familyAllergyMapper = familyAllergyMapper;
    }

    /**
     * Save a familyAllergy.
     *
     * @param familyAllergyDTO the entity to save.
     * @return the persisted entity.
     */
    public FamilyAllergyDTO save(FamilyAllergyDTO familyAllergyDTO) {
        LOG.debug("Request to save FamilyAllergy : {}", familyAllergyDTO);
        FamilyAllergy familyAllergy = familyAllergyMapper.toEntity(familyAllergyDTO);
        familyAllergy = familyAllergyRepository.save(familyAllergy);
        return familyAllergyMapper.toDto(familyAllergy);
    }

    /**
     * Update a familyAllergy.
     *
     * @param familyAllergyDTO the entity to save.
     * @return the persisted entity.
     */
    public FamilyAllergyDTO update(FamilyAllergyDTO familyAllergyDTO) {
        LOG.debug("Request to update FamilyAllergy : {}", familyAllergyDTO);
        FamilyAllergy familyAllergy = familyAllergyMapper.toEntity(familyAllergyDTO);
        familyAllergy = familyAllergyRepository.save(familyAllergy);
        return familyAllergyMapper.toDto(familyAllergy);
    }

    /**
     * Partially update a familyAllergy.
     *
     * @param familyAllergyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FamilyAllergyDTO> partialUpdate(FamilyAllergyDTO familyAllergyDTO) {
        LOG.debug("Request to partially update FamilyAllergy : {}", familyAllergyDTO);

        return familyAllergyRepository
            .findById(familyAllergyDTO.getId())
            .map(existingFamilyAllergy -> {
                familyAllergyMapper.partialUpdate(existingFamilyAllergy, familyAllergyDTO);

                return existingFamilyAllergy;
            })
            .map(familyAllergyRepository::save)
            .map(familyAllergyMapper::toDto);
    }

    /**
     * Get all the familyAllergies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FamilyAllergyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all FamilyAllergies");
        return familyAllergyRepository.findAll(pageable).map(familyAllergyMapper::toDto);
    }

    /**
     * Get one familyAllergy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FamilyAllergyDTO> findOne(Long id) {
        LOG.debug("Request to get FamilyAllergy : {}", id);
        return familyAllergyRepository.findById(id).map(familyAllergyMapper::toDto);
    }

    /**
     * Delete the familyAllergy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FamilyAllergy : {}", id);
        familyAllergyRepository.deleteById(id);
    }
}
