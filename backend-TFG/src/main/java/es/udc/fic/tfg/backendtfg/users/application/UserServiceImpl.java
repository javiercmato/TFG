package es.udc.fic.tfg.backendtfg.users.application;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.*;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.PrivateListRecipeRepository;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.RecipeRepository;
import es.udc.fic.tfg.backendtfg.users.application.utils.UserUtils;
import es.udc.fic.tfg.backendtfg.users.domain.entities.*;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectPasswordException;
import es.udc.fic.tfg.backendtfg.users.domain.repositories.PrivateListRepository;
import es.udc.fic.tfg.backendtfg.users.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PrivateListRepository listRepository;
    @Autowired
    private PrivateListRecipeRepository listRecipeRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    
    
    /* ******************** FUNCIONALIDADES USUARIO ******************** */
    @Override
    public User signUp(User user) throws EntityAlreadyExistsException {
        // Comprobar si existe un usuario con el mismo nickname
        if (userRepository.existsByNicknameIgnoreCase(user.getNickname()))
            throw new EntityAlreadyExistsException(User.class.getSimpleName(), user.getNickname());
        
        // Asignar datos por defecto del usuario
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegisterDate(LocalDateTime.now());
        user.setRole(UserRole.USER);
        
        // Guardar datos y devolver usuario creado
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    @Override
    public User login(String nickname, String rawPassword) throws IncorrectLoginException {
        // Comprobar si existe el usuario recibido
        User user;
        try {
            user = userUtils.fetchUserByNickname(nickname);
        } catch ( EntityNotFoundException ex ) {            // Convertir EntityNotFoundException en IncorrectLoginExceptions
            throw new IncorrectLoginException();
        }
    
        // Comprobar si las contraseñas coinciden
        if ( !passwordEncoder.matches(rawPassword, user.getPassword()) ) {
            throw new IncorrectLoginException();
        }
        
        return user;
    }
    
    @Transactional(readOnly = true)
    @Override
    public User loginFromToken(UUID userID) throws EntityNotFoundException {
        // Obtener al usuario
        return userUtils.fetchUserByID(userID);
    }
    
    @Override
    public User updateProfile(UUID userID, String name, String surname, String email, byte[] avatar) throws EntityNotFoundException {
        // Obtener al usuario
        User user = userUtils.fetchUserByID(userID);
        
        // Cambiar solo los datos que se han modificado
        if (name != null) user.setName(name);
        if (surname != null) user.setSurname(surname);
        if (email != null) user.setEmail(email);
        if (avatar != null) user.setAvatar(avatar);
        
        return userRepository.save(user);
    }
    
    @Override
    public void changePassword(UUID userID, String oldPassword, String newPassword) throws EntityNotFoundException, IncorrectPasswordException {
        // Obtener al usuario
        User user = userUtils.fetchUserByID(userID);
        
        // Comprobar que contraseñas coincidan
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new IncorrectPasswordException();
        
        // Cambiar contraseña y actualizar usuario
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        
        userRepository.save(user);
    }
    
    @Override
    public void deleteUser(UUID userID) throws EntityNotFoundException {
        // Obtener al usuario
        User user = userUtils.fetchUserByID(userID);
        
        // Eliminar al usuario
        userRepository.delete(user);
    }
    
    @Transactional(readOnly = true)
    @Override
    public User findUserById(UUID userID) throws EntityNotFoundException {
        return userUtils.fetchUserByID(userID);
    }
    
    @Transactional(readOnly = true)
    @Override
    public User findUserByNickname(String nickname) throws EntityNotFoundException {
        return userUtils.fetchUserByNickname(nickname);
    }
    
    @Override
    public PrivateList createPrivateList(UUID userID, String title, String description) throws EntityNotFoundException {
        // Obtener al usuario
        User creator = userUtils.fetchUserByID(userID);
        
        // Crear la lista
        PrivateList list = new PrivateList();
        list.setTitle(title);
        list.setDescription(description);
        
        // Incluye la lista en las listas privadas del usuario
        list.setCreator(creator);
        
        return listRepository.save(list);
    }
    
    @Override
    public List<PrivateList> getPrivateListsByUser(UUID userID) throws EntityNotFoundException {
        // Obtener al usuario
        User user = userUtils.fetchUserByID(userID);
        
        return user.getAllPrivateLists();
    }
    
    @Override
    public PrivateList findPrivateListByID(UUID listID) throws EntityNotFoundException {
        // Obtiene la lista. Si no existe lanza EntityNotFoundException
        return fetchPrivateList(listID);
    }
    
    @Override
    public List<Recipe> getRecipesFromPrivateList(UUID listID) throws EntityNotFoundException {
        // Obtiene la lista. Si no existe lanza EntityNotFoundException
        PrivateList list = fetchPrivateList(listID);
        
        // Recupera las imágenes almacenadas en la lista navegando por las entidades asociadas
        return list.getPrivateListRecipes()
                   .stream()
                   .map(PrivateListRecipe::getRecipe)
                   .collect(Collectors.toList());
    }
    
    @Override
    public void addRecipeToPrivateList(UUID listID, UUID recipeID) throws EntityNotFoundException {
        // Comprueba si existe la lista privada. Si no existe lanza EntityNotFoundException
        PrivateList list = fetchPrivateList(listID);
        
        // Comprueba si existe la receta. Si no existe lanza EntityNotFoundException
        Recipe recipe = fetchRecipe(recipeID);
        
        // Añadir receta a la lista
        PrivateListRecipe plr = new PrivateListRecipe();
        plr.setId(new PrivateListRecipeID(listID, recipeID));
        plr.setInsertionDate(LocalDateTime.now());
        list.insertRecipe(plr);             // Indica a la lista que tiene una nueva receta
        recipe.insertToPrivateList(plr);    // Indica a la receta que ha sido añadida en una nueva lista
        
        listRecipeRepository.save(plr);
        listRepository.save(list);
        recipeRepository.save(recipe);
    }
    
    @Override
    public void removeRecipeFromPrivateList(UUID listID, UUID recipeID) throws EntityNotFoundException {
        // Comprueba si existe la lista privada. Si no existe lanza EntityNotFoundException
        PrivateList list = fetchPrivateList(listID);
    
        // Comprueba si existe la receta. Si no existe lanza EntityNotFoundException
        Recipe recipe = fetchRecipe(recipeID);
        
        // Borrar relación entre receta y lista
        PrivateListRecipe plr = fetchPrivateListRecipe(listID, recipeID);
        list.removeRecipe(plr);                 // Indica a la lista que se ha retirado una receta
        recipe.removeFromPrivateList(plr);      // Indica a la receta que ha sido retirada de una lista
        
        listRecipeRepository.delete(plr);
        listRepository.save(list);
        recipeRepository.save(recipe);
    }
    
    @Override
    public void deletePrivateList(UUID listID) throws EntityNotFoundException {
        // Comprueba si existe la lista privada. Si no existe lanza EntityNotFoundException
        if (!listRepository.existsById(listID))
            throw new EntityNotFoundException(PrivateList.class.getSimpleName(), listID);
        
        listRepository.deleteById(listID);
    }
    
    /* ******************** FUNCIONALIDADES ADMINISTRADOR ******************** */
    @Override
    public boolean banUserAsAdmin(UUID adminID, UUID targetUserID) throws EntityNotFoundException, PermissionException {
        // Commprobar si existe el administrador
        try {
            userUtils.fetchAdministrator(adminID);
        } catch ( EntityNotFoundException ex) {
            throw new PermissionException();
        }
        
        // Obtener al usuario a banear
        User targetUser = userUtils.fetchUserByID(targetUserID);
        
        // Aplicar/retirar baneo
        targetUser.setBannedByAdmin(!targetUser.isBannedByAdmin());
        userRepository.save(targetUser);
        
        return targetUser.isBannedByAdmin();
    }
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    private PrivateList fetchPrivateList(UUID listID) throws EntityNotFoundException {
        Optional<PrivateList> optionalList = listRepository.findById(listID);
        if ( optionalList.isEmpty())
            throw new EntityNotFoundException(PrivateList.class.getSimpleName(), listID);
        
        return optionalList.get();
    }
    
    private Recipe fetchRecipe(UUID recipeID) throws EntityNotFoundException {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeID);
        if (optionalRecipe.isEmpty())
            throw new EntityNotFoundException(Recipe.class.getSimpleName(), recipeID);
        
        return optionalRecipe.get();
    }
    
    private PrivateListRecipe fetchPrivateListRecipe(UUID listID, UUID recipeID) throws EntityNotFoundException {
        PrivateListRecipeID id = new PrivateListRecipeID(listID, recipeID);

        Optional<PrivateListRecipe> optionalPlr = listRecipeRepository.findById(id);
        if (optionalPlr.isEmpty())
            throw new EntityNotFoundException(Recipe.class.getSimpleName(), recipeID);
        
        return optionalPlr.get();
    }
    
}
