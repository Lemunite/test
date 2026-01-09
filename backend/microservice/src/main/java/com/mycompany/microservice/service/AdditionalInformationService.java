package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.AdditionalInformation;
import com.mycompany.microservice.repository.AdditionalInformationRepository;
import com.mycompany.microservice.service.dto.AdditionalInformationDTO;
import com.mycompany.microservice.service.mapper.AdditionalInformationMapper;
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
 * Service Implementation for managing {@link com.mycompany.microservice.domain.AdditionalInformation}.
 */
@Service
@Transactional
public class AdditionalInformationService {

    private static final Logger LOG = LoggerFactory.getLogger(AdditionalInformationService.class);

    private final AdditionalInformationRepository additionalInformationRepository;

    private final AdditionalInformationMapper additionalInformationMapper;

    public AdditionalInformationService(
        AdditionalInformationRepository additionalInformationRepository,
        AdditionalInformationMapper additionalInformationMapper
    ) {
        this.additionalInformationRepository = additionalInformationRepository;
        this.additionalInformationMapper = additionalInformationMapper;
    }

    /**
     * Save a additionalInformation.
     *
     * @param additionalInformationDTO the entity to save.
     * @return the persisted entity.
     */
    public AdditionalInformationDTO save(AdditionalInformationDTO additionalInformationDTO) {
        LOG.debug("Request to save AdditionalInformation : {}", additionalInformationDTO);
        AdditionalInformation additionalInformation = additionalInformationMapper.toEntity(additionalInformationDTO);
        additionalInformation = additionalInformationRepository.save(additionalInformation);
        return additionalInformationMapper.toDto(additionalInformation);
    }

    /**
     * Update a additionalInformation.
     *
     * @param additionalInformationDTO the entity to save.
     * @return the persisted entity.
     */
    public AdditionalInformationDTO update(AdditionalInformationDTO additionalInformationDTO) {
        LOG.debug("Request to update AdditionalInformation : {}", additionalInformationDTO);
        AdditionalInformation additionalInformation = additionalInformationMapper.toEntity(additionalInformationDTO);
        additionalInformation = additionalInformationRepository.save(additionalInformation);
        return additionalInformationMapper.toDto(additionalInformation);
    }

    /**
     * Partially update a additionalInformation.
     *
     * @param additionalInformationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdditionalInformationDTO> partialUpdate(AdditionalInformationDTO additionalInformationDTO) {
        LOG.debug("Request to partially update AdditionalInformation : {}", additionalInformationDTO);

        return additionalInformationRepository
            .findById(additionalInformationDTO.getId())
            .map(existingAdditionalInformation -> {
                additionalInformationMapper.partialUpdate(existingAdditionalInformation, additionalInformationDTO);

                return existingAdditionalInformation;
            })
            .map(additionalInformationRepository::save)
            .map(additionalInformationMapper::toDto);
    }

    /**
     * Get all the additionalInformations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdditionalInformationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AdditionalInformations");
        return additionalInformationRepository.findAll(pageable).map(additionalInformationMapper::toDto);
    }

    /**
     *  Get all the additionalInformations where Patient is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AdditionalInformationDTO> findAllWherePatientIsNull() {
        LOG.debug("Request to get all additionalInformations where Patient is null");
        return StreamSupport.stream(additionalInformationRepository.findAll().spliterator(), false)
            .filter(additionalInformation -> additionalInformation.getPatient() == null)
            .map(additionalInformationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one additionalInformation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdditionalInformationDTO> findOne(Long id) {
        LOG.debug("Request to get AdditionalInformation : {}", id);
        return additionalInformationRepository.findById(id).map(additionalInformationMapper::toDto);
    }

    /**
     * Delete the additionalInformation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AdditionalInformation : {}", id);
        additionalInformationRepository.deleteById(id);
    }
}
