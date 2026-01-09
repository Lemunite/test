package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.FamilyDiseaseRepository;
import com.mycompany.microservice.service.FamilyDiseaseService;
import com.mycompany.microservice.service.dto.FamilyDiseaseDTO;
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
 * REST controller for managing {@link com.mycompany.microservice.domain.FamilyDisease}.
 */
@RestController
@RequestMapping("/api/family-diseases")
public class FamilyDiseaseResource {

    private static final Logger LOG = LoggerFactory.getLogger(FamilyDiseaseResource.class);

    private static final String ENTITY_NAME = "microserviceFamilyDisease";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilyDiseaseService familyDiseaseService;

    private final FamilyDiseaseRepository familyDiseaseRepository;

    public FamilyDiseaseResource(FamilyDiseaseService familyDiseaseService, FamilyDiseaseRepository familyDiseaseRepository) {
        this.familyDiseaseService = familyDiseaseService;
        this.familyDiseaseRepository = familyDiseaseRepository;
    }

    /**
     * {@code POST  /family-diseases} : Create a new familyDisease.
     *
     * @param familyDiseaseDTO the familyDiseaseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familyDiseaseDTO, or with status {@code 400 (Bad Request)} if the familyDisease has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FamilyDiseaseDTO> createFamilyDisease(@Valid @RequestBody FamilyDiseaseDTO familyDiseaseDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save FamilyDisease : {}", familyDiseaseDTO);
        if (familyDiseaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new familyDisease cannot already have an ID", ENTITY_NAME, "idexists");
        }
        familyDiseaseDTO = familyDiseaseService.save(familyDiseaseDTO);
        return ResponseEntity.created(new URI("/api/family-diseases/" + familyDiseaseDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, familyDiseaseDTO.getId().toString()))
            .body(familyDiseaseDTO);
    }

    /**
     * {@code PUT  /family-diseases/:id} : Updates an existing familyDisease.
     *
     * @param id the id of the familyDiseaseDTO to save.
     * @param familyDiseaseDTO the familyDiseaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyDiseaseDTO,
     * or with status {@code 400 (Bad Request)} if the familyDiseaseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familyDiseaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FamilyDiseaseDTO> updateFamilyDisease(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FamilyDiseaseDTO familyDiseaseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FamilyDisease : {}, {}", id, familyDiseaseDTO);
        if (familyDiseaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyDiseaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyDiseaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        familyDiseaseDTO = familyDiseaseService.update(familyDiseaseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyDiseaseDTO.getId().toString()))
            .body(familyDiseaseDTO);
    }

    /**
     * {@code PATCH  /family-diseases/:id} : Partial updates given fields of an existing familyDisease, field will ignore if it is null
     *
     * @param id the id of the familyDiseaseDTO to save.
     * @param familyDiseaseDTO the familyDiseaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyDiseaseDTO,
     * or with status {@code 400 (Bad Request)} if the familyDiseaseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the familyDiseaseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the familyDiseaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FamilyDiseaseDTO> partialUpdateFamilyDisease(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FamilyDiseaseDTO familyDiseaseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FamilyDisease partially : {}, {}", id, familyDiseaseDTO);
        if (familyDiseaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyDiseaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyDiseaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FamilyDiseaseDTO> result = familyDiseaseService.partialUpdate(familyDiseaseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyDiseaseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /family-diseases} : get all the familyDiseases.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familyDiseases in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FamilyDiseaseDTO>> getAllFamilyDiseases(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of FamilyDiseases");
        Page<FamilyDiseaseDTO> page = familyDiseaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /family-diseases/:id} : get the "id" familyDisease.
     *
     * @param id the id of the familyDiseaseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familyDiseaseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FamilyDiseaseDTO> getFamilyDisease(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FamilyDisease : {}", id);
        Optional<FamilyDiseaseDTO> familyDiseaseDTO = familyDiseaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familyDiseaseDTO);
    }

    /**
     * {@code DELETE  /family-diseases/:id} : delete the "id" familyDisease.
     *
     * @param id the id of the familyDiseaseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamilyDisease(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FamilyDisease : {}", id);
        familyDiseaseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
