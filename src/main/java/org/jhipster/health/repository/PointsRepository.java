package org.jhipster.health.repository;

import org.jhipster.health.domain.Points;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Points entity.
 */
@SuppressWarnings("unused")
public interface PointsRepository extends JpaRepository<Points,Long> {

    @Query("select points from Points points where points.user.login = ?#{principal.username}")
    Page<Points> findByUserIsCurrentUser(Pageable pageable);

    @Query("select points from Points points where points.date >= :start_date and points.date <= :end_date")
    List<Points> findAllByDateBetween(@Param("start_date") LocalDate startDate, @Param("end_date") LocalDate endDate);
}
