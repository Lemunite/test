package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.Vaccine;
import com.mycompany.microservice.repository.VaccineRepository;
import com.mycompany.microservice.service.dto.VaccineDTO;
import com.mycompany.microservice.service.mapper.VaccineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.Vaccine}.
 */
@Service
@Transactional
public class VaccineService {

    private static final Logger LOG = LoggerFactory.getLogger(VaccineService.class);

    private final VaccineRepository vaccineRepository;

    private final VaccineMapper vaccineMapper;

    public VaccineService(VaccineRepository vaccineRepository, VaccineMapper vaccineMapper) {
        this.vaccineRepository = vaccineRepository;
        this.vaccineMapper = vaccineMapper;
    }

    /**
     * Save a vaccine.
     *
     * @param vaccineDTO the entity to save.
     * @return the persisted entity.
     */
    public VaccineDTO save(VaccineDTO vaccineDTO) {
        LOG.debug("Request to save Vaccine : {}", vaccineDTO);
        Vaccine vaccine = vaccineMapper.toEntity(vaccineDTO);
        vaccine = vaccineRepository.save(vaccine);
        return vaccineMapper.toDto(vaccine);
    }

    /**
     * Update a vaccine.
     *
     * @param vaccineDTO the entity to save.
     * @return the persisted entity.
     */
    public VaccineDTO update(VaccineDTO vaccineDTO) {
        LOG.debug("Request to update Vaccine : {}", vaccineDTO);
        Vaccine vaccine = vaccineMapper.toEntity(vaccineDTO);
        vaccine = vaccineRepository.save(vaccine);
        return vaccineMapper.toDto(vaccine);
    }

    /**
     * Partially update a vaccine.
     *
     * @param vaccineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VaccineDTO> partialUpdate(VaccineDTO vaccineDTO) {
        LOG.debug("Request to partially update Vaccine : {}", vaccineDTO);

        return vaccineRepository
            .findById(vaccineDTO.getId())
            .map(existingVaccine -> {
                vaccineMapper.partialUpdate(existingVaccine, vaccineDTO);

                return existingVaccine;
            })
            .map(vaccineRepository::save)
            .map(vaccineMapper::toDto);
    }

    /**
     * Get all the vaccines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VaccineDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Vaccines");
        return vaccineRepository.findAll(pageable).map(vaccineMapper::toDto);
    }

    /**
     * Get one vaccine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VaccineDTO> findOne(Long id) {
        LOG.debug("Request to get Vaccine : {}", id);
        return vaccineRepository.findById(id).map(vaccineMapper::toDto);
    }

    /**
     * Delete the vaccine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vaccine : {}", id);
        vaccineRepository.deleteById(id);
    }
}
