package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.AllergyRepository;
import com.mycompany.microservice.service.AllergyService;
import com.mycompany.microservice.service.dto.AllergyDTO;
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
 * REST controller for managing {@link com.mycompany.microservice.domain.Allergy}.
 */
@RestController
@RequestMapping("/api/allergies")
public class AllergyResource {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyResource.class);

    private static final String ENTITY_NAME = "microserviceAllergy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AllergyService allergyService;

    private final AllergyRepository allergyRepository;

    public AllergyResource(AllergyService allergyService, AllergyRepository allergyRepository) {
        this.allergyService = allergyService;
        this.allergyRepository = allergyRepository;
    }

    /**
     * {@code POST  /allergies} : Create a new allergy.
     *
     * @param allergyDTO the allergyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allergyDTO, or with status {@code 400 (Bad Request)} if the allergy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AllergyDTO> createAllergy(@Valid @RequestBody AllergyDTO allergyDTO) throws URISyntaxException {
        LOG.debug("REST request to save Allergy : {}", allergyDTO);
        if (allergyDTO.getId() != null) {
            throw new BadRequestAlertException("A new allergy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        allergyDTO = allergyService.save(allergyDTO);
        return ResponseEntity.created(new URI("/api/allergies/" + allergyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, allergyDTO.getId().toString()))
            .body(allergyDTO);
    }

    /**
     * {@code PUT  /allergies/:id} : Updates an existing allergy.
     *
     * @param id the id of the allergyDTO to save.
     * @param allergyDTO the allergyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allergyDTO,
     * or with status {@code 400 (Bad Request)} if the allergyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allergyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AllergyDTO> updateAllergy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AllergyDTO allergyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Allergy : {}, {}", id, allergyDTO);
        if (allergyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allergyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allergyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        allergyDTO = allergyService.update(allergyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allergyDTO.getId().toString()))
            .body(allergyDTO);
    }

    /**
     * {@code PATCH  /allergies/:id} : Partial updates given fields of an existing allergy, field will ignore if it is null
     *
     * @param id the id of the allergyDTO to save.
     * @param allergyDTO the allergyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allergyDTO,
     * or with status {@code 400 (Bad Request)} if the allergyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the allergyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the allergyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AllergyDTO> partialUpdateAllergy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AllergyDTO allergyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Allergy partially : {}, {}", id, allergyDTO);
        if (allergyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allergyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allergyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AllergyDTO> result = allergyService.partialUpdate(allergyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allergyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /allergies} : get all the allergies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of allergies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AllergyDTO>> getAllAllergies(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Allergies");
        Page<AllergyDTO> page = allergyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /allergies/:id} : get the "id" allergy.
     *
     * @param id the id of the allergyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allergyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AllergyDTO> getAllergy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Allergy : {}", id);
        Optional<AllergyDTO> allergyDTO = allergyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(allergyDTO);
    }

    /**
     * {@code DELETE  /allergies/:id} : delete the "id" allergy.
     *
     * @param id the id of the allergyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllergy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Allergy : {}", id);
        allergyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
