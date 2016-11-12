package org.jhipster.health.repository;

import org.jhipster.health.domain.Goal;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Goal entity.
 */
@SuppressWarnings("unused")
public interface GoalRepository extends JpaRepository<Goal,Long> {

    @Query("select goal from Goal goal where goal.user.login = ?#{principal.username}")
    List<Goal> findByUserIsCurrentUser();

    @Query("select distinct goal from Goal goal left join fetch goal.metrics")
    List<Goal> findAllWithEagerRelationships();

    @Query("select goal from Goal goal left join fetch goal.metrics where goal.id =:id")
    Goal findOneWithEagerRelationships(@Param("id") Long id);

}
