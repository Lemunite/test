package com.mycompany.microservice.service;

import com.mycompany.microservice.domain.MedicalRecord;
import com.mycompany.microservice.repository.MedicalRecordRepository;
import com.mycompany.microservice.service.dto.MedicalRecordDTO;
import com.mycompany.microservice.service.mapper.MedicalRecordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.microservice.domain.MedicalRecord}.
 */
@Service
@Transactional
public class MedicalRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalRecordService.class);

    private final MedicalRecordRepository medicalRecordRepository;

    private final MedicalRecordMapper medicalRecordMapper;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, MedicalRecordMapper medicalRecordMapper) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicalRecordMapper = medicalRecordMapper;
    }

    /**
     * Save a medicalRecord.
     *
     * @param medicalRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalRecordDTO save(MedicalRecordDTO medicalRecordDTO) {
        LOG.debug("Request to save MedicalRecord : {}", medicalRecordDTO);
        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(medicalRecordDTO);
        medicalRecord = medicalRecordRepository.save(medicalRecord);
        return medicalRecordMapper.toDto(medicalRecord);
    }

    /**
     * Update a medicalRecord.
     *
     * @param medicalRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalRecordDTO update(MedicalRecordDTO medicalRecordDTO) {
        LOG.debug("Request to update MedicalRecord : {}", medicalRecordDTO);
        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(medicalRecordDTO);
        medicalRecord = medicalRecordRepository.save(medicalRecord);
        return medicalRecordMapper.toDto(medicalRecord);
    }

    /**
     * Partially update a medicalRecord.
     *
     * @param medicalRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicalRecordDTO> partialUpdate(MedicalRecordDTO medicalRecordDTO) {
        LOG.debug("Request to partially update MedicalRecord : {}", medicalRecordDTO);

        return medicalRecordRepository
            .findById(medicalRecordDTO.getId())
            .map(existingMedicalRecord -> {
                medicalRecordMapper.partialUpdate(existingMedicalRecord, medicalRecordDTO);

                return existingMedicalRecord;
            })
            .map(medicalRecordRepository::save)
            .map(medicalRecordMapper::toDto);
    }

    /**
     * Get all the medicalRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicalRecordDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MedicalRecords");
        return medicalRecordRepository.findAll(pageable).map(medicalRecordMapper::toDto);
    }

    /**
     * Get one medicalRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicalRecordDTO> findOne(Long id) {
        LOG.debug("Request to get MedicalRecord : {}", id);
        return medicalRecordRepository.findById(id).map(medicalRecordMapper::toDto);
    }

    /**
     * Delete the medicalRecord by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MedicalRecord : {}", id);
        medicalRecordRepository.deleteById(id);
    }
}
