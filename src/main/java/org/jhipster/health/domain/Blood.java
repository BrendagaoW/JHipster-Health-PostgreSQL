package org.jhipster.health.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Blood.
 */
@Entity
@Table(name = "blood")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Blood implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "systolic")
    private String systolic;

    @Column(name = "diastolic")
    private String diastolic;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Blood date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getSystolic() {
        return systolic;
    }

    public Blood systolic(String systolic) {
        this.systolic = systolic;
        return this;
    }

    public void setSystolic(String systolic) {
        this.systolic = systolic;
    }

    public String getDiastolic() {
        return diastolic;
    }

    public Blood diastolic(String diastolic) {
        this.diastolic = diastolic;
        return this;
    }

    public void setDiastolic(String diastolic) {
        this.diastolic = diastolic;
    }

    public User getUser() {
        return user;
    }

    public Blood user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Blood blood = (Blood) o;
        if(blood.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, blood.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Blood{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", systolic='" + systolic + "'" +
            ", diastolic='" + diastolic + "'" +
            '}';
    }
}
