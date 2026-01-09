package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.ParaclinicalResult;
import com.mycompany.microservice.repository.ParaclinicalResultRepository;
import com.mycompany.microservice.service.dto.ParaclinicalResultDTO;
import com.mycompany.microservice.service.mapper.ParaclinicalResultMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.ParaclinicalResult}.
 */
@Service
@Transactional
public class ParaclinicalResultService {

    private static final Logger LOG = LoggerFactory.getLogger(ParaclinicalResultService.class);

    private final ParaclinicalResultRepository paraclinicalResultRepository;

    private final ParaclinicalResultMapper paraclinicalResultMapper;

    public ParaclinicalResultService(
        ParaclinicalResultRepository paraclinicalResultRepository,
        ParaclinicalResultMapper paraclinicalResultMapper
    ) {
        this.paraclinicalResultRepository = paraclinicalResultRepository;
        this.paraclinicalResultMapper = paraclinicalResultMapper;
    }

    /**
     * Save a paraclinicalResult.
     *
     * @param paraclinicalResultDTO the entity to save.
     * @return the persisted entity.
     */
    public ParaclinicalResultDTO save(ParaclinicalResultDTO paraclinicalResultDTO) {
        LOG.debug("Request to save ParaclinicalResult : {}", paraclinicalResultDTO);
        ParaclinicalResult paraclinicalResult = paraclinicalResultMapper.toEntity(paraclinicalResultDTO);
        paraclinicalResult = paraclinicalResultRepository.save(paraclinicalResult);
        return paraclinicalResultMapper.toDto(paraclinicalResult);
    }

    /**
     * Update a paraclinicalResult.
     *
     * @param paraclinicalResultDTO the entity to save.
     * @return the persisted entity.
     */
    public ParaclinicalResultDTO update(ParaclinicalResultDTO paraclinicalResultDTO) {
        LOG.debug("Request to update ParaclinicalResult : {}", paraclinicalResultDTO);
        ParaclinicalResult paraclinicalResult = paraclinicalResultMapper.toEntity(paraclinicalResultDTO);
        paraclinicalResult = paraclinicalResultRepository.save(paraclinicalResult);
        return paraclinicalResultMapper.toDto(paraclinicalResult);
    }

    /**
     * Partially update a paraclinicalResult.
     *
     * @param paraclinicalResultDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParaclinicalResultDTO> partialUpdate(ParaclinicalResultDTO paraclinicalResultDTO) {
        LOG.debug("Request to partially update ParaclinicalResult : {}", paraclinicalResultDTO);

        return paraclinicalResultRepository
            .findById(paraclinicalResultDTO.getId())
            .map(existingParaclinicalResult -> {
                paraclinicalResultMapper.partialUpdate(existingParaclinicalResult, paraclinicalResultDTO);

                return existingParaclinicalResult;
            })
            .map(paraclinicalResultRepository::save)
            .map(paraclinicalResultMapper::toDto);
    }

    /**
     * Get all the paraclinicalResults.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParaclinicalResultDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ParaclinicalResults");
        return paraclinicalResultRepository.findAll(pageable).map(paraclinicalResultMapper::toDto);
    }

    /**
     * Get one paraclinicalResult by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParaclinicalResultDTO> findOne(Long id) {
        LOG.debug("Request to get ParaclinicalResult : {}", id);
        return paraclinicalResultRepository.findById(id).map(paraclinicalResultMapper::toDto);
    }

    /**
     * Delete the paraclinicalResult by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ParaclinicalResult : {}", id);
        paraclinicalResultRepository.deleteById(id);
    }
}
