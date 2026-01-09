package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.VaccinationForBaby;
import com.mycompany.microservice.repository.VaccinationForBabyRepository;
import com.mycompany.microservice.service.dto.VaccinationForBabyDTO;
import com.mycompany.microservice.service.mapper.VaccinationForBabyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.VaccinationForBaby}.
 */
@Service
@Transactional
public class VaccinationForBabyService {

    private static final Logger LOG = LoggerFactory.getLogger(VaccinationForBabyService.class);

    private final VaccinationForBabyRepository vaccinationForBabyRepository;

    private final VaccinationForBabyMapper vaccinationForBabyMapper;

    public VaccinationForBabyService(
        VaccinationForBabyRepository vaccinationForBabyRepository,
        VaccinationForBabyMapper vaccinationForBabyMapper
    ) {
        this.vaccinationForBabyRepository = vaccinationForBabyRepository;
        this.vaccinationForBabyMapper = vaccinationForBabyMapper;
    }

    /**
     * Save a vaccinationForBaby.
     *
     * @param vaccinationForBabyDTO the entity to save.
     * @return the persisted entity.
     */
    public VaccinationForBabyDTO save(VaccinationForBabyDTO vaccinationForBabyDTO) {
        LOG.debug("Request to save VaccinationForBaby : {}", vaccinationForBabyDTO);
        VaccinationForBaby vaccinationForBaby = vaccinationForBabyMapper.toEntity(vaccinationForBabyDTO);
        vaccinationForBaby = vaccinationForBabyRepository.save(vaccinationForBaby);
        return vaccinationForBabyMapper.toDto(vaccinationForBaby);
    }

    /**
     * Update a vaccinationForBaby.
     *
     * @param vaccinationForBabyDTO the entity to save.
     * @return the persisted entity.
     */
    public VaccinationForBabyDTO update(VaccinationForBabyDTO vaccinationForBabyDTO) {
        LOG.debug("Request to update VaccinationForBaby : {}", vaccinationForBabyDTO);
        VaccinationForBaby vaccinationForBaby = vaccinationForBabyMapper.toEntity(vaccinationForBabyDTO);
        vaccinationForBaby = vaccinationForBabyRepository.save(vaccinationForBaby);
        return vaccinationForBabyMapper.toDto(vaccinationForBaby);
    }

    /**
     * Partially update a vaccinationForBaby.
     *
     * @param vaccinationForBabyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VaccinationForBabyDTO> partialUpdate(VaccinationForBabyDTO vaccinationForBabyDTO) {
        LOG.debug("Request to partially update VaccinationForBaby : {}", vaccinationForBabyDTO);

        return vaccinationForBabyRepository
            .findById(vaccinationForBabyDTO.getId())
            .map(existingVaccinationForBaby -> {
                vaccinationForBabyMapper.partialUpdate(existingVaccinationForBaby, vaccinationForBabyDTO);

                return existingVaccinationForBaby;
            })
            .map(vaccinationForBabyRepository::save)
            .map(vaccinationForBabyMapper::toDto);
    }

    /**
     * Get all the vaccinationForBabies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VaccinationForBabyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all VaccinationForBabies");
        return vaccinationForBabyRepository.findAll(pageable).map(vaccinationForBabyMapper::toDto);
    }

    /**
     *  Get all the vaccinationForBabies where Patient is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VaccinationForBabyDTO> findAllWherePatientIsNull() {
        LOG.debug("Request to get all vaccinationForBabies where Patient is null");
        return StreamSupport.stream(vaccinationForBabyRepository.findAll().spliterator(), false)
            .filter(vaccinationForBaby -> vaccinationForBaby.getPatient() == null)
            .map(vaccinationForBabyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one vaccinationForBaby by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VaccinationForBabyDTO> findOne(Long id) {
        LOG.debug("Request to get VaccinationForBaby : {}", id);
        return vaccinationForBabyRepository.findById(id).map(vaccinationForBabyMapper::toDto);
    }

    /**
     * Delete the vaccinationForBaby by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete VaccinationForBaby : {}", id);
        vaccinationForBabyRepository.deleteById(id);
    }
}
