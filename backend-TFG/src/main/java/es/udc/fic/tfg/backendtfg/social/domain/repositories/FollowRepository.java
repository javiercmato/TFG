package es.udc.fic.tfg.backendtfg.social.domain.repositories;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Follow;
import es.udc.fic.tfg.backendtfg.social.domain.entities.FollowID;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FollowRepository extends PagingAndSortingRepository<Follow, FollowID> {

}
