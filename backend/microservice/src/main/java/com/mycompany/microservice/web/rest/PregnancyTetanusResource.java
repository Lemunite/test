package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.PregnancyTetanusRepository;
import com.mycompany.microservice.service.PregnancyTetanusService;
import com.mycompany.microservice.service.dto.PregnancyTetanusDTO;
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
 * REST controller for managing {@link com.mycompany.microservice.domain.PregnancyTetanus}.
 */
@RestController
@RequestMapping("/api/pregnancy-tetanuses")
public class PregnancyTetanusResource {

    private static final Logger LOG = LoggerFactory.getLogger(PregnancyTetanusResource.class);

    private static final String ENTITY_NAME = "microservicePregnancyTetanus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PregnancyTetanusService pregnancyTetanusService;

    private final PregnancyTetanusRepository pregnancyTetanusRepository;

    public PregnancyTetanusResource(
        PregnancyTetanusService pregnancyTetanusService,
        PregnancyTetanusRepository pregnancyTetanusRepository
    ) {
        this.pregnancyTetanusService = pregnancyTetanusService;
        this.pregnancyTetanusRepository = pregnancyTetanusRepository;
    }

    /**
     * {@code POST  /pregnancy-tetanuses} : Create a new pregnancyTetanus.
     *
     * @param pregnancyTetanusDTO the pregnancyTetanusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pregnancyTetanusDTO, or with status {@code 400 (Bad Request)} if the pregnancyTetanus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PregnancyTetanusDTO> createPregnancyTetanus(@Valid @RequestBody PregnancyTetanusDTO pregnancyTetanusDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PregnancyTetanus : {}", pregnancyTetanusDTO);
        if (pregnancyTetanusDTO.getId() != null) {
            throw new BadRequestAlertException("A new pregnancyTetanus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pregnancyTetanusDTO = pregnancyTetanusService.save(pregnancyTetanusDTO);
        return ResponseEntity.created(new URI("/api/pregnancy-tetanuses/" + pregnancyTetanusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pregnancyTetanusDTO.getId().toString()))
            .body(pregnancyTetanusDTO);
    }

    /**
     * {@code PUT  /pregnancy-tetanuses/:id} : Updates an existing pregnancyTetanus.
     *
     * @param id the id of the pregnancyTetanusDTO to save.
     * @param pregnancyTetanusDTO the pregnancyTetanusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pregnancyTetanusDTO,
     * or with status {@code 400 (Bad Request)} if the pregnancyTetanusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pregnancyTetanusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PregnancyTetanusDTO> updatePregnancyTetanus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PregnancyTetanusDTO pregnancyTetanusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PregnancyTetanus : {}, {}", id, pregnancyTetanusDTO);
        if (pregnancyTetanusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pregnancyTetanusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pregnancyTetanusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pregnancyTetanusDTO = pregnancyTetanusService.update(pregnancyTetanusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pregnancyTetanusDTO.getId().toString()))
            .body(pregnancyTetanusDTO);
    }

    /**
     * {@code PATCH  /pregnancy-tetanuses/:id} : Partial updates given fields of an existing pregnancyTetanus, field will ignore if it is null
     *
     * @param id the id of the pregnancyTetanusDTO to save.
     * @param pregnancyTetanusDTO the pregnancyTetanusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pregnancyTetanusDTO,
     * or with status {@code 400 (Bad Request)} if the pregnancyTetanusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pregnancyTetanusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pregnancyTetanusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PregnancyTetanusDTO> partialUpdatePregnancyTetanus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PregnancyTetanusDTO pregnancyTetanusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PregnancyTetanus partially : {}, {}", id, pregnancyTetanusDTO);
        if (pregnancyTetanusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pregnancyTetanusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pregnancyTetanusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PregnancyTetanusDTO> result = pregnancyTetanusService.partialUpdate(pregnancyTetanusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pregnancyTetanusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pregnancy-tetanuses} : get all the pregnancyTetanuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pregnancyTetanuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PregnancyTetanusDTO>> getAllPregnancyTetanuses(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PregnancyTetanuses");
        Page<PregnancyTetanusDTO> page = pregnancyTetanusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pregnancy-tetanuses/:id} : get the "id" pregnancyTetanus.
     *
     * @param id the id of the pregnancyTetanusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pregnancyTetanusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PregnancyTetanusDTO> getPregnancyTetanus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PregnancyTetanus : {}", id);
        Optional<PregnancyTetanusDTO> pregnancyTetanusDTO = pregnancyTetanusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pregnancyTetanusDTO);
    }

    /**
     * {@code DELETE  /pregnancy-tetanuses/:id} : delete the "id" pregnancyTetanus.
     *
     * @param id the id of the pregnancyTetanusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePregnancyTetanus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PregnancyTetanus : {}", id);
        pregnancyTetanusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
