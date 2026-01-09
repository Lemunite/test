package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.VaccineRepository;
import com.mycompany.microservice.service.VaccineService;
import com.mycompany.microservice.service.dto.VaccineDTO;
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
 * REST controller for managing {@link com.mycompany.microservice.domain.Vaccine}.
 */
@RestController
@RequestMapping("/api/vaccines")
public class VaccineResource {

    private static final Logger LOG = LoggerFactory.getLogger(VaccineResource.class);

    private static final String ENTITY_NAME = "microserviceVaccine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaccineService vaccineService;

    private final VaccineRepository vaccineRepository;

    public VaccineResource(VaccineService vaccineService, VaccineRepository vaccineRepository) {
        this.vaccineService = vaccineService;
        this.vaccineRepository = vaccineRepository;
    }

    /**
     * {@code POST  /vaccines} : Create a new vaccine.
     *
     * @param vaccineDTO the vaccineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaccineDTO, or with status {@code 400 (Bad Request)} if the vaccine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VaccineDTO> createVaccine(@Valid @RequestBody VaccineDTO vaccineDTO) throws URISyntaxException {
        LOG.debug("REST request to save Vaccine : {}", vaccineDTO);
        if (vaccineDTO.getId() != null) {
            throw new BadRequestAlertException("A new vaccine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vaccineDTO = vaccineService.save(vaccineDTO);
        return ResponseEntity.created(new URI("/api/vaccines/" + vaccineDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vaccineDTO.getId().toString()))
            .body(vaccineDTO);
    }

    /**
     * {@code PUT  /vaccines/:id} : Updates an existing vaccine.
     *
     * @param id the id of the vaccineDTO to save.
     * @param vaccineDTO the vaccineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccineDTO,
     * or with status {@code 400 (Bad Request)} if the vaccineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaccineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VaccineDTO> updateVaccine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VaccineDTO vaccineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Vaccine : {}, {}", id, vaccineDTO);
        if (vaccineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vaccineDTO = vaccineService.update(vaccineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaccineDTO.getId().toString()))
            .body(vaccineDTO);
    }

    /**
     * {@code PATCH  /vaccines/:id} : Partial updates given fields of an existing vaccine, field will ignore if it is null
     *
     * @param id the id of the vaccineDTO to save.
     * @param vaccineDTO the vaccineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccineDTO,
     * or with status {@code 400 (Bad Request)} if the vaccineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vaccineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaccineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VaccineDTO> partialUpdateVaccine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VaccineDTO vaccineDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Vaccine partially : {}, {}", id, vaccineDTO);
        if (vaccineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VaccineDTO> result = vaccineService.partialUpdate(vaccineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaccineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vaccines} : get all the vaccines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaccines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VaccineDTO>> getAllVaccines(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Vaccines");
        Page<VaccineDTO> page = vaccineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vaccines/:id} : get the "id" vaccine.
     *
     * @param id the id of the vaccineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaccineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VaccineDTO> getVaccine(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Vaccine : {}", id);
        Optional<VaccineDTO> vaccineDTO = vaccineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vaccineDTO);
    }

    /**
     * {@code DELETE  /vaccines/:id} : delete the "id" vaccine.
     *
     * @param id the id of the vaccineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaccine(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Vaccine : {}", id);
        vaccineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
