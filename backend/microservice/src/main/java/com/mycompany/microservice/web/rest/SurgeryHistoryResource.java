package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.SurgeryHistoryRepository;
import com.mycompany.microservice.service.SurgeryHistoryService;
import com.mycompany.microservice.service.dto.SurgeryHistoryDTO;
import com.mycompany.microservice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.microservice.domain.SurgeryHistory}.
 */
@RestController
@RequestMapping("/api/surgery-histories")
public class SurgeryHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(SurgeryHistoryResource.class);

    private static final String ENTITY_NAME = "microserviceSurgeryHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurgeryHistoryService surgeryHistoryService;

    private final SurgeryHistoryRepository surgeryHistoryRepository;

    public SurgeryHistoryResource(SurgeryHistoryService surgeryHistoryService, SurgeryHistoryRepository surgeryHistoryRepository) {
        this.surgeryHistoryService = surgeryHistoryService;
        this.surgeryHistoryRepository = surgeryHistoryRepository;
    }

    /**
     * {@code POST  /surgery-histories} : Create a new surgeryHistory.
     *
     * @param surgeryHistoryDTO the surgeryHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new surgeryHistoryDTO, or with status {@code 400 (Bad Request)} if the surgeryHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SurgeryHistoryDTO> createSurgeryHistory(@Valid @RequestBody SurgeryHistoryDTO surgeryHistoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SurgeryHistory : {}", surgeryHistoryDTO);
        if (surgeryHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new surgeryHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        surgeryHistoryDTO = surgeryHistoryService.save(surgeryHistoryDTO);
        return ResponseEntity.created(new URI("/api/surgery-histories/" + surgeryHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, surgeryHistoryDTO.getId().toString()))
            .body(surgeryHistoryDTO);
    }

    /**
     * {@code PUT  /surgery-histories/:id} : Updates an existing surgeryHistory.
     *
     * @param id the id of the surgeryHistoryDTO to save.
     * @param surgeryHistoryDTO the surgeryHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surgeryHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the surgeryHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the surgeryHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SurgeryHistoryDTO> updateSurgeryHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SurgeryHistoryDTO surgeryHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SurgeryHistory : {}, {}", id, surgeryHistoryDTO);
        if (surgeryHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surgeryHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surgeryHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        surgeryHistoryDTO = surgeryHistoryService.update(surgeryHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surgeryHistoryDTO.getId().toString()))
            .body(surgeryHistoryDTO);
    }

    /**
     * {@code PATCH  /surgery-histories/:id} : Partial updates given fields of an existing surgeryHistory, field will ignore if it is null
     *
     * @param id the id of the surgeryHistoryDTO to save.
     * @param surgeryHistoryDTO the surgeryHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surgeryHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the surgeryHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the surgeryHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the surgeryHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SurgeryHistoryDTO> partialUpdateSurgeryHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SurgeryHistoryDTO surgeryHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SurgeryHistory partially : {}, {}", id, surgeryHistoryDTO);
        if (surgeryHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surgeryHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surgeryHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SurgeryHistoryDTO> result = surgeryHistoryService.partialUpdate(surgeryHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surgeryHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /surgery-histories} : get all the surgeryHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surgeryHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SurgeryHistoryDTO>> getAllSurgeryHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of SurgeryHistories");
        Page<SurgeryHistoryDTO> page = surgeryHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /surgery-histories/:id} : get the "id" surgeryHistory.
     *
     * @param id the id of the surgeryHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the surgeryHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SurgeryHistoryDTO> getSurgeryHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SurgeryHistory : {}", id);
        Optional<SurgeryHistoryDTO> surgeryHistoryDTO = surgeryHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(surgeryHistoryDTO);
    }

    /**
     * {@code DELETE  /surgery-histories/:id} : delete the "id" surgeryHistory.
     *
     * @param id the id of the surgeryHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurgeryHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SurgeryHistory : {}", id);
        surgeryHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
