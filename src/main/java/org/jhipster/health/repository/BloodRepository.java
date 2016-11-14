package org.jhipster.health.repository;

import org.jhipster.health.domain.Blood;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Blood entity.
 */
@SuppressWarnings("unused")
public interface BloodRepository extends JpaRepository<Blood,Long> {

    @Query("select blood from Blood blood where blood.user.login = ?#{principal.username}")
    List<Blood> findByUserIsCurrentUser();

}
