package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.OrganExamination;
import com.mycompany.microservice.repository.OrganExaminationRepository;
import com.mycompany.microservice.service.dto.OrganExaminationDTO;
import com.mycompany.microservice.service.mapper.OrganExaminationMapper;
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
 * Service Implementation for managing {@link com.mycompany.microservice.domain.OrganExamination}.
 */
@Service
@Transactional
public class OrganExaminationService {

    private static final Logger LOG = LoggerFactory.getLogger(OrganExaminationService.class);

    private final OrganExaminationRepository organExaminationRepository;

    private final OrganExaminationMapper organExaminationMapper;

    public OrganExaminationService(OrganExaminationRepository organExaminationRepository, OrganExaminationMapper organExaminationMapper) {
        this.organExaminationRepository = organExaminationRepository;
        this.organExaminationMapper = organExaminationMapper;
    }

    /**
     * Save a organExamination.
     *
     * @param organExaminationDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganExaminationDTO save(OrganExaminationDTO organExaminationDTO) {
        LOG.debug("Request to save OrganExamination : {}", organExaminationDTO);
        OrganExamination organExamination = organExaminationMapper.toEntity(organExaminationDTO);
        organExamination = organExaminationRepository.save(organExamination);
        return organExaminationMapper.toDto(organExamination);
    }

    /**
     * Update a organExamination.
     *
     * @param organExaminationDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganExaminationDTO update(OrganExaminationDTO organExaminationDTO) {
        LOG.debug("Request to update OrganExamination : {}", organExaminationDTO);
        OrganExamination organExamination = organExaminationMapper.toEntity(organExaminationDTO);
        organExamination = organExaminationRepository.save(organExamination);
        return organExaminationMapper.toDto(organExamination);
    }

    /**
     * Partially update a organExamination.
     *
     * @param organExaminationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrganExaminationDTO> partialUpdate(OrganExaminationDTO organExaminationDTO) {
        LOG.debug("Request to partially update OrganExamination : {}", organExaminationDTO);

        return organExaminationRepository
            .findById(organExaminationDTO.getId())
            .map(existingOrganExamination -> {
                organExaminationMapper.partialUpdate(existingOrganExamination, organExaminationDTO);

                return existingOrganExamination;
            })
            .map(organExaminationRepository::save)
            .map(organExaminationMapper::toDto);
    }

    /**
     * Get all the organExaminations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganExaminationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OrganExaminations");
        return organExaminationRepository.findAll(pageable).map(organExaminationMapper::toDto);
    }

    /**
     *  Get all the organExaminations where MedicalRecord is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrganExaminationDTO> findAllWhereMedicalRecordIsNull() {
        LOG.debug("Request to get all organExaminations where MedicalRecord is null");
        return StreamSupport.stream(organExaminationRepository.findAll().spliterator(), false)
            .filter(organExamination -> organExamination.getMedicalRecord() == null)
            .map(organExaminationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one organExamination by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganExaminationDTO> findOne(Long id) {
        LOG.debug("Request to get OrganExamination : {}", id);
        return organExaminationRepository.findById(id).map(organExaminationMapper::toDto);
    }

    /**
     * Delete the organExamination by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrganExamination : {}", id);
        organExaminationRepository.deleteById(id);
    }
}
