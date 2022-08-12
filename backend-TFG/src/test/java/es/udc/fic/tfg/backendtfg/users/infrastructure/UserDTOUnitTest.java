package es.udc.fic.tfg.backendtfg.users.infrastructure;

import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static es.udc.fic.tfg.backendtfg.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserDTOUnitTest {
    @Test
    public void testUserDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        String avatar = "";
        
        // Ejecutar código
        UserDTO dto = new UserDTO();
        dto.setUserID(id);
        dto.setName(DEFAULT_NAME);
        dto.setSurname(DEFAULT_SURNAME);
        dto.setNickname(DEFAULT_NICKNAME);
        dto.setBannedByAdmin(false);
        dto.setRole(UserRole.USER.toString());
        dto.setRegisterDate(currentDateTime);
        dto.setAvatar(avatar);
        dto.setEmail(DEFAULT_NAME + DEFAULT_EMAIL_DOMAIN);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, dto.getUserID()),
                () -> assertEquals(DEFAULT_NAME, dto.getName()),
                () -> assertEquals(DEFAULT_SURNAME, dto.getSurname()),
                () -> assertEquals(DEFAULT_NICKNAME, dto.getNickname()),
                () -> assertFalse(dto.isBannedByAdmin()),
                () -> assertEquals(UserRole.USER.toString(), dto.getRole()),
                () -> assertEquals(currentDateTime, dto.getRegisterDate()),
                () -> assertEquals(avatar, dto.getAvatar()),
                () -> assertEquals(DEFAULT_NAME + DEFAULT_EMAIL_DOMAIN, dto.getEmail())
        );
    }
    
    @Test
    public void testSignUpParamsDTO() {
        // Crear datos de prueba
        String avatar = "";
        
        // Ejecutar código
        SignUpParamsDTO dto = new SignUpParamsDTO();
        dto.setName(DEFAULT_NAME);
        dto.setSurname(DEFAULT_SURNAME);
        dto.setNickname(DEFAULT_NICKNAME);
        dto.setAvatar(avatar);
        dto.setEmail(DEFAULT_NAME + DEFAULT_EMAIL_DOMAIN);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(DEFAULT_NAME, dto.getName()),
                () -> assertEquals(DEFAULT_SURNAME, dto.getSurname()),
                () -> assertEquals(DEFAULT_NICKNAME, dto.getNickname()),
                () -> assertEquals(avatar, dto.getAvatar()),
                () -> assertEquals(DEFAULT_NAME + DEFAULT_EMAIL_DOMAIN, dto.getEmail())
        );
    }
    
    @Test
    public void testLoginParamsDTO() {
        // Ejecutar código
        LoginParamsDTO dto = new LoginParamsDTO();
        dto.setNickname(DEFAULT_NICKNAME);
        dto.setPassword(DEFAULT_PASSWORD);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(DEFAULT_NICKNAME, dto.getNickname()),
                () -> assertEquals(DEFAULT_PASSWORD, dto.getPassword())
        );
    }
    
    @Test
    public void testUpdateProfileParamsDTO() {
        // Crear datos de prueba
        LocalDateTime currentDateTime = LocalDateTime.now();
        String avatar = "";
        
        // Ejecutar código
        UpdateProfileParamsDTO dto = new UpdateProfileParamsDTO();
        dto.setName(DEFAULT_NAME);
        dto.setSurname(DEFAULT_SURNAME);
        dto.setAvatar(avatar);
        dto.setEmail(DEFAULT_NAME + DEFAULT_EMAIL_DOMAIN);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(DEFAULT_NAME, dto.getName()),
                () -> assertEquals(DEFAULT_SURNAME, dto.getSurname()),
                () -> assertEquals(avatar, dto.getAvatar()),
                () -> assertEquals(DEFAULT_NAME + DEFAULT_EMAIL_DOMAIN, dto.getEmail())
        );
    }
    
    @Test
    public void testAuthenticatedUserDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        LocalDateTime currentDateTime = LocalDateTime.now();
        String avatar = "";
        String token = "token";
        
        UserDTO userDto = new UserDTO();
        userDto.setUserID(id);
        userDto.setName(DEFAULT_NAME);
        userDto.setSurname(DEFAULT_SURNAME);
        userDto.setNickname(DEFAULT_NICKNAME);
        userDto.setBannedByAdmin(false);
        userDto.setRole(UserRole.USER.toString());
        userDto.setRegisterDate(currentDateTime);
        userDto.setAvatar(avatar);
        userDto.setEmail(DEFAULT_NAME + DEFAULT_EMAIL_DOMAIN);
        
        // Ejecutar código
        AuthenticatedUserDTO dto = new AuthenticatedUserDTO(token, userDto);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(token, dto.getServiceToken()),
                () -> assertEquals(userDto, dto.getUserDTO())
        );
    }
    
    @Test
    public void testChangePasswordParamsDTO() {
        // Crear datos de prueba
        String newPassword = DEFAULT_PASSWORD + "XYZ";
        
        // Ejecutar código
        ChangePasswordParamsDTO dto = new ChangePasswordParamsDTO();
        dto.setOldPassword(DEFAULT_PASSWORD);
        dto.setNewPassword(newPassword);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(DEFAULT_PASSWORD, dto.getOldPassword()),
                () -> assertEquals(newPassword, dto.getNewPassword())
        );
    }
}
