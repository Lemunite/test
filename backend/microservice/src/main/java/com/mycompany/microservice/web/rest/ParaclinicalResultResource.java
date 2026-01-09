package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.ParaclinicalResultRepository;
import com.mycompany.microservice.service.ParaclinicalResultService;
import com.mycompany.microservice.service.dto.ParaclinicalResultDTO;
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
 * REST controller for managing {@link com.mycompany.microservice.domain.ParaclinicalResult}.
 */
@RestController
@RequestMapping("/api/paraclinical-results")
public class ParaclinicalResultResource {

    private static final Logger LOG = LoggerFactory.getLogger(ParaclinicalResultResource.class);

    private static final String ENTITY_NAME = "microserviceParaclinicalResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParaclinicalResultService paraclinicalResultService;

    private final ParaclinicalResultRepository paraclinicalResultRepository;

    public ParaclinicalResultResource(
        ParaclinicalResultService paraclinicalResultService,
        ParaclinicalResultRepository paraclinicalResultRepository
    ) {
        this.paraclinicalResultService = paraclinicalResultService;
        this.paraclinicalResultRepository = paraclinicalResultRepository;
    }

    /**
     * {@code POST  /paraclinical-results} : Create a new paraclinicalResult.
     *
     * @param paraclinicalResultDTO the paraclinicalResultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paraclinicalResultDTO, or with status {@code 400 (Bad Request)} if the paraclinicalResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ParaclinicalResultDTO> createParaclinicalResult(@Valid @RequestBody ParaclinicalResultDTO paraclinicalResultDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ParaclinicalResult : {}", paraclinicalResultDTO);
        if (paraclinicalResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new paraclinicalResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paraclinicalResultDTO = paraclinicalResultService.save(paraclinicalResultDTO);
        return ResponseEntity.created(new URI("/api/paraclinical-results/" + paraclinicalResultDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paraclinicalResultDTO.getId().toString()))
            .body(paraclinicalResultDTO);
    }

    /**
     * {@code PUT  /paraclinical-results/:id} : Updates an existing paraclinicalResult.
     *
     * @param id the id of the paraclinicalResultDTO to save.
     * @param paraclinicalResultDTO the paraclinicalResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paraclinicalResultDTO,
     * or with status {@code 400 (Bad Request)} if the paraclinicalResultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paraclinicalResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ParaclinicalResultDTO> updateParaclinicalResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParaclinicalResultDTO paraclinicalResultDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ParaclinicalResult : {}, {}", id, paraclinicalResultDTO);
        if (paraclinicalResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paraclinicalResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paraclinicalResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paraclinicalResultDTO = paraclinicalResultService.update(paraclinicalResultDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paraclinicalResultDTO.getId().toString()))
            .body(paraclinicalResultDTO);
    }

    /**
     * {@code PATCH  /paraclinical-results/:id} : Partial updates given fields of an existing paraclinicalResult, field will ignore if it is null
     *
     * @param id the id of the paraclinicalResultDTO to save.
     * @param paraclinicalResultDTO the paraclinicalResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paraclinicalResultDTO,
     * or with status {@code 400 (Bad Request)} if the paraclinicalResultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paraclinicalResultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paraclinicalResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParaclinicalResultDTO> partialUpdateParaclinicalResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParaclinicalResultDTO paraclinicalResultDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ParaclinicalResult partially : {}, {}", id, paraclinicalResultDTO);
        if (paraclinicalResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paraclinicalResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paraclinicalResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParaclinicalResultDTO> result = paraclinicalResultService.partialUpdate(paraclinicalResultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paraclinicalResultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paraclinical-results} : get all the paraclinicalResults.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paraclinicalResults in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ParaclinicalResultDTO>> getAllParaclinicalResults(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ParaclinicalResults");
        Page<ParaclinicalResultDTO> page = paraclinicalResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paraclinical-results/:id} : get the "id" paraclinicalResult.
     *
     * @param id the id of the paraclinicalResultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paraclinicalResultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParaclinicalResultDTO> getParaclinicalResult(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ParaclinicalResult : {}", id);
        Optional<ParaclinicalResultDTO> paraclinicalResultDTO = paraclinicalResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paraclinicalResultDTO);
    }

    /**
     * {@code DELETE  /paraclinical-results/:id} : delete the "id" paraclinicalResult.
     *
     * @param id the id of the paraclinicalResultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParaclinicalResult(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ParaclinicalResult : {}", id);
        paraclinicalResultService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
