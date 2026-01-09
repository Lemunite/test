package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.FamilyAllergyRepository;
import com.mycompany.microservice.service.FamilyAllergyService;
import com.mycompany.microservice.service.dto.FamilyAllergyDTO;
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
 * REST controller for managing {@link com.mycompany.microservice.domain.FamilyAllergy}.
 */
@RestController
@RequestMapping("/api/family-allergies")
public class FamilyAllergyResource {

    private static final Logger LOG = LoggerFactory.getLogger(FamilyAllergyResource.class);

    private static final String ENTITY_NAME = "microserviceFamilyAllergy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilyAllergyService familyAllergyService;

    private final FamilyAllergyRepository familyAllergyRepository;

    public FamilyAllergyResource(FamilyAllergyService familyAllergyService, FamilyAllergyRepository familyAllergyRepository) {
        this.familyAllergyService = familyAllergyService;
        this.familyAllergyRepository = familyAllergyRepository;
    }

    /**
     * {@code POST  /family-allergies} : Create a new familyAllergy.
     *
     * @param familyAllergyDTO the familyAllergyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familyAllergyDTO, or with status {@code 400 (Bad Request)} if the familyAllergy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FamilyAllergyDTO> createFamilyAllergy(@Valid @RequestBody FamilyAllergyDTO familyAllergyDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save FamilyAllergy : {}", familyAllergyDTO);
        if (familyAllergyDTO.getId() != null) {
            throw new BadRequestAlertException("A new familyAllergy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        familyAllergyDTO = familyAllergyService.save(familyAllergyDTO);
        return ResponseEntity.created(new URI("/api/family-allergies/" + familyAllergyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, familyAllergyDTO.getId().toString()))
            .body(familyAllergyDTO);
    }

    /**
     * {@code PUT  /family-allergies/:id} : Updates an existing familyAllergy.
     *
     * @param id the id of the familyAllergyDTO to save.
     * @param familyAllergyDTO the familyAllergyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyAllergyDTO,
     * or with status {@code 400 (Bad Request)} if the familyAllergyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familyAllergyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FamilyAllergyDTO> updateFamilyAllergy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FamilyAllergyDTO familyAllergyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FamilyAllergy : {}, {}", id, familyAllergyDTO);
        if (familyAllergyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyAllergyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyAllergyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        familyAllergyDTO = familyAllergyService.update(familyAllergyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyAllergyDTO.getId().toString()))
            .body(familyAllergyDTO);
    }

    /**
     * {@code PATCH  /family-allergies/:id} : Partial updates given fields of an existing familyAllergy, field will ignore if it is null
     *
     * @param id the id of the familyAllergyDTO to save.
     * @param familyAllergyDTO the familyAllergyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyAllergyDTO,
     * or with status {@code 400 (Bad Request)} if the familyAllergyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the familyAllergyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the familyAllergyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FamilyAllergyDTO> partialUpdateFamilyAllergy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FamilyAllergyDTO familyAllergyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FamilyAllergy partially : {}, {}", id, familyAllergyDTO);
        if (familyAllergyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyAllergyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyAllergyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FamilyAllergyDTO> result = familyAllergyService.partialUpdate(familyAllergyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyAllergyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /family-allergies} : get all the familyAllergies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familyAllergies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FamilyAllergyDTO>> getAllFamilyAllergies(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of FamilyAllergies");
        Page<FamilyAllergyDTO> page = familyAllergyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /family-allergies/:id} : get the "id" familyAllergy.
     *
     * @param id the id of the familyAllergyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familyAllergyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FamilyAllergyDTO> getFamilyAllergy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FamilyAllergy : {}", id);
        Optional<FamilyAllergyDTO> familyAllergyDTO = familyAllergyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familyAllergyDTO);
    }

    /**
     * {@code DELETE  /family-allergies/:id} : delete the "id" familyAllergy.
     *
     * @param id the id of the familyAllergyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamilyAllergy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FamilyAllergy : {}", id);
        familyAllergyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
