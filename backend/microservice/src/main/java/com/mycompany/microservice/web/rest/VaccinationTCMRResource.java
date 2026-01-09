package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.VaccinationTCMRRepository;
import com.mycompany.microservice.service.VaccinationTCMRService;
import com.mycompany.microservice.service.dto.VaccinationTCMRDTO;
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
 * REST controller for managing {@link com.mycompany.microservice.domain.VaccinationTCMR}.
 */
@RestController
@RequestMapping("/api/vaccination-tcmrs")
public class VaccinationTCMRResource {

    private static final Logger LOG = LoggerFactory.getLogger(VaccinationTCMRResource.class);

    private static final String ENTITY_NAME = "microserviceVaccinationTcmr";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaccinationTCMRService vaccinationTCMRService;

    private final VaccinationTCMRRepository vaccinationTCMRRepository;

    public VaccinationTCMRResource(VaccinationTCMRService vaccinationTCMRService, VaccinationTCMRRepository vaccinationTCMRRepository) {
        this.vaccinationTCMRService = vaccinationTCMRService;
        this.vaccinationTCMRRepository = vaccinationTCMRRepository;
    }

    /**
     * {@code POST  /vaccination-tcmrs} : Create a new vaccinationTCMR.
     *
     * @param vaccinationTCMRDTO the vaccinationTCMRDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaccinationTCMRDTO, or with status {@code 400 (Bad Request)} if the vaccinationTCMR has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VaccinationTCMRDTO> createVaccinationTCMR(@Valid @RequestBody VaccinationTCMRDTO vaccinationTCMRDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save VaccinationTCMR : {}", vaccinationTCMRDTO);
        if (vaccinationTCMRDTO.getId() != null) {
            throw new BadRequestAlertException("A new vaccinationTCMR cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vaccinationTCMRDTO = vaccinationTCMRService.save(vaccinationTCMRDTO);
        return ResponseEntity.created(new URI("/api/vaccination-tcmrs/" + vaccinationTCMRDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vaccinationTCMRDTO.getId().toString()))
            .body(vaccinationTCMRDTO);
    }

    /**
     * {@code PUT  /vaccination-tcmrs/:id} : Updates an existing vaccinationTCMR.
     *
     * @param id the id of the vaccinationTCMRDTO to save.
     * @param vaccinationTCMRDTO the vaccinationTCMRDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationTCMRDTO,
     * or with status {@code 400 (Bad Request)} if the vaccinationTCMRDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationTCMRDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VaccinationTCMRDTO> updateVaccinationTCMR(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VaccinationTCMRDTO vaccinationTCMRDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update VaccinationTCMR : {}, {}", id, vaccinationTCMRDTO);
        if (vaccinationTCMRDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationTCMRDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationTCMRRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vaccinationTCMRDTO = vaccinationTCMRService.update(vaccinationTCMRDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaccinationTCMRDTO.getId().toString()))
            .body(vaccinationTCMRDTO);
    }

    /**
     * {@code PATCH  /vaccination-tcmrs/:id} : Partial updates given fields of an existing vaccinationTCMR, field will ignore if it is null
     *
     * @param id the id of the vaccinationTCMRDTO to save.
     * @param vaccinationTCMRDTO the vaccinationTCMRDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationTCMRDTO,
     * or with status {@code 400 (Bad Request)} if the vaccinationTCMRDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vaccinationTCMRDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationTCMRDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VaccinationTCMRDTO> partialUpdateVaccinationTCMR(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VaccinationTCMRDTO vaccinationTCMRDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VaccinationTCMR partially : {}, {}", id, vaccinationTCMRDTO);
        if (vaccinationTCMRDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationTCMRDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationTCMRRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VaccinationTCMRDTO> result = vaccinationTCMRService.partialUpdate(vaccinationTCMRDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaccinationTCMRDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vaccination-tcmrs} : get all the vaccinationTCMRS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaccinationTCMRS in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VaccinationTCMRDTO>> getAllVaccinationTCMRS(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of VaccinationTCMRS");
        Page<VaccinationTCMRDTO> page = vaccinationTCMRService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vaccination-tcmrs/:id} : get the "id" vaccinationTCMR.
     *
     * @param id the id of the vaccinationTCMRDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaccinationTCMRDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VaccinationTCMRDTO> getVaccinationTCMR(@PathVariable("id") Long id) {
        LOG.debug("REST request to get VaccinationTCMR : {}", id);
        Optional<VaccinationTCMRDTO> vaccinationTCMRDTO = vaccinationTCMRService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vaccinationTCMRDTO);
    }

    /**
     * {@code DELETE  /vaccination-tcmrs/:id} : delete the "id" vaccinationTCMR.
     *
     * @param id the id of the vaccinationTCMRDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaccinationTCMR(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete VaccinationTCMR : {}", id);
        vaccinationTCMRService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
