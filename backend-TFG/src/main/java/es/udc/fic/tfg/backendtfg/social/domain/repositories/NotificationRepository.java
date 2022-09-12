package es.udc.fic.tfg.backendtfg.social.domain.repositories;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Notification;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, UUID> {

}
