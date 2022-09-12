package es.udc.fic.tfg.backendtfg.social.domain.repositories;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, UUID> {
    /** Devuelve las notificaciones sin leer ordenadas por fecha de creación descendiente */
    Slice<Notification> findByTarget_IdAndIsReadFalseOrderByCreatedAtDesc(UUID id, Pageable pageable);

    
}
