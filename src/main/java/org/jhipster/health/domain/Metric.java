package org.jhipster.health.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Metric.
 */
@Entity
@Table(name = "metric")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Metric implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToMany(mappedBy = "metrics")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Goal> goals = new HashSet<>();

    @ManyToMany(mappedBy = "metrics")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Entry> entries = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Metric name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public Metric value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<Goal> getGoals() {
        return goals;
    }

    public Metric goals(Set<Goal> goals) {
        this.goals = goals;
        return this;
    }

    public Metric addGoal(Goal goal) {
        goals.add(goal);
        goal.getMetrics().add(this);
        return this;
    }

    public Metric removeGoal(Goal goal) {
        goals.remove(goal);
        goal.getMetrics().remove(this);
        return this;
    }

    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }

    public Set<Entry> getEntries() {
        return entries;
    }

    public Metric entries(Set<Entry> entries) {
        this.entries = entries;
        return this;
    }

    public Metric addEntry(Entry entry) {
        entries.add(entry);
        entry.getMetrics().add(this);
        return this;
    }

    public Metric removeEntry(Entry entry) {
        entries.remove(entry);
        entry.getMetrics().remove(this);
        return this;
    }

    public void setEntries(Set<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Metric metric = (Metric) o;
        if(metric.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, metric.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Metric{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", value='" + value + "'" +
            '}';
    }
}
