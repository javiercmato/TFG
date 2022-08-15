package es.udc.fic.tfg.backendtfg.recipes.domain;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.MeasureUnit;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.*;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.PNG_EXTENSION;
import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.loadImageFromResourceName;
import static es.udc.fic.tfg.backendtfg.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RecipeUnitTest {
    
    @Test
    void createRecipe () {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        LocalDateTime creationDate = LocalDateTime.now();
        long duration = 10;
        int diners = 2;
        boolean isBannedByAdmin = false;
        Category category = new Category();
        User author = new User();
        List<RecipeStep> steps = new ArrayList<>();
        List<RecipePicture> pictures = new ArrayList<>();
        List<RecipeIngredient> ingredients = new ArrayList<>();
        List<PrivateListRecipe> privateLists = new ArrayList<>();
        
        // Ejecutar código
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(DEFAULT_RECIPE_NAME);
        recipe.setDescription(DEFAULT_RECIPE_DESCRIPTION);
        recipe.setCreationDate(creationDate);
        recipe.setDuration(duration);
        recipe.setDiners(diners);
        recipe.setBannedByAdmin(isBannedByAdmin);
        recipe.setCategory(category);
        recipe.setAuthor(author);
        recipe.setSteps(steps);
        recipe.setPictures(pictures);
        recipe.setIngredients(ingredients);
        recipe.setPrivateListRecipes(privateLists);
        author.getRecipes().add(recipe);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, recipe.getId()),
                () -> assertEquals(DEFAULT_RECIPE_NAME, recipe.getName()),
                () -> assertEquals(DEFAULT_RECIPE_DESCRIPTION, recipe.getDescription()),
                () -> assertEquals(creationDate, recipe.getCreationDate()),
                () -> assertEquals(duration, recipe.getDuration()),
                () -> assertEquals(diners, recipe.getDiners()),
                () -> assertEquals(isBannedByAdmin, recipe.isBannedByAdmin()),
                () -> assertEquals(category, recipe.getCategory()),
                () -> assertEquals(author, recipe.getAuthor()),
                () -> assertEquals(steps, recipe.getSteps()),
                () -> assertEquals(pictures, recipe.getPictures()),
                () -> assertEquals(ingredients, recipe.getIngredients()),
                () -> assertEquals(privateLists, recipe.getPrivateListRecipes()),
                () -> assertTrue(author.getRecipes().contains(recipe))
        );
    }
    
    @Test
    void createRecipeAndAddStep () {
        // Crear datos de prueba
        UUID recipeID = UUID.randomUUID();
        int stepOrder = 1;
        Recipe recipe = new Recipe();
        recipe.setId(recipeID);
        List<RecipeStep> steps = new ArrayList<>();
        RecipeStepID stepID = new RecipeStepID(recipeID, stepOrder);
        RecipeStep step = new RecipeStep(stepID, DEFAULT_RECIPESTEP_TEXT, recipe);
        RecipeStep noArgsEntity = new RecipeStep();
    
        // Ejecutar código
        recipe.setSteps(steps);
        recipe.addStep(step);
    
        // Comprobar resultados
        assertAll(
                // Hay un paso añadido a la receta
                () -> assertTrue(recipe.getSteps().size() > 0),
                () -> assertTrue(recipe.getSteps().contains(step)),
                // La clave primaria compuesta coincide
                () -> assertEquals(step.getId(), stepID),
                () -> assertEquals(stepID.getRecipeID(), recipeID),
                () -> assertEquals(stepID.getStep(), stepOrder),
                // Los datos son correctos
                () -> assertEquals(DEFAULT_RECIPESTEP_TEXT, step.getText()),
                () -> assertEquals(recipe, step.getRecipe()),
                // Constructor sin argumentos funciona
                () -> assertNotNull(noArgsEntity)
        );
    }
    
    @Test
    void createRecipeAndAddPicture () {
        // Crear datos de prueba
        UUID recipeID = UUID.randomUUID();
        int pictureOrder = 1;
        Recipe recipe = new Recipe();
        recipe.setId(recipeID);
        List<RecipePicture> pictures = new ArrayList<>();
        byte[] imageBytes = loadImageFromResourceName(DEFAULT_RECIPE_IMAGE_1, PNG_EXTENSION);
        RecipePicture noArgsEntity = new RecipePicture();
        
        RecipePictureID pictureID = new RecipePictureID(recipeID, pictureOrder);
        RecipePicture picture = new RecipePicture(pictureID, imageBytes, recipe);
        
        // Ejecutar código
        recipe.setPictures(pictures);
        recipe.addPicture(picture);
        
        // Comprobar resultados
        assertAll(
                // Hay imágenes añadidas a la receta
                () -> assertTrue(recipe.getPictures().size() > 0),
                () -> assertTrue(recipe.getPictures().contains(picture)),
                // La clave primaria compuesta coincide
                () -> assertEquals(picture.getId(), pictureID),
                () -> assertEquals(recipeID, pictureID.getRecipeID()),
                () -> assertEquals(pictureOrder, pictureID.getOrder()),
                // Los datos son correctos
                () -> assertEquals(recipe, picture.getRecipe()),
                () -> assertEquals(imageBytes, picture.getPictureData()),
                // Constructor sin argumentos funciona
                () -> assertNotNull(noArgsEntity)
        );
    }
    
    @Test
    void createRecipeAndAddIngredient () {
        // Crear datos de prueba
        UUID recipeID = UUID.randomUUID();
        UUID ingredientID = UUID.randomUUID();
        Recipe recipe = new Recipe();
        recipe.setId(recipeID);
        List<RecipeIngredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientID);
        String quantity = "2";
        MeasureUnit measureUnit = MeasureUnit.CUCHARADA;
        RecipeIngredient noArgsEntity = new RecipeIngredient();
        
        RecipeIngredientID recipeIngredientID = new RecipeIngredientID(recipeID, ingredientID);
        RecipeIngredient recipeIngredient = new RecipeIngredient(recipeIngredientID, quantity, measureUnit, recipe, ingredient);
        
        // Ejecutar código
        recipe.setIngredients(ingredients);
        recipe.addIngredient(recipeIngredient);
    
        // Comprobar resultados
        assertAll(
                // Hay ingredientes añadidos a la receta
                () -> assertTrue(recipe.getIngredients().size() > 0),
                () -> assertTrue(recipe.getIngredients().contains(recipeIngredient)),
                // La clave primaria compuesta coincide
                () -> assertEquals(recipeIngredientID, recipeIngredient.getId()),
                () -> assertEquals(recipeID, recipeIngredientID.getRecipeID()),
                () -> assertEquals(ingredientID, recipeIngredientID.getIngredientID()),
                // Los datos son correctos
                () -> assertEquals(recipe, recipeIngredient.getRecipe()),
                () -> assertEquals(ingredient, recipeIngredient.getIngredient()),
                () -> assertEquals(measureUnit, recipeIngredient.getMeasureUnit()),
                () -> assertEquals(quantity, recipeIngredient.getQuantity()),
                // Constructor sin argumentos funciona
                () -> assertNotNull(noArgsEntity)
        );
    }
}
