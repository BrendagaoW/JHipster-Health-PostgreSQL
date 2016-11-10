package org.jhipster.health.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Entry.
 */
@Entity
@Table(name = "entry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Entry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "exercise")
    private Integer exercise;

    @Column(name = "meals")
    private Integer meals;

    @Column(name = "alcohol")
    private Integer alcohol;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    private Goal goal;

    @ManyToMany(mappedBy = "entries")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Metric> metrics = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Entry date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getExercise() {
        return exercise;
    }

    public Entry exercise(Integer exercise) {
        this.exercise = exercise;
        return this;
    }

    public void setExercise(Integer exercise) {
        this.exercise = exercise;
    }

    public Integer getMeals() {
        return meals;
    }

    public Entry meals(Integer meals) {
        this.meals = meals;
        return this;
    }

    public void setMeals(Integer meals) {
        this.meals = meals;
    }

    public Integer getAlcohol() {
        return alcohol;
    }

    public Entry alcohol(Integer alcohol) {
        this.alcohol = alcohol;
        return this;
    }

    public void setAlcohol(Integer alcohol) {
        this.alcohol = alcohol;
    }

    public String getNotes() {
        return notes;
    }

    public Entry notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Goal getGoal() {
        return goal;
    }

    public Entry goal(Goal goal) {
        this.goal = goal;
        return this;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Set<Metric> getMetrics() {
        return metrics;
    }

    public Entry metrics(Set<Metric> metrics) {
        this.metrics = metrics;
        return this;
    }

    public Entry addMetric(Metric metric) {
        metrics.add(metric);
        metric.getEntries().add(this);
        return this;
    }

    public Entry removeMetric(Metric metric) {
        metrics.remove(metric);
        metric.getEntries().remove(this);
        return this;
    }

    public void setMetrics(Set<Metric> metrics) {
        this.metrics = metrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entry entry = (Entry) o;
        if(entry.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, entry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Entry{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", exercise='" + exercise + "'" +
            ", meals='" + meals + "'" +
            ", alcohol='" + alcohol + "'" +
            ", notes='" + notes + "'" +
            '}';
    }
}
