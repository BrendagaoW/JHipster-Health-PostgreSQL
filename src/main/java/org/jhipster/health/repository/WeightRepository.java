package org.jhipster.health.repository;

import org.jhipster.health.domain.Weight;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Weight entity.
 */
@SuppressWarnings("unused")
public interface WeightRepository extends JpaRepository<Weight,Long> {

    @Query("select weight from Weight weight where weight.user.login = ?#{principal.username}")
    List<Weight> findByUserIsCurrentUser();

    @Query("select weight from Weight weight where weight.date > :first_date and weight.date <= :second_date order by weight.date DESC")
    List<Weight> findAllBetweenOrderByTimestampDesc(@Param("first_date") LocalDate firstDate, @Param("second_date") LocalDate secondDate);
}
