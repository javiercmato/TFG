package es.udc.fic.tfg.backendtfg.social.domain.repositories;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Follow;
import es.udc.fic.tfg.backendtfg.social.domain.entities.FollowID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface FollowRepository extends PagingAndSortingRepository<Follow, FollowID> {
    
    /** Recupera la gente que sigue al usuario recibido */
    @Query(value = "SELECT f from Follow f WHERE f.followed.id = ?1",
        countQuery = "SELECT count(f) FROM Follow f WHERE f.followed.id = ?1"
    )
    Slice<Follow> findFollowers(UUID following, Pageable pageable);
    
    /** Recupera la gente a la que está siguiendo el usuario recibido */
    @Query(value = "SELECT f from Follow f WHERE f.following.id = ?1",
            countQuery = "SELECT count(f) FROM Follow f WHERE f.following.id = ?1"
    )
    Slice<Follow> findFollowings(UUID following, Pageable pageable);
    
}
