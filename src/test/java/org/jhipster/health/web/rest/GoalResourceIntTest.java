package org.jhipster.health.web.rest;

import org.jhipster.health.Application;

import org.jhipster.health.domain.Goal;
import org.jhipster.health.repository.GoalRepository;

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
 * Test class for the GoalResource REST controller.
 *
 * @see GoalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GoalResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private GoalRepository goalRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGoalMockMvc;

    private Goal goal;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GoalResource goalResource = new GoalResource();
        ReflectionTestUtils.setField(goalResource, "goalRepository", goalRepository);
        this.restGoalMockMvc = MockMvcBuilders.standaloneSetup(goalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Goal createEntity(EntityManager em) {
        Goal goal = new Goal()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION);
        return goal;
    }

    @Before
    public void initTest() {
        goal = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoal() throws Exception {
        int databaseSizeBeforeCreate = goalRepository.findAll().size();

        // Create the Goal

        restGoalMockMvc.perform(post("/api/goals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(goal)))
                .andExpect(status().isCreated());

        // Validate the Goal in the database
        List<Goal> goals = goalRepository.findAll();
        assertThat(goals).hasSize(databaseSizeBeforeCreate + 1);
        Goal testGoal = goals.get(goals.size() - 1);
        assertThat(testGoal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGoal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = goalRepository.findAll().size();
        // set the field null
        goal.setName(null);

        // Create the Goal, which fails.

        restGoalMockMvc.perform(post("/api/goals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(goal)))
                .andExpect(status().isBadRequest());

        List<Goal> goals = goalRepository.findAll();
        assertThat(goals).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGoals() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get all the goals
        restGoalMockMvc.perform(get("/api/goals?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(goal.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);

        // Get the goal
        restGoalMockMvc.perform(get("/api/goals/{id}", goal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goal.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGoal() throws Exception {
        // Get the goal
        restGoalMockMvc.perform(get("/api/goals/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);
        int databaseSizeBeforeUpdate = goalRepository.findAll().size();

        // Update the goal
        Goal updatedGoal = goalRepository.findOne(goal.getId());
        updatedGoal
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION);

        restGoalMockMvc.perform(put("/api/goals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGoal)))
                .andExpect(status().isOk());

        // Validate the Goal in the database
        List<Goal> goals = goalRepository.findAll();
        assertThat(goals).hasSize(databaseSizeBeforeUpdate);
        Goal testGoal = goals.get(goals.size() - 1);
        assertThat(testGoal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGoal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteGoal() throws Exception {
        // Initialize the database
        goalRepository.saveAndFlush(goal);
        int databaseSizeBeforeDelete = goalRepository.findAll().size();

        // Get the goal
        restGoalMockMvc.perform(delete("/api/goals/{id}", goal.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Goal> goals = goalRepository.findAll();
        assertThat(goals).hasSize(databaseSizeBeforeDelete - 1);
    }
}
