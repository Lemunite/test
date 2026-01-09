package com.mycompany.microservice.web.rest;

import com.mycompany.microservice.repository.OrganExaminationRepository;
import com.mycompany.microservice.service.OrganExaminationService;
import com.mycompany.microservice.service.dto.OrganExaminationDTO;
import com.mycompany.microservice.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.mycompany.microservice.domain.OrganExamination}.
 */
@RestController
@RequestMapping("/api/organ-examinations")
public class OrganExaminationResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrganExaminationResource.class);

    private static final String ENTITY_NAME = "microserviceOrganExamination";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganExaminationService organExaminationService;

    private final OrganExaminationRepository organExaminationRepository;

    public OrganExaminationResource(
        OrganExaminationService organExaminationService,
        OrganExaminationRepository organExaminationRepository
    ) {
        this.organExaminationService = organExaminationService;
        this.organExaminationRepository = organExaminationRepository;
    }

    /**
     * {@code POST  /organ-examinations} : Create a new organExamination.
     *
     * @param organExaminationDTO the organExaminationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organExaminationDTO, or with status {@code 400 (Bad Request)} if the organExamination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrganExaminationDTO> createOrganExamination(@RequestBody OrganExaminationDTO organExaminationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save OrganExamination : {}", organExaminationDTO);
        if (organExaminationDTO.getId() != null) {
            throw new BadRequestAlertException("A new organExamination cannot already have an ID", ENTITY_NAME, "idexists");
        }
        organExaminationDTO = organExaminationService.save(organExaminationDTO);
        return ResponseEntity.created(new URI("/api/organ-examinations/" + organExaminationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, organExaminationDTO.getId().toString()))
            .body(organExaminationDTO);
    }

    /**
     * {@code PUT  /organ-examinations/:id} : Updates an existing organExamination.
     *
     * @param id the id of the organExaminationDTO to save.
     * @param organExaminationDTO the organExaminationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organExaminationDTO,
     * or with status {@code 400 (Bad Request)} if the organExaminationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organExaminationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrganExaminationDTO> updateOrganExamination(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrganExaminationDTO organExaminationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrganExamination : {}, {}", id, organExaminationDTO);
        if (organExaminationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organExaminationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organExaminationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        organExaminationDTO = organExaminationService.update(organExaminationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organExaminationDTO.getId().toString()))
            .body(organExaminationDTO);
    }

    /**
     * {@code PATCH  /organ-examinations/:id} : Partial updates given fields of an existing organExamination, field will ignore if it is null
     *
     * @param id the id of the organExaminationDTO to save.
     * @param organExaminationDTO the organExaminationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organExaminationDTO,
     * or with status {@code 400 (Bad Request)} if the organExaminationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the organExaminationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the organExaminationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrganExaminationDTO> partialUpdateOrganExamination(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrganExaminationDTO organExaminationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrganExamination partially : {}, {}", id, organExaminationDTO);
        if (organExaminationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organExaminationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organExaminationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganExaminationDTO> result = organExaminationService.partialUpdate(organExaminationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organExaminationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /organ-examinations} : get all the organExaminations.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organExaminations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrganExaminationDTO>> getAllOrganExaminations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("medicalrecord-is-null".equals(filter)) {
            LOG.debug("REST request to get all OrganExaminations where medicalRecord is null");
            return new ResponseEntity<>(organExaminationService.findAllWhereMedicalRecordIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of OrganExaminations");
        Page<OrganExaminationDTO> page = organExaminationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organ-examinations/:id} : get the "id" organExamination.
     *
     * @param id the id of the organExaminationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organExaminationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrganExaminationDTO> getOrganExamination(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrganExamination : {}", id);
        Optional<OrganExaminationDTO> organExaminationDTO = organExaminationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organExaminationDTO);
    }

    /**
     * {@code DELETE  /organ-examinations/:id} : delete the "id" organExamination.
     *
     * @param id the id of the organExaminationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganExamination(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrganExamination : {}", id);
        organExaminationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
