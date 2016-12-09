package org.jhipster.health.domain;

import java.util.List;

public class WeightByPeriod {

    private String period;
    private List<Weight> readings;

    public WeightByPeriod(String period, List<Weight> readings) {
        this.period = period;
        this.readings = readings;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<Weight> getReadings() {
        return readings;
    }

    public void setReadings(List<Weight> readings) {
        this.readings = readings;
    }
}
