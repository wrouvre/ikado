package fr.ippon.ikado.repository;

import fr.ippon.ikado.domain.Gift;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Gift entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GiftRepository extends JpaRepository<Gift,Long> {

    @Query("select gift from Gift gift where gift.user.login = ?#{principal.username}")
    List<Gift> findByUserIsCurrentUser();
    
}
