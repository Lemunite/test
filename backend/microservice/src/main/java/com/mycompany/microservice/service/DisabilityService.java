package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.Disability;
import com.mycompany.microservice.repository.DisabilityRepository;
import com.mycompany.microservice.service.dto.DisabilityDTO;
import com.mycompany.microservice.service.mapper.DisabilityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.Disability}.
 */
@Service
@Transactional
public class DisabilityService {

    private static final Logger LOG = LoggerFactory.getLogger(DisabilityService.class);

    private final DisabilityRepository disabilityRepository;

    private final DisabilityMapper disabilityMapper;

    public DisabilityService(DisabilityRepository disabilityRepository, DisabilityMapper disabilityMapper) {
        this.disabilityRepository = disabilityRepository;
        this.disabilityMapper = disabilityMapper;
    }

    /**
     * Save a disability.
     *
     * @param disabilityDTO the entity to save.
     * @return the persisted entity.
     */
    public DisabilityDTO save(DisabilityDTO disabilityDTO) {
        LOG.debug("Request to save Disability : {}", disabilityDTO);
        Disability disability = disabilityMapper.toEntity(disabilityDTO);
        disability = disabilityRepository.save(disability);
        return disabilityMapper.toDto(disability);
    }

    /**
     * Update a disability.
     *
     * @param disabilityDTO the entity to save.
     * @return the persisted entity.
     */
    public DisabilityDTO update(DisabilityDTO disabilityDTO) {
        LOG.debug("Request to update Disability : {}", disabilityDTO);
        Disability disability = disabilityMapper.toEntity(disabilityDTO);
        disability = disabilityRepository.save(disability);
        return disabilityMapper.toDto(disability);
    }

    /**
     * Partially update a disability.
     *
     * @param disabilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DisabilityDTO> partialUpdate(DisabilityDTO disabilityDTO) {
        LOG.debug("Request to partially update Disability : {}", disabilityDTO);

        return disabilityRepository
            .findById(disabilityDTO.getId())
            .map(existingDisability -> {
                disabilityMapper.partialUpdate(existingDisability, disabilityDTO);

                return existingDisability;
            })
            .map(disabilityRepository::save)
            .map(disabilityMapper::toDto);
    }

    /**
     * Get all the disabilities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DisabilityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Disabilities");
        return disabilityRepository.findAll(pageable).map(disabilityMapper::toDto);
    }

    /**
     * Get one disability by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DisabilityDTO> findOne(Long id) {
        LOG.debug("Request to get Disability : {}", id);
        return disabilityRepository.findById(id).map(disabilityMapper::toDto);
    }

    /**
     * Delete the disability by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Disability : {}", id);
        disabilityRepository.deleteById(id);
    }
}
