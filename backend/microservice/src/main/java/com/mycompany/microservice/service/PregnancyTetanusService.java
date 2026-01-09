package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.PregnancyTetanus;
import com.mycompany.microservice.repository.PregnancyTetanusRepository;
import com.mycompany.microservice.service.dto.PregnancyTetanusDTO;
import com.mycompany.microservice.service.mapper.PregnancyTetanusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.PregnancyTetanus}.
 */
@Service
@Transactional
public class PregnancyTetanusService {

    private static final Logger LOG = LoggerFactory.getLogger(PregnancyTetanusService.class);

    private final PregnancyTetanusRepository pregnancyTetanusRepository;

    private final PregnancyTetanusMapper pregnancyTetanusMapper;

    public PregnancyTetanusService(PregnancyTetanusRepository pregnancyTetanusRepository, PregnancyTetanusMapper pregnancyTetanusMapper) {
        this.pregnancyTetanusRepository = pregnancyTetanusRepository;
        this.pregnancyTetanusMapper = pregnancyTetanusMapper;
    }

    /**
     * Save a pregnancyTetanus.
     *
     * @param pregnancyTetanusDTO the entity to save.
     * @return the persisted entity.
     */
    public PregnancyTetanusDTO save(PregnancyTetanusDTO pregnancyTetanusDTO) {
        LOG.debug("Request to save PregnancyTetanus : {}", pregnancyTetanusDTO);
        PregnancyTetanus pregnancyTetanus = pregnancyTetanusMapper.toEntity(pregnancyTetanusDTO);
        pregnancyTetanus = pregnancyTetanusRepository.save(pregnancyTetanus);
        return pregnancyTetanusMapper.toDto(pregnancyTetanus);
    }

    /**
     * Update a pregnancyTetanus.
     *
     * @param pregnancyTetanusDTO the entity to save.
     * @return the persisted entity.
     */
    public PregnancyTetanusDTO update(PregnancyTetanusDTO pregnancyTetanusDTO) {
        LOG.debug("Request to update PregnancyTetanus : {}", pregnancyTetanusDTO);
        PregnancyTetanus pregnancyTetanus = pregnancyTetanusMapper.toEntity(pregnancyTetanusDTO);
        pregnancyTetanus = pregnancyTetanusRepository.save(pregnancyTetanus);
        return pregnancyTetanusMapper.toDto(pregnancyTetanus);
    }

    /**
     * Partially update a pregnancyTetanus.
     *
     * @param pregnancyTetanusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PregnancyTetanusDTO> partialUpdate(PregnancyTetanusDTO pregnancyTetanusDTO) {
        LOG.debug("Request to partially update PregnancyTetanus : {}", pregnancyTetanusDTO);

        return pregnancyTetanusRepository
            .findById(pregnancyTetanusDTO.getId())
            .map(existingPregnancyTetanus -> {
                pregnancyTetanusMapper.partialUpdate(existingPregnancyTetanus, pregnancyTetanusDTO);

                return existingPregnancyTetanus;
            })
            .map(pregnancyTetanusRepository::save)
            .map(pregnancyTetanusMapper::toDto);
    }

    /**
     * Get all the pregnancyTetanuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PregnancyTetanusDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PregnancyTetanuses");
        return pregnancyTetanusRepository.findAll(pageable).map(pregnancyTetanusMapper::toDto);
    }

    /**
     * Get one pregnancyTetanus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PregnancyTetanusDTO> findOne(Long id) {
        LOG.debug("Request to get PregnancyTetanus : {}", id);
        return pregnancyTetanusRepository.findById(id).map(pregnancyTetanusMapper::toDto);
    }

    /**
     * Delete the pregnancyTetanus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PregnancyTetanus : {}", id);
        pregnancyTetanusRepository.deleteById(id);
    }
}
