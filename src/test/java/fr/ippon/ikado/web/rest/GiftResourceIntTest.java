package fr.ippon.ikado.web.rest;

import fr.ippon.ikado.IkadoApp;

import fr.ippon.ikado.domain.Gift;
import fr.ippon.ikado.repository.GiftRepository;
import fr.ippon.ikado.service.GiftService;
import fr.ippon.ikado.web.rest.errors.ExceptionTranslator;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GiftResource REST controller.
 *
 * @see GiftResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IkadoApp.class)
public class GiftResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private GiftService giftService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGiftMockMvc;

    private Gift gift;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GiftResource giftResource = new GiftResource(giftService);
        this.restGiftMockMvc = MockMvcBuilders.standaloneSetup(giftResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gift createEntity(EntityManager em) {
        Gift gift = new Gift()
            .name(DEFAULT_NAME)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE);
        return gift;
    }

    @Before
    public void initTest() {
        gift = createEntity(em);
    }

    @Test
    @Transactional
    public void createGift() throws Exception {
        int databaseSizeBeforeCreate = giftRepository.findAll().size();

        // Create the Gift
        restGiftMockMvc.perform(post("/api/gifts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gift)))
            .andExpect(status().isCreated());

        // Validate the Gift in the database
        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeCreate + 1);
        Gift testGift = giftList.get(giftList.size() - 1);
        assertThat(testGift.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGift.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testGift.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testGift.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGift.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createGiftWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = giftRepository.findAll().size();

        // Create the Gift with an existing ID
        gift.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGiftMockMvc.perform(post("/api/gifts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gift)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGifts() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get all the giftList
        restGiftMockMvc.perform(get("/api/gifts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gift.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getGift() throws Exception {
        // Initialize the database
        giftRepository.saveAndFlush(gift);

        // Get the gift
        restGiftMockMvc.perform(get("/api/gifts/{id}", gift.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gift.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGift() throws Exception {
        // Get the gift
        restGiftMockMvc.perform(get("/api/gifts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGift() throws Exception {
        // Initialize the database
        giftService.save(gift);

        int databaseSizeBeforeUpdate = giftRepository.findAll().size();

        // Update the gift
        Gift updatedGift = giftRepository.findOne(gift.getId());
        updatedGift
            .name(UPDATED_NAME)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE);

        restGiftMockMvc.perform(put("/api/gifts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGift)))
            .andExpect(status().isOk());

        // Validate the Gift in the database
        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeUpdate);
        Gift testGift = giftList.get(giftList.size() - 1);
        assertThat(testGift.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGift.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testGift.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testGift.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGift.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingGift() throws Exception {
        int databaseSizeBeforeUpdate = giftRepository.findAll().size();

        // Create the Gift

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGiftMockMvc.perform(put("/api/gifts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gift)))
            .andExpect(status().isCreated());

        // Validate the Gift in the database
        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGift() throws Exception {
        // Initialize the database
        giftService.save(gift);

        int databaseSizeBeforeDelete = giftRepository.findAll().size();

        // Get the gift
        restGiftMockMvc.perform(delete("/api/gifts/{id}", gift.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Gift> giftList = giftRepository.findAll();
        assertThat(giftList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gift.class);
        Gift gift1 = new Gift();
        gift1.setId(1L);
        Gift gift2 = new Gift();
        gift2.setId(gift1.getId());
        assertThat(gift1).isEqualTo(gift2);
        gift2.setId(2L);
        assertThat(gift1).isNotEqualTo(gift2);
        gift1.setId(null);
        assertThat(gift1).isNotEqualTo(gift2);
    }
}
