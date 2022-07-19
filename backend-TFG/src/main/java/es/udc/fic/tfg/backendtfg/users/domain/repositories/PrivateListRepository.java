package es.udc.fic.tfg.backendtfg.users.domain.repositories;

import es.udc.fic.tfg.backendtfg.users.domain.entities.PrivateList;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PrivateListRepository extends PagingAndSortingRepository<PrivateList, UUID> {

}
