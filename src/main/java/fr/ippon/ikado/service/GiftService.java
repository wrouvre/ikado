package fr.ippon.ikado.service;

import fr.ippon.ikado.domain.Gift;
import fr.ippon.ikado.repository.GiftRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Gift.
 */
@Service
@Transactional
public class GiftService {

    private final Logger log = LoggerFactory.getLogger(GiftService.class);

    private final GiftRepository giftRepository;

    public GiftService(GiftRepository giftRepository) {
        this.giftRepository = giftRepository;
    }

    /**
     * Save a gift.
     *
     * @param gift the entity to save
     * @return the persisted entity
     */
    public Gift save(Gift gift) {
        log.debug("Request to save Gift : {}", gift);
        return giftRepository.save(gift);
    }

    /**
     *  Get all the gifts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Gift> findAll(Pageable pageable) {
        log.debug("Request to get all Gifts");
        return giftRepository.findAll(pageable);
    }

    /**
     *  Get one gift by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Gift findOne(Long id) {
        log.debug("Request to get Gift : {}", id);
        return giftRepository.findOne(id);
    }

    /**
     *  Delete the  gift by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Gift : {}", id);
        giftRepository.delete(id);
    }
}
