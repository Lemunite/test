package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.FamilyDisease;
import com.mycompany.microservice.repository.FamilyDiseaseRepository;
import com.mycompany.microservice.service.dto.FamilyDiseaseDTO;
import com.mycompany.microservice.service.mapper.FamilyDiseaseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.FamilyDisease}.
 */
@Service
@Transactional
public class FamilyDiseaseService {

    private static final Logger LOG = LoggerFactory.getLogger(FamilyDiseaseService.class);

    private final FamilyDiseaseRepository familyDiseaseRepository;

    private final FamilyDiseaseMapper familyDiseaseMapper;

    public FamilyDiseaseService(FamilyDiseaseRepository familyDiseaseRepository, FamilyDiseaseMapper familyDiseaseMapper) {
        this.familyDiseaseRepository = familyDiseaseRepository;
        this.familyDiseaseMapper = familyDiseaseMapper;
    }

    /**
     * Save a familyDisease.
     *
     * @param familyDiseaseDTO the entity to save.
     * @return the persisted entity.
     */
    public FamilyDiseaseDTO save(FamilyDiseaseDTO familyDiseaseDTO) {
        LOG.debug("Request to save FamilyDisease : {}", familyDiseaseDTO);
        FamilyDisease familyDisease = familyDiseaseMapper.toEntity(familyDiseaseDTO);
        familyDisease = familyDiseaseRepository.save(familyDisease);
        return familyDiseaseMapper.toDto(familyDisease);
    }

    /**
     * Update a familyDisease.
     *
     * @param familyDiseaseDTO the entity to save.
     * @return the persisted entity.
     */
    public FamilyDiseaseDTO update(FamilyDiseaseDTO familyDiseaseDTO) {
        LOG.debug("Request to update FamilyDisease : {}", familyDiseaseDTO);
        FamilyDisease familyDisease = familyDiseaseMapper.toEntity(familyDiseaseDTO);
        familyDisease = familyDiseaseRepository.save(familyDisease);
        return familyDiseaseMapper.toDto(familyDisease);
    }

    /**
     * Partially update a familyDisease.
     *
     * @param familyDiseaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FamilyDiseaseDTO> partialUpdate(FamilyDiseaseDTO familyDiseaseDTO) {
        LOG.debug("Request to partially update FamilyDisease : {}", familyDiseaseDTO);

        return familyDiseaseRepository
            .findById(familyDiseaseDTO.getId())
            .map(existingFamilyDisease -> {
                familyDiseaseMapper.partialUpdate(existingFamilyDisease, familyDiseaseDTO);

                return existingFamilyDisease;
            })
            .map(familyDiseaseRepository::save)
            .map(familyDiseaseMapper::toDto);
    }

    /**
     * Get all the familyDiseases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FamilyDiseaseDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all FamilyDiseases");
        return familyDiseaseRepository.findAll(pageable).map(familyDiseaseMapper::toDto);
    }

    /**
     * Get one familyDisease by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FamilyDiseaseDTO> findOne(Long id) {
        LOG.debug("Request to get FamilyDisease : {}", id);
        return familyDiseaseRepository.findById(id).map(familyDiseaseMapper::toDto);
    }

    /**
     * Delete the familyDisease by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FamilyDisease : {}", id);
        familyDiseaseRepository.deleteById(id);
    }
}
