package es.udc.fic.tfg.backendtfg.recipes.application;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.MeasureUnit;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientRepository;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.*;
import es.udc.fic.tfg.backendtfg.recipes.domain.exceptions.EmptyRecipeStepsListException;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.*;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.*;
import es.udc.fic.tfg.backendtfg.users.application.utils.UserUtils;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private CategoryRepository categoryRepo;
    
    @Autowired
    private UserUtils userUtils;
    
    @Autowired
    private IngredientRepository ingredientRepo;
    
    @Autowired
    private RecipeRepository recipeRepo;
    
    @Autowired
    private RecipeStepRepository stepRepo;
    
    @Autowired
    private RecipePictureRepository pictureRepo;
    
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepo;
    
    
    /* ******************** FUNCIONALIDADES INGREDIENTES ******************** */
    
    @Override
    public Category createCategoryAsAdmin(String categoryName, UUID adminID)
            throws EntityAlreadyExistsException, PermissionException {
        // Commprobar si existe el administrador
        try {
            userUtils.fetchAdministrator(adminID);
        } catch ( EntityNotFoundException ex) {
            throw new PermissionException();
        }
        
        // Comprobar si ya existe la categoría
        if ( categoryRepo.existsByNameIgnoreCase(categoryName) )
            throw new EntityAlreadyExistsException(Category.class.getSimpleName(), categoryName);
        
        // Crear categoría
        Category category = new Category();
        category.setName(categoryName);
        
        // Guardar datos y devolver instancia
        return categoryRepo.save(category);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<Category> getAllCategories( ) {
        List<Category> results = new ArrayList<>();
        
        // Busca las categorías por orden alfabético ascendente
        Sort ascNameSort = Sort.by(Direction.ASC, "name");
        Iterable<Category> categoryIterable = categoryRepo.findAll(ascNameSort);
        // Crea una lista de resultados
        categoryIterable.forEach((c) -> results.add(c));
        
        return results;
    }
    
    @Override
    public Recipe createRecipe(CreateRecipeParamsDTO params)
            throws EmptyRecipeStepsListException, EntityNotFoundException {
        // Buscar el usuario. Si no existe lanza EntityNotFoundException
        User author = userUtils.fetchUserByID(params.getAuthorID());
        
        // Buscar la categoría. Si no existe lanza EntityNotFoundException
        Category category = fetchCategoryByID(params.getCategoryID());
        
        // Crear la receta con los datos recibidos para obtener su ID
        // Así se puede asignar el ID de la receta al resto de entidades que tengan una relación con ésta
        Recipe recipe = new Recipe();
        recipe.setName(params.getName());
        recipe.setDescription(params.getDescription());
        recipe.setDuration(params.getDuration());
        recipe.setDiners(params.getDiners());
        recipe.setAuthor(author);
        recipe.setCategory(category);
        recipe.setCreationDate(LocalDateTime.now());
        recipe = recipeRepo.save(recipe);
        
        // Crear y asignar pasos a la receta
        createRecipeSteps(params.getSteps(), recipe);
        
        // Crear y asignar imágenes a la receta
        createRecipePictures(params.getPictures(), recipe);
        
        // Asignar los ingredientes con sus cantidades y unidades de medida a la receta
        attachIngredientsToRecipe(params.getIngredients(), recipe);
    
        // Guardar datos y devolver instancia
        return recipeRepo.save(recipe);
    }
    
    
    
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    /** Busca la categoría por el ID recibido */
    private Category fetchCategoryByID(UUID categoryID) throws EntityNotFoundException {
        // Comprobar si existe la categoría con el ID recibido
        Optional<Category> optionalCategory = categoryRepo.findById(categoryID);
        if ( optionalCategory.isEmpty() )
            throw new EntityNotFoundException(Category.class.getName(), categoryID);
        
        return optionalCategory.get();
    }
    
    /** Busca el ingrediente por el ID recibido */
    private Ingredient fetchIngredientByID(UUID ingredientID) throws EntityNotFoundException {
        // Comprobar si existe la categoría con el ID recibido
        Optional<Ingredient> optionalIngredient = ingredientRepo.findById(ingredientID);
        if ( optionalIngredient.isEmpty() )
            throw new EntityNotFoundException(Ingredient.class.getSimpleName(), ingredientID);
        
        return optionalIngredient.get();
    }
    
    /** Registra una lista de pasos y se los asigna a la receta recibida */
    void createRecipeSteps(List<CreateRecipeStepParamsDTO> stepParams, Recipe recipe)
            throws EmptyRecipeStepsListException {
        // Si no se recibe ninǵun paso, se lanza EmptyRecipeStepsListException
        if (stepParams.isEmpty())
            throw new EmptyRecipeStepsListException();
        
        // Para cada item recibido, crea un paso con los datos recibidos
        stepParams.stream()
                  .forEach((item) -> {
                      RecipeStepID stepID = new RecipeStepID(recipe.getId(), item.getStep());
                      RecipeStep step = new RecipeStep(stepID, item.getText(), recipe);
                      
                      // Guardar paso y asignárselo a la receta
                      step = stepRepo.save(step);
                      recipe.addStep(step);
                  });
    }
    
    /** Registra una lista de imágenes y se las asigna a la receta recibida */
    private void createRecipePictures(List<CreateRecipePictureParamsDTO> pictureParams, Recipe recipe) {
        // Para cada item recibido, crea una imagen con los datos recibidos
        pictureParams.stream()
                     .forEach((item) -> {
                         byte[] pictureBytes = Base64.getDecoder().decode(item.getData());                   // Decodificar imágen recibida
                         RecipePictureID pictureID = new RecipePictureID(recipe.getId(), item.getOrder());
                         RecipePicture picture = new RecipePicture(pictureID, pictureBytes, recipe);
                         
                         // Guardar imagen y asignárselo a la receta
                         picture = pictureRepo.save(picture);
                         recipe.addPicture(picture);
                     });
    }
    
    /** Asigna los ingredientes, junto a sus cantidades y unidades de medida, a la receta recibida.
     * Si un ingrediente no existe lanza EntityNotFoundException */
    private void attachIngredientsToRecipe(List<CreateRecipeIngredientParamsDTO> ingredientParams, Recipe recipe) throws EntityNotFoundException {
        // Obtener los ID de los ingredientes y ordenarlos
        List<UUID> ingredientIds = ingredientParams.stream()
                                                   .map((ing) -> ing.getIngredientID())
                                                   .collect(Collectors.toList());
        
        // Asignar cada ingrediente con sus propiedades a la receta recibida
        for (UUID ingredientID: ingredientIds) {
            // Busca el ingrediente. Si no existe lanza EntityNotFoundException
            Ingredient currentIngredient = fetchIngredientByID(ingredientID);
            // Asegurar que los parámetros recibidos se corresponden con el ingrediente recibido (evita problemas si los datos recibidos estuviesen desordenados)
            // Esto evitaría asignar parámetros del ingrediente 'X' al ingrediente 'Y'
            CreateRecipeIngredientParamsDTO currentIngredientParams = ingredientParams.stream()
                                                                                      .filter((item) -> item.getIngredientID() == ingredientID)
                                                                                      .findFirst()
                                                                                      .get();
            
            String quantity = currentIngredientParams.getQuantity();
            MeasureUnit measureUnit = MeasureUnit.valueOf(currentIngredientParams.getMeasureUnit());
            RecipeIngredientID recipeIngredientID = new RecipeIngredientID(recipe.getId(), ingredientID);
            RecipeIngredient recipeIngredient = new RecipeIngredient(recipeIngredientID, quantity, measureUnit, recipe, currentIngredient);
            
            // Guardar ingrediente y asignárselo a la receta
            recipeIngredient = recipeIngredientRepo.save(recipeIngredient);
            recipe.addIngredient(recipeIngredient);
        }
    }
    
    
}
