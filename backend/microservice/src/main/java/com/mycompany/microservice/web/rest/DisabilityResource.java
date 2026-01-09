package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.DisabilityRepository;
import com.mycompany.microservice.service.DisabilityService;
import com.mycompany.microservice.service.dto.DisabilityDTO;
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
 * REST controller for managing {@link com.mycompany.microservice.domain.Disability}.
 */
@RestController
@RequestMapping("/api/disabilities")
public class DisabilityResource {

    private static final Logger LOG = LoggerFactory.getLogger(DisabilityResource.class);

    private static final String ENTITY_NAME = "microserviceDisability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisabilityService disabilityService;

    private final DisabilityRepository disabilityRepository;

    public DisabilityResource(DisabilityService disabilityService, DisabilityRepository disabilityRepository) {
        this.disabilityService = disabilityService;
        this.disabilityRepository = disabilityRepository;
    }

    /**
     * {@code POST  /disabilities} : Create a new disability.
     *
     * @param disabilityDTO the disabilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disabilityDTO, or with status {@code 400 (Bad Request)} if the disability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DisabilityDTO> createDisability(@Valid @RequestBody DisabilityDTO disabilityDTO) throws URISyntaxException {
        LOG.debug("REST request to save Disability : {}", disabilityDTO);
        if (disabilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new disability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        disabilityDTO = disabilityService.save(disabilityDTO);
        return ResponseEntity.created(new URI("/api/disabilities/" + disabilityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, disabilityDTO.getId().toString()))
            .body(disabilityDTO);
    }

    /**
     * {@code PUT  /disabilities/:id} : Updates an existing disability.
     *
     * @param id the id of the disabilityDTO to save.
     * @param disabilityDTO the disabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disabilityDTO,
     * or with status {@code 400 (Bad Request)} if the disabilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DisabilityDTO> updateDisability(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DisabilityDTO disabilityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Disability : {}, {}", id, disabilityDTO);
        if (disabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        disabilityDTO = disabilityService.update(disabilityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disabilityDTO.getId().toString()))
            .body(disabilityDTO);
    }

    /**
     * {@code PATCH  /disabilities/:id} : Partial updates given fields of an existing disability, field will ignore if it is null
     *
     * @param id the id of the disabilityDTO to save.
     * @param disabilityDTO the disabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disabilityDTO,
     * or with status {@code 400 (Bad Request)} if the disabilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the disabilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the disabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DisabilityDTO> partialUpdateDisability(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DisabilityDTO disabilityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Disability partially : {}, {}", id, disabilityDTO);
        if (disabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DisabilityDTO> result = disabilityService.partialUpdate(disabilityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disabilityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /disabilities} : get all the disabilities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disabilities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DisabilityDTO>> getAllDisabilities(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Disabilities");
        Page<DisabilityDTO> page = disabilityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /disabilities/:id} : get the "id" disability.
     *
     * @param id the id of the disabilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disabilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DisabilityDTO> getDisability(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Disability : {}", id);
        Optional<DisabilityDTO> disabilityDTO = disabilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(disabilityDTO);
    }

    /**
     * {@code DELETE  /disabilities/:id} : delete the "id" disability.
     *
     * @param id the id of the disabilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisability(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Disability : {}", id);
        disabilityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
