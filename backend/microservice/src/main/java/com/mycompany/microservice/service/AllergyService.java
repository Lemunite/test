package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.Allergy;
import com.mycompany.microservice.repository.AllergyRepository;
import com.mycompany.microservice.service.dto.AllergyDTO;
import com.mycompany.microservice.service.mapper.AllergyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.Allergy}.
 */
@Service
@Transactional
public class AllergyService {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyService.class);

    private final AllergyRepository allergyRepository;

    private final AllergyMapper allergyMapper;

    public AllergyService(AllergyRepository allergyRepository, AllergyMapper allergyMapper) {
        this.allergyRepository = allergyRepository;
        this.allergyMapper = allergyMapper;
    }

    /**
     * Save a allergy.
     *
     * @param allergyDTO the entity to save.
     * @return the persisted entity.
     */
    public AllergyDTO save(AllergyDTO allergyDTO) {
        LOG.debug("Request to save Allergy : {}", allergyDTO);
        Allergy allergy = allergyMapper.toEntity(allergyDTO);
        allergy = allergyRepository.save(allergy);
        return allergyMapper.toDto(allergy);
    }

    /**
     * Update a allergy.
     *
     * @param allergyDTO the entity to save.
     * @return the persisted entity.
     */
    public AllergyDTO update(AllergyDTO allergyDTO) {
        LOG.debug("Request to update Allergy : {}", allergyDTO);
        Allergy allergy = allergyMapper.toEntity(allergyDTO);
        allergy = allergyRepository.save(allergy);
        return allergyMapper.toDto(allergy);
    }

    /**
     * Partially update a allergy.
     *
     * @param allergyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AllergyDTO> partialUpdate(AllergyDTO allergyDTO) {
        LOG.debug("Request to partially update Allergy : {}", allergyDTO);

        return allergyRepository
            .findById(allergyDTO.getId())
            .map(existingAllergy -> {
                allergyMapper.partialUpdate(existingAllergy, allergyDTO);

                return existingAllergy;
            })
            .map(allergyRepository::save)
            .map(allergyMapper::toDto);
    }

    /**
     * Get all the allergies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AllergyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Allergies");
        return allergyRepository.findAll(pageable).map(allergyMapper::toDto);
    }

    /**
     * Get one allergy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AllergyDTO> findOne(Long id) {
        LOG.debug("Request to get Allergy : {}", id);
        return allergyRepository.findById(id).map(allergyMapper::toDto);
    }

    /**
     * Delete the allergy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Allergy : {}", id);
        allergyRepository.deleteById(id);
    }
}
