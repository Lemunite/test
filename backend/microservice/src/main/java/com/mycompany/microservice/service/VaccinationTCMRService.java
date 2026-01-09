package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.VaccinationTCMR;
import com.mycompany.microservice.repository.VaccinationTCMRRepository;
import com.mycompany.microservice.service.dto.VaccinationTCMRDTO;
import com.mycompany.microservice.service.mapper.VaccinationTCMRMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.VaccinationTCMR}.
 */
@Service
@Transactional
public class VaccinationTCMRService {

    private static final Logger LOG = LoggerFactory.getLogger(VaccinationTCMRService.class);

    private final VaccinationTCMRRepository vaccinationTCMRRepository;

    private final VaccinationTCMRMapper vaccinationTCMRMapper;

    public VaccinationTCMRService(VaccinationTCMRRepository vaccinationTCMRRepository, VaccinationTCMRMapper vaccinationTCMRMapper) {
        this.vaccinationTCMRRepository = vaccinationTCMRRepository;
        this.vaccinationTCMRMapper = vaccinationTCMRMapper;
    }

    /**
     * Save a vaccinationTCMR.
     *
     * @param vaccinationTCMRDTO the entity to save.
     * @return the persisted entity.
     */
    public VaccinationTCMRDTO save(VaccinationTCMRDTO vaccinationTCMRDTO) {
        LOG.debug("Request to save VaccinationTCMR : {}", vaccinationTCMRDTO);
        VaccinationTCMR vaccinationTCMR = vaccinationTCMRMapper.toEntity(vaccinationTCMRDTO);
        vaccinationTCMR = vaccinationTCMRRepository.save(vaccinationTCMR);
        return vaccinationTCMRMapper.toDto(vaccinationTCMR);
    }

    /**
     * Update a vaccinationTCMR.
     *
     * @param vaccinationTCMRDTO the entity to save.
     * @return the persisted entity.
     */
    public VaccinationTCMRDTO update(VaccinationTCMRDTO vaccinationTCMRDTO) {
        LOG.debug("Request to update VaccinationTCMR : {}", vaccinationTCMRDTO);
        VaccinationTCMR vaccinationTCMR = vaccinationTCMRMapper.toEntity(vaccinationTCMRDTO);
        vaccinationTCMR = vaccinationTCMRRepository.save(vaccinationTCMR);
        return vaccinationTCMRMapper.toDto(vaccinationTCMR);
    }

    /**
     * Partially update a vaccinationTCMR.
     *
     * @param vaccinationTCMRDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VaccinationTCMRDTO> partialUpdate(VaccinationTCMRDTO vaccinationTCMRDTO) {
        LOG.debug("Request to partially update VaccinationTCMR : {}", vaccinationTCMRDTO);

        return vaccinationTCMRRepository
            .findById(vaccinationTCMRDTO.getId())
            .map(existingVaccinationTCMR -> {
                vaccinationTCMRMapper.partialUpdate(existingVaccinationTCMR, vaccinationTCMRDTO);

                return existingVaccinationTCMR;
            })
            .map(vaccinationTCMRRepository::save)
            .map(vaccinationTCMRMapper::toDto);
    }

    /**
     * Get all the vaccinationTCMRS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VaccinationTCMRDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all VaccinationTCMRS");
        return vaccinationTCMRRepository.findAll(pageable).map(vaccinationTCMRMapper::toDto);
    }

    /**
     * Get one vaccinationTCMR by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VaccinationTCMRDTO> findOne(Long id) {
        LOG.debug("Request to get VaccinationTCMR : {}", id);
        return vaccinationTCMRRepository.findById(id).map(vaccinationTCMRMapper::toDto);
    }

    /**
     * Delete the vaccinationTCMR by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete VaccinationTCMR : {}", id);
        vaccinationTCMRRepository.deleteById(id);
    }
}
