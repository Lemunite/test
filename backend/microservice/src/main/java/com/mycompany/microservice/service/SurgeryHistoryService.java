package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.SurgeryHistory;
import com.mycompany.microservice.repository.SurgeryHistoryRepository;
import com.mycompany.microservice.service.dto.SurgeryHistoryDTO;
import com.mycompany.microservice.service.mapper.SurgeryHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.SurgeryHistory}.
 */
@Service
@Transactional
public class SurgeryHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(SurgeryHistoryService.class);

    private final SurgeryHistoryRepository surgeryHistoryRepository;

    private final SurgeryHistoryMapper surgeryHistoryMapper;

    public SurgeryHistoryService(SurgeryHistoryRepository surgeryHistoryRepository, SurgeryHistoryMapper surgeryHistoryMapper) {
        this.surgeryHistoryRepository = surgeryHistoryRepository;
        this.surgeryHistoryMapper = surgeryHistoryMapper;
    }

    /**
     * Save a surgeryHistory.
     *
     * @param surgeryHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public SurgeryHistoryDTO save(SurgeryHistoryDTO surgeryHistoryDTO) {
        LOG.debug("Request to save SurgeryHistory : {}", surgeryHistoryDTO);
        SurgeryHistory surgeryHistory = surgeryHistoryMapper.toEntity(surgeryHistoryDTO);
        surgeryHistory = surgeryHistoryRepository.save(surgeryHistory);
        return surgeryHistoryMapper.toDto(surgeryHistory);
    }

    /**
     * Update a surgeryHistory.
     *
     * @param surgeryHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public SurgeryHistoryDTO update(SurgeryHistoryDTO surgeryHistoryDTO) {
        LOG.debug("Request to update SurgeryHistory : {}", surgeryHistoryDTO);
        SurgeryHistory surgeryHistory = surgeryHistoryMapper.toEntity(surgeryHistoryDTO);
        surgeryHistory = surgeryHistoryRepository.save(surgeryHistory);
        return surgeryHistoryMapper.toDto(surgeryHistory);
    }

    /**
     * Partially update a surgeryHistory.
     *
     * @param surgeryHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SurgeryHistoryDTO> partialUpdate(SurgeryHistoryDTO surgeryHistoryDTO) {
        LOG.debug("Request to partially update SurgeryHistory : {}", surgeryHistoryDTO);

        return surgeryHistoryRepository
            .findById(surgeryHistoryDTO.getId())
            .map(existingSurgeryHistory -> {
                surgeryHistoryMapper.partialUpdate(existingSurgeryHistory, surgeryHistoryDTO);

                return existingSurgeryHistory;
            })
            .map(surgeryHistoryRepository::save)
            .map(surgeryHistoryMapper::toDto);
    }

    /**
     * Get all the surgeryHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SurgeryHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SurgeryHistories");
        return surgeryHistoryRepository.findAll(pageable).map(surgeryHistoryMapper::toDto);
    }

    /**
     * Get one surgeryHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SurgeryHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get SurgeryHistory : {}", id);
        return surgeryHistoryRepository.findById(id).map(surgeryHistoryMapper::toDto);
    }

    /**
     * Delete the surgeryHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SurgeryHistory : {}", id);
        surgeryHistoryRepository.deleteById(id);
    }
}
