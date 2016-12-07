package org.jhipster.health.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.jhipster.health.domain.BloodPressure;
import org.jhipster.health.domain.BloodPressureByPeriod;

import org.jhipster.health.repository.BloodRepository;
import org.jhipster.health.security.SecurityUtils;
import org.jhipster.health.web.rest.util.HeaderUtil;
import org.jhipster.health.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * REST controller for managing Blood.
 */
@RestController
@RequestMapping("/api")
public class BloodResource {

    private final Logger log = LoggerFactory.getLogger(BloodResource.class);

    @Inject
    private BloodRepository bloodRepository;

    /**
     * POST  /blood : Create a new blood.
     *
     * @param bloodPressure the blood to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blood, or with status 400 (Bad Request) if the blood has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/blood")
    @Timed
    public ResponseEntity<BloodPressure> createBlood(@RequestBody BloodPressure bloodPressure) throws URISyntaxException {
        log.debug("REST request to save Blood : {}", bloodPressure);
        if (bloodPressure.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("blood", "idexists", "A new blood cannot already have an ID")).body(null);
        }
        BloodPressure result = bloodRepository.save(bloodPressure);
        return ResponseEntity.created(new URI("/api/blood/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("blood", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /blood : Updates an existing blood.
     *
     * @param bloodPressure the blood to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated blood,
     * or with status 400 (Bad Request) if the blood is not valid,
     * or with status 500 (Internal Server Error) if the blood couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/blood")
    @Timed
    public ResponseEntity<BloodPressure> updateBlood(@RequestBody BloodPressure bloodPressure) throws URISyntaxException {
        log.debug("REST request to update Blood : {}", bloodPressure);
        if (bloodPressure.getId() == null) {
            return createBlood(bloodPressure);
        }
        BloodPressure result = bloodRepository.save(bloodPressure);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("blood", bloodPressure.getId().toString()))
            .body(result);
    }

    /**
     * GET  /blood : get all the blood.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of blood in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/blood")
    @Timed
    public ResponseEntity<List<BloodPressure>> getAllBlood(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Blood");
        Page<BloodPressure> page = bloodRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blood");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /blood/:id : get the "id" blood.
     *
     * @param id the id of the blood to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the blood, or with status 404 (Not Found)
     */
    @GetMapping("/blood/{id}")
    @Timed
    public ResponseEntity<BloodPressure> getBlood(@PathVariable Long id) {
        log.debug("REST request to get Blood : {}", id);
        BloodPressure bloodPressure = bloodRepository.findOne(id);
        return Optional.ofNullable(bloodPressure)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /blood/:id : delete the "id" blood.
     *
     * @param id the id of the blood to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/blood/{id}")
    @Timed
    public ResponseEntity<Void> deleteBlood(@PathVariable Long id) {
        log.debug("REST request to delete Blood : {}", id);
        bloodRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("blood", id.toString())).build();
    }

    @RequestMapping(value = "/bp-by-days/{days}")
    @Timed
    public ResponseEntity<BloodPressureByPeriod> getByDays(@PathVariable int days) {
        log.debug("REST request to get period Blood Pressures: {}", days);
        LocalDate today = LocalDate.now();
        LocalDate previousDate = today.minusDays(days);
        List<BloodPressure> readings = bloodRepository
            .findAllBetweenOrderByTimestampDesc(previousDate, today);
        BloodPressureByPeriod response = new BloodPressureByPeriod("Last " + days + " Days",
            filterByUser(readings));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    private List<BloodPressure> filterByUser(List<BloodPressure> readings) {
        Stream<BloodPressure> userReadings = readings.stream()
            .filter(bp -> bp.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin()));
        return userReadings.collect(Collectors.toList());
    }

}
