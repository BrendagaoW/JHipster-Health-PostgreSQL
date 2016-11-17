package org.jhipster.health.web.rest;

import org.jhipster.health.Application;
import org.jhipster.health.domain.Points;
import org.jhipster.health.repository.PointsRepository;
import org.jhipster.health.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PointsResource REST controller.
 *
 * @see PointsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PointsResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_EXERCISE = 1;
    private static final Integer UPDATED_EXERCISE = 2;

    private static final Integer DEFAULT_MEALS = 1;
    private static final Integer UPDATED_MEALS = 2;

    private static final Integer DEFAULT_ALCOHOL = 1;
    private static final Integer UPDATED_ALCOHOL = 2;

    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private PointsRepository pointsRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    @Autowired
    private WebApplicationContext context;

    private MockMvc restPointsMockMvc;

    private Points points;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PointsResource pointsResource = new PointsResource();
        ReflectionTestUtils.setField(pointsResource, "pointsRepository", pointsRepository);
        ReflectionTestUtils.setField(pointsResource, "userRepository", userRepository);
        this.restPointsMockMvc = MockMvcBuilders.standaloneSetup(pointsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Points createEntity(EntityManager em) {
        Points points = new Points()
            .date(DEFAULT_DATE)
            .exercise(DEFAULT_EXERCISE)
            .meals(DEFAULT_MEALS)
            .alcohol(DEFAULT_ALCOHOL)
            .notes(DEFAULT_NOTES);
        return points;
    }

    @Before
    public void initTest() {
        points = createEntity(em);
    }

    @Test
    @Transactional
    public void createPoints() throws Exception {
        int databaseSizeBeforeCreate = pointsRepository.findAll().size();

        // Create the Points

        //Create security-aware mockMvc
        restPointsMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        restPointsMockMvc.perform(post("/api/points")
            .with(user("user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(points)))
            .andExpect(status().isCreated());

        // Validate the Points in the database
        List<Points> points = pointsRepository.findAll();
        assertThat(points).hasSize(databaseSizeBeforeCreate + 1);
        Points testPoints = points.get(points.size() - 1);
        assertThat(testPoints.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPoints.getExercise()).isEqualTo(DEFAULT_EXERCISE);
        assertThat(testPoints.getMeals()).isEqualTo(DEFAULT_MEALS);
        assertThat(testPoints.getAlcohol()).isEqualTo(DEFAULT_ALCOHOL);
        assertThat(testPoints.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void getAllPoints() throws Exception {
        // Initialize the database
        pointsRepository.saveAndFlush(points);

        restPointsMockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        // Get all the points
        restPointsMockMvc.perform(get("/api/points?sort=id,desc")
            .with(user("admin").roles("ADMIN")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(points.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].exercise").value(hasItem(DEFAULT_EXERCISE)))
            .andExpect(jsonPath("$.[*].meals").value(hasItem(DEFAULT_MEALS)))
            .andExpect(jsonPath("$.[*].alcohol").value(hasItem(DEFAULT_ALCOHOL)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getPoints() throws Exception {
        // Initialize the database
        pointsRepository.saveAndFlush(points);

        // Get the points
        restPointsMockMvc.perform(get("/api/points/{id}", points.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(points.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.exercise").value(DEFAULT_EXERCISE))
            .andExpect(jsonPath("$.meals").value(DEFAULT_MEALS))
            .andExpect(jsonPath("$.alcohol").value(DEFAULT_ALCOHOL))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPoints() throws Exception {
        // Get the points
        restPointsMockMvc.perform(get("/api/points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePoints() throws Exception {
        // Initialize the database
        pointsRepository.saveAndFlush(points);
        int databaseSizeBeforeUpdate = pointsRepository.findAll().size();

        // Update the points
        Points updatedPoints = pointsRepository.findOne(points.getId());
        updatedPoints
            .date(UPDATED_DATE)
            .exercise(UPDATED_EXERCISE)
            .meals(UPDATED_MEALS)
            .alcohol(UPDATED_ALCOHOL)
            .notes(UPDATED_NOTES);

        restPointsMockMvc.perform(put("/api/points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPoints)))
            .andExpect(status().isOk());

        // Validate the Points in the database
        List<Points> points = pointsRepository.findAll();
        assertThat(points).hasSize(databaseSizeBeforeUpdate);
        Points testPoints = points.get(points.size() - 1);
        assertThat(testPoints.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPoints.getExercise()).isEqualTo(UPDATED_EXERCISE);
        assertThat(testPoints.getMeals()).isEqualTo(UPDATED_MEALS);
        assertThat(testPoints.getAlcohol()).isEqualTo(UPDATED_ALCOHOL);
        assertThat(testPoints.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void deletePoints() throws Exception {
        // Initialize the database
        pointsRepository.saveAndFlush(points);
        int databaseSizeBeforeDelete = pointsRepository.findAll().size();

        // Get the points
        restPointsMockMvc.perform(delete("/api/points/{id}", points.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Points> points = pointsRepository.findAll();
        assertThat(points).hasSize(databaseSizeBeforeDelete - 1);
    }
}
