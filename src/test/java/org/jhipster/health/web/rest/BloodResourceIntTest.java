package org.jhipster.health.web.rest;

import org.jhipster.health.Application;

import org.jhipster.health.domain.Blood;
import org.jhipster.health.repository.BloodRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BloodResource REST controller.
 *
 * @see BloodResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BloodResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SYSTOLIC = 1;
    private static final Integer UPDATED_SYSTOLIC = 2;

    private static final Integer DEFAULT_DIASTOLIC = 1;
    private static final Integer UPDATED_DIASTOLIC = 2;

    @Inject
    private BloodRepository bloodRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBloodMockMvc;

    private Blood blood;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BloodResource bloodResource = new BloodResource();
        ReflectionTestUtils.setField(bloodResource, "bloodRepository", bloodRepository);
        this.restBloodMockMvc = MockMvcBuilders.standaloneSetup(bloodResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blood createEntity(EntityManager em) {
        Blood blood = new Blood()
                .date(DEFAULT_DATE)
                .systolic(DEFAULT_SYSTOLIC)
                .diastolic(DEFAULT_DIASTOLIC);
        return blood;
    }

    @Before
    public void initTest() {
        blood = createEntity(em);
    }

    @Test
    @Transactional
    public void createBlood() throws Exception {
        int databaseSizeBeforeCreate = bloodRepository.findAll().size();

        // Create the Blood

        restBloodMockMvc.perform(post("/api/blood")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(blood)))
                .andExpect(status().isCreated());

        // Validate the Blood in the database
        List<Blood> blood = bloodRepository.findAll();
        assertThat(blood).hasSize(databaseSizeBeforeCreate + 1);
        Blood testBlood = blood.get(blood.size() - 1);
        assertThat(testBlood.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBlood.getSystolic()).isEqualTo(DEFAULT_SYSTOLIC);
        assertThat(testBlood.getDiastolic()).isEqualTo(DEFAULT_DIASTOLIC);
    }

    @Test
    @Transactional
    public void getAllBlood() throws Exception {
        // Initialize the database
        bloodRepository.saveAndFlush(blood);

        // Get all the blood
        restBloodMockMvc.perform(get("/api/blood?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(blood.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
                .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));
    }

    @Test
    @Transactional
    public void getBlood() throws Exception {
        // Initialize the database
        bloodRepository.saveAndFlush(blood);

        // Get the blood
        restBloodMockMvc.perform(get("/api/blood/{id}", blood.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(blood.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.systolic").value(DEFAULT_SYSTOLIC))
            .andExpect(jsonPath("$.diastolic").value(DEFAULT_DIASTOLIC));
    }

    @Test
    @Transactional
    public void getNonExistingBlood() throws Exception {
        // Get the blood
        restBloodMockMvc.perform(get("/api/blood/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlood() throws Exception {
        // Initialize the database
        bloodRepository.saveAndFlush(blood);
        int databaseSizeBeforeUpdate = bloodRepository.findAll().size();

        // Update the blood
        Blood updatedBlood = bloodRepository.findOne(blood.getId());
        updatedBlood
                .date(UPDATED_DATE)
                .systolic(UPDATED_SYSTOLIC)
                .diastolic(UPDATED_DIASTOLIC);

        restBloodMockMvc.perform(put("/api/blood")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBlood)))
                .andExpect(status().isOk());

        // Validate the Blood in the database
        List<Blood> blood = bloodRepository.findAll();
        assertThat(blood).hasSize(databaseSizeBeforeUpdate);
        Blood testBlood = blood.get(blood.size() - 1);
        assertThat(testBlood.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBlood.getSystolic()).isEqualTo(UPDATED_SYSTOLIC);
        assertThat(testBlood.getDiastolic()).isEqualTo(UPDATED_DIASTOLIC);
    }

    @Test
    @Transactional
    public void deleteBlood() throws Exception {
        // Initialize the database
        bloodRepository.saveAndFlush(blood);
        int databaseSizeBeforeDelete = bloodRepository.findAll().size();

        // Get the blood
        restBloodMockMvc.perform(delete("/api/blood/{id}", blood.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Blood> blood = bloodRepository.findAll();
        assertThat(blood).hasSize(databaseSizeBeforeDelete - 1);
    }
}
