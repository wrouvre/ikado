package fr.ippon.ikado.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ippon.ikado.domain.Gift;
import fr.ippon.ikado.service.GiftService;
import fr.ippon.ikado.web.rest.util.HeaderUtil;
import fr.ippon.ikado.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Gift.
 */
@RestController
@RequestMapping("/api")
public class GiftResource {

    private final Logger log = LoggerFactory.getLogger(GiftResource.class);

    private static final String ENTITY_NAME = "gift";

    private final GiftService giftService;

    public GiftResource(GiftService giftService) {
        this.giftService = giftService;
    }

    /**
     * POST  /gifts : Create a new gift.
     *
     * @param gift the gift to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gift, or with status 400 (Bad Request) if the gift has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gifts")
    @Timed
    public ResponseEntity<Gift> createGift(@RequestBody Gift gift) throws URISyntaxException {
        log.debug("REST request to save Gift : {}", gift);
        if (gift.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gift cannot already have an ID")).body(null);
        }
        Gift result = giftService.save(gift);
        return ResponseEntity.created(new URI("/api/gifts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gifts : Updates an existing gift.
     *
     * @param gift the gift to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gift,
     * or with status 400 (Bad Request) if the gift is not valid,
     * or with status 500 (Internal Server Error) if the gift couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gifts")
    @Timed
    public ResponseEntity<Gift> updateGift(@RequestBody Gift gift) throws URISyntaxException {
        log.debug("REST request to update Gift : {}", gift);
        if (gift.getId() == null) {
            return createGift(gift);
        }
        Gift result = giftService.save(gift);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gift.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gifts : get all the gifts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gifts in body
     */
    @GetMapping("/gifts")
    @Timed
    public ResponseEntity<List<Gift>> getAllGifts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Gifts");
        Page<Gift> page = giftService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gifts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gifts/:id : get the "id" gift.
     *
     * @param id the id of the gift to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gift, or with status 404 (Not Found)
     */
    @GetMapping("/gifts/{id}")
    @Timed
    public ResponseEntity<Gift> getGift(@PathVariable Long id) {
        log.debug("REST request to get Gift : {}", id);
        Gift gift = giftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gift));
    }

    /**
     * DELETE  /gifts/:id : delete the "id" gift.
     *
     * @param id the id of the gift to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gifts/{id}")
    @Timed
    public ResponseEntity<Void> deleteGift(@PathVariable Long id) {
        log.debug("REST request to delete Gift : {}", id);
        giftService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
