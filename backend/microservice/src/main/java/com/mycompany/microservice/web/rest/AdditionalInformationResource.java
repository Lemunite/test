package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.AdditionalInformationRepository;
import com.mycompany.microservice.service.AdditionalInformationService;
import com.mycompany.microservice.service.dto.AdditionalInformationDTO;
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
 * REST controller for managing {@link com.mycompany.microservice.domain.AdditionalInformation}.
 */
@RestController
@RequestMapping("/api/additional-informations")
public class AdditionalInformationResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdditionalInformationResource.class);

    private static final String ENTITY_NAME = "microserviceAdditionalInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdditionalInformationService additionalInformationService;

    private final AdditionalInformationRepository additionalInformationRepository;

    public AdditionalInformationResource(
        AdditionalInformationService additionalInformationService,
        AdditionalInformationRepository additionalInformationRepository
    ) {
        this.additionalInformationService = additionalInformationService;
        this.additionalInformationRepository = additionalInformationRepository;
    }

    /**
     * {@code POST  /additional-informations} : Create a new additionalInformation.
     *
     * @param additionalInformationDTO the additionalInformationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new additionalInformationDTO, or with status {@code 400 (Bad Request)} if the additionalInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdditionalInformationDTO> createAdditionalInformation(
        @Valid @RequestBody AdditionalInformationDTO additionalInformationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AdditionalInformation : {}", additionalInformationDTO);
        if (additionalInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new additionalInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        additionalInformationDTO = additionalInformationService.save(additionalInformationDTO);
        return ResponseEntity.created(new URI("/api/additional-informations/" + additionalInformationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, additionalInformationDTO.getId().toString()))
            .body(additionalInformationDTO);
    }

    /**
     * {@code PUT  /additional-informations/:id} : Updates an existing additionalInformation.
     *
     * @param id the id of the additionalInformationDTO to save.
     * @param additionalInformationDTO the additionalInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additionalInformationDTO,
     * or with status {@code 400 (Bad Request)} if the additionalInformationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the additionalInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdditionalInformationDTO> updateAdditionalInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdditionalInformationDTO additionalInformationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AdditionalInformation : {}, {}", id, additionalInformationDTO);
        if (additionalInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, additionalInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!additionalInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        additionalInformationDTO = additionalInformationService.update(additionalInformationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, additionalInformationDTO.getId().toString()))
            .body(additionalInformationDTO);
    }

    /**
     * {@code PATCH  /additional-informations/:id} : Partial updates given fields of an existing additionalInformation, field will ignore if it is null
     *
     * @param id the id of the additionalInformationDTO to save.
     * @param additionalInformationDTO the additionalInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additionalInformationDTO,
     * or with status {@code 400 (Bad Request)} if the additionalInformationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the additionalInformationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the additionalInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdditionalInformationDTO> partialUpdateAdditionalInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdditionalInformationDTO additionalInformationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AdditionalInformation partially : {}, {}", id, additionalInformationDTO);
        if (additionalInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, additionalInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!additionalInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdditionalInformationDTO> result = additionalInformationService.partialUpdate(additionalInformationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, additionalInformationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /additional-informations} : get all the additionalInformations.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of additionalInformations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdditionalInformationDTO>> getAllAdditionalInformations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("patient-is-null".equals(filter)) {
            LOG.debug("REST request to get all AdditionalInformations where patient is null");
            return new ResponseEntity<>(additionalInformationService.findAllWherePatientIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of AdditionalInformations");
        Page<AdditionalInformationDTO> page = additionalInformationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /additional-informations/:id} : get the "id" additionalInformation.
     *
     * @param id the id of the additionalInformationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the additionalInformationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdditionalInformationDTO> getAdditionalInformation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AdditionalInformation : {}", id);
        Optional<AdditionalInformationDTO> additionalInformationDTO = additionalInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(additionalInformationDTO);
    }

    /**
     * {@code DELETE  /additional-informations/:id} : delete the "id" additionalInformation.
     *
     * @param id the id of the additionalInformationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdditionalInformation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AdditionalInformation : {}", id);
        additionalInformationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
