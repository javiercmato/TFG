package es.udc.fic.tfg.backendtfg.social.application;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Notification;
import es.udc.fic.tfg.backendtfg.social.domain.repositories.NotificationRepository;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

import static es.udc.fic.tfg.backendtfg.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SocialServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private SocialService socialService;
    
    /* ************************* MÉTODOS AUXILIARES ************************* */
    /** Genera datos de un usuario válido. */
    private User generateValidUser() {
        User user = new User();
        user.setNickname(DEFAULT_NICKNAME);
        user.setPassword(DEFAULT_PASSWORD);
        user.setName(DEFAULT_NAME);
        user.setSurname(DEFAULT_SURNAME);
        user.setEmail(user.getNickname() + DEFAULT_EMAIL_DOMAIN);
        user.setRole(UserRole.USER);
        user.setBannedByAdmin(false);
        user.setRegisterDate(LocalDateTime.now());
        user.setPrivateLists(Collections.emptySet());
        
        return userRepository.save(user);
    }
    
    private Notification generateValidNotification(User target) {
        Notification notification = new Notification();
        notification.setTitle(DEFAULT_NOTIFICATION_TITLE);
        notification.setMessage(DEFAULT_NOTIFICATION_MESSAGE);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setTarget(target);
        
        return notificationRepository.save(notification);
    }
    
    /* ************************* CASOS DE PRUEBA ************************* */
    @Test
    void whenCreateNotification_thenNewNotificationIsCreated() throws EntityNotFoundException {
        // Crear datos de prueba
        User target = generateValidUser();
        
        // Ejecutar funcionalidades
        Notification notification = socialService
                .createNotification(DEFAULT_NOTIFICATION_TITLE, DEFAULT_NOTIFICATION_MESSAGE, target.getId());
        
        // Comprobar resultados
        assertAll(
            // Se crea la notificación
            () -> assertNotNull(notification),
            // Datos son los mismos
            () -> assertEquals(DEFAULT_NOTIFICATION_TITLE, notification.getTitle()),
            () -> assertEquals(DEFAULT_NOTIFICATION_MESSAGE, notification.getMessage()),
            () -> assertEquals(target, notification.getTarget()),
            () -> assertFalse(notification.isRead()),
            () -> assertTrue(notification.getCreatedAt().isBefore(LocalDateTime.now()))
        );
    }
    
}
