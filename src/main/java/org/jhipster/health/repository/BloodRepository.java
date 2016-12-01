package org.jhipster.health.repository;

import org.jhipster.health.domain.BloodPressure;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Blood entity.
 */
@SuppressWarnings("unused")
public interface BloodRepository extends JpaRepository<BloodPressure,Long> {

    /**
     * In the HQL , you should use the java class name and property name of the mapped @Entity instead of the actual table name and column name
     * So there use "BloodPressure" to be the table name, other than "blood_pressure"
     * The first letter of word should be upper case.
     * @return
     */
    @Query("select blood from BloodPressure blood where blood.user.login = ?#{principal.username}")
    List<BloodPressure> findByUserIsCurrentUser();

}
