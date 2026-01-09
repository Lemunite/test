package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.VaccinationForBabyRepository;
import com.mycompany.microservice.service.VaccinationForBabyService;
import com.mycompany.microservice.service.dto.VaccinationForBabyDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.microservice.domain.VaccinationForBaby}.
 */
@RestController
@RequestMapping("/api/vaccination-for-babies")
public class VaccinationForBabyResource {

    private static final Logger LOG = LoggerFactory.getLogger(VaccinationForBabyResource.class);

    private static final String ENTITY_NAME = "microserviceVaccinationForBaby";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaccinationForBabyService vaccinationForBabyService;

    private final VaccinationForBabyRepository vaccinationForBabyRepository;

    public VaccinationForBabyResource(
        VaccinationForBabyService vaccinationForBabyService,
        VaccinationForBabyRepository vaccinationForBabyRepository
    ) {
        this.vaccinationForBabyService = vaccinationForBabyService;
        this.vaccinationForBabyRepository = vaccinationForBabyRepository;
    }

    /**
     * {@code POST  /vaccination-for-babies} : Create a new vaccinationForBaby.
     *
     * @param vaccinationForBabyDTO the vaccinationForBabyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaccinationForBabyDTO, or with status {@code 400 (Bad Request)} if the vaccinationForBaby has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VaccinationForBabyDTO> createVaccinationForBaby(@Valid @RequestBody VaccinationForBabyDTO vaccinationForBabyDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save VaccinationForBaby : {}", vaccinationForBabyDTO);
        if (vaccinationForBabyDTO.getId() != null) {
            throw new BadRequestAlertException("A new vaccinationForBaby cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vaccinationForBabyDTO = vaccinationForBabyService.save(vaccinationForBabyDTO);
        return ResponseEntity.created(new URI("/api/vaccination-for-babies/" + vaccinationForBabyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vaccinationForBabyDTO.getId().toString()))
            .body(vaccinationForBabyDTO);
    }

    /**
     * {@code PUT  /vaccination-for-babies/:id} : Updates an existing vaccinationForBaby.
     *
     * @param id the id of the vaccinationForBabyDTO to save.
     * @param vaccinationForBabyDTO the vaccinationForBabyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationForBabyDTO,
     * or with status {@code 400 (Bad Request)} if the vaccinationForBabyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationForBabyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VaccinationForBabyDTO> updateVaccinationForBaby(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VaccinationForBabyDTO vaccinationForBabyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update VaccinationForBaby : {}, {}", id, vaccinationForBabyDTO);
        if (vaccinationForBabyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationForBabyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationForBabyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vaccinationForBabyDTO = vaccinationForBabyService.update(vaccinationForBabyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaccinationForBabyDTO.getId().toString()))
            .body(vaccinationForBabyDTO);
    }

    /**
     * {@code PATCH  /vaccination-for-babies/:id} : Partial updates given fields of an existing vaccinationForBaby, field will ignore if it is null
     *
     * @param id the id of the vaccinationForBabyDTO to save.
     * @param vaccinationForBabyDTO the vaccinationForBabyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccinationForBabyDTO,
     * or with status {@code 400 (Bad Request)} if the vaccinationForBabyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vaccinationForBabyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaccinationForBabyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VaccinationForBabyDTO> partialUpdateVaccinationForBaby(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VaccinationForBabyDTO vaccinationForBabyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VaccinationForBaby partially : {}, {}", id, vaccinationForBabyDTO);
        if (vaccinationForBabyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccinationForBabyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationForBabyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VaccinationForBabyDTO> result = vaccinationForBabyService.partialUpdate(vaccinationForBabyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaccinationForBabyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vaccination-for-babies} : get all the vaccinationForBabies.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaccinationForBabies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VaccinationForBabyDTO>> getAllVaccinationForBabies(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("patient-is-null".equals(filter)) {
            LOG.debug("REST request to get all VaccinationForBabys where patient is null");
            return new ResponseEntity<>(vaccinationForBabyService.findAllWherePatientIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of VaccinationForBabies");
        Page<VaccinationForBabyDTO> page = vaccinationForBabyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vaccination-for-babies/:id} : get the "id" vaccinationForBaby.
     *
     * @param id the id of the vaccinationForBabyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaccinationForBabyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VaccinationForBabyDTO> getVaccinationForBaby(@PathVariable("id") Long id) {
        LOG.debug("REST request to get VaccinationForBaby : {}", id);
        Optional<VaccinationForBabyDTO> vaccinationForBabyDTO = vaccinationForBabyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vaccinationForBabyDTO);
    }

    /**
     * {@code DELETE  /vaccination-for-babies/:id} : delete the "id" vaccinationForBaby.
     *
     * @param id the id of the vaccinationForBabyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaccinationForBaby(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete VaccinationForBaby : {}", id);
        vaccinationForBabyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
