package org.jhipster.health.web.rest;

import org.jhipster.health.Application;

import org.jhipster.health.domain.Metric;
import org.jhipster.health.repository.MetricRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MetricResource REST controller.
 *
 * @see MetricResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MetricResourceIntTest {

    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";

    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private MetricRepository metricRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMetricMockMvc;

    private Metric metric;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MetricResource metricResource = new MetricResource();
        ReflectionTestUtils.setField(metricResource, "metricRepository", metricRepository);
        this.restMetricMockMvc = MockMvcBuilders.standaloneSetup(metricResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metric createEntity(EntityManager em) {
        Metric metric = new Metric()
                .name(DEFAULT_NAME)
                .value(DEFAULT_VALUE);
        return metric;
    }

    @Before
    public void initTest() {
        metric = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetric() throws Exception {
        int databaseSizeBeforeCreate = metricRepository.findAll().size();

        // Create the Metric

        restMetricMockMvc.perform(post("/api/metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metric)))
                .andExpect(status().isCreated());

        // Validate the Metric in the database
        List<Metric> metrics = metricRepository.findAll();
        assertThat(metrics).hasSize(databaseSizeBeforeCreate + 1);
        Metric testMetric = metrics.get(metrics.size() - 1);
        assertThat(testMetric.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMetric.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = metricRepository.findAll().size();
        // set the field null
        metric.setName(null);

        // Create the Metric, which fails.

        restMetricMockMvc.perform(post("/api/metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metric)))
                .andExpect(status().isBadRequest());

        List<Metric> metrics = metricRepository.findAll();
        assertThat(metrics).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = metricRepository.findAll().size();
        // set the field null
        metric.setValue(null);

        // Create the Metric, which fails.

        restMetricMockMvc.perform(post("/api/metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metric)))
                .andExpect(status().isBadRequest());

        List<Metric> metrics = metricRepository.findAll();
        assertThat(metrics).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMetrics() throws Exception {
        // Initialize the database
        metricRepository.saveAndFlush(metric);

        // Get all the metrics
        restMetricMockMvc.perform(get("/api/metrics?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(metric.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getMetric() throws Exception {
        // Initialize the database
        metricRepository.saveAndFlush(metric);

        // Get the metric
        restMetricMockMvc.perform(get("/api/metrics/{id}", metric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metric.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMetric() throws Exception {
        // Get the metric
        restMetricMockMvc.perform(get("/api/metrics/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetric() throws Exception {
        // Initialize the database
        metricRepository.saveAndFlush(metric);
        int databaseSizeBeforeUpdate = metricRepository.findAll().size();

        // Update the metric
        Metric updatedMetric = metricRepository.findOne(metric.getId());
        updatedMetric
                .name(UPDATED_NAME)
                .value(UPDATED_VALUE);

        restMetricMockMvc.perform(put("/api/metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMetric)))
                .andExpect(status().isOk());

        // Validate the Metric in the database
        List<Metric> metrics = metricRepository.findAll();
        assertThat(metrics).hasSize(databaseSizeBeforeUpdate);
        Metric testMetric = metrics.get(metrics.size() - 1);
        assertThat(testMetric.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMetric.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteMetric() throws Exception {
        // Initialize the database
        metricRepository.saveAndFlush(metric);
        int databaseSizeBeforeDelete = metricRepository.findAll().size();

        // Get the metric
        restMetricMockMvc.perform(delete("/api/metrics/{id}", metric.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Metric> metrics = metricRepository.findAll();
        assertThat(metrics).hasSize(databaseSizeBeforeDelete - 1);
    }
}
