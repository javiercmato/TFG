package es.udc.fic.tfg.backendtfg.recipes.infrastructure;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.MeasureUnit;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static es.udc.fic.tfg.backendtfg.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RecipeDTOUnitTest {
    @Test
    void testRecipeDTO() {
        // Crear datos de prueba
        UUID recipeID = UUID.randomUUID();
        LocalDateTime creationDate = LocalDateTime.now();
        long duration = 10;
        int diners = 2;
        boolean isBannedByAdmin = false;
        UUID authorID = UUID.randomUUID();
        UUID categoryID = UUID.randomUUID();
        List<RecipeIngredientDTO> recipeIngredients = new ArrayList<>();
        List<RecipePictureDTO> recipePictures = new ArrayList<>();
        List<RecipeStepDTO> recipeSteps = new ArrayList<>();
        
        // Ejecutar código
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipeID);
        dto.setName(DEFAULT_RECIPE_NAME);
        dto.setDescription(DEFAULT_RECIPE_DESCRIPTION);
        dto.setCreationDate(creationDate);
        dto.setDuration(duration);
        dto.setDiners(diners);
        dto.setBannedByAdmin(isBannedByAdmin);
        dto.setAuthorID(authorID);
        dto.setCategoryID(categoryID);
        dto.setIngredients(recipeIngredients);
        dto.setPictures(recipePictures);
        dto.setSteps(recipeSteps);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(recipeID, dto.getId()),
                () -> assertEquals(DEFAULT_RECIPE_NAME, dto.getName()),
                () -> assertEquals(DEFAULT_RECIPE_DESCRIPTION, dto.getDescription()),
                () -> assertEquals(creationDate, dto.getCreationDate()),
                () -> assertEquals(duration, dto.getDuration()),
                () -> assertEquals(diners, dto.getDiners()),
                () -> assertEquals(isBannedByAdmin, dto.isBannedByAdmin()),
                () -> assertEquals(authorID, dto.getAuthorID()),
                () -> assertEquals(categoryID, dto.getCategoryID()),
                () -> assertEquals(recipeIngredients, dto.getIngredients()),
                () -> assertEquals(recipePictures, dto.getPictures()),
                () -> assertEquals(recipeSteps, dto.getSteps())
        );
    }
    
    @Test
    void testCreateRecipeParamsDTO() {
        // Crear datos de prueba
        UUID recipeID = UUID.randomUUID();
        long duration = 10;
        int diners = 2;
        boolean isBannedByAdmin = false;
        UUID authorID = UUID.randomUUID();
        UUID categoryID = UUID.randomUUID();
        List<CreateRecipeIngredientParamsDTO> ingredientsParams = new ArrayList<>();
        List<CreateRecipePictureParamsDTO> picturesParams = new ArrayList<>();
        List<CreateRecipeStepParamsDTO> stepsParams = new ArrayList<>();
        
        // Ejecutar código
        CreateRecipeParamsDTO dto = new CreateRecipeParamsDTO();
        dto.setName(DEFAULT_RECIPE_NAME);
        dto.setDescription(DEFAULT_RECIPE_DESCRIPTION);
        dto.setDuration(duration);
        dto.setDiners(diners);
        dto.setAuthorID(authorID);
        dto.setCategoryID(categoryID);
        dto.setIngredients(ingredientsParams);
        dto.setPictures(picturesParams);
        dto.setSteps(stepsParams);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(DEFAULT_RECIPE_NAME, dto.getName()),
                () -> assertEquals(DEFAULT_RECIPE_DESCRIPTION, dto.getDescription()),
                () -> assertEquals(duration, dto.getDuration()),
                () -> assertEquals(diners, dto.getDiners()),
                () -> assertEquals(authorID, dto.getAuthorID()),
                () -> assertEquals(categoryID, dto.getCategoryID()),
                () -> assertEquals(ingredientsParams, dto.getIngredients()),
                () -> assertEquals(picturesParams, dto.getPictures()),
                () -> assertEquals(stepsParams, dto.getSteps())
        );
    }
    
    @Test
    void testRecipeIngredientDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        String quantity = "1";
        String measureUnit = MeasureUnit.UNIDAD.toString();
        
        // Ejecutar código
        RecipeIngredientDTO dto
                = new RecipeIngredientDTO(id, DEFAULT_INGREDIENT_NAME, quantity, measureUnit);
        RecipeIngredientDTO noArgsEntity = new RecipeIngredientDTO();
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, dto.getId()),
                () -> assertEquals(DEFAULT_INGREDIENT_NAME, dto.getName()),
                () -> assertEquals(quantity, dto.getQuantity()),
                () -> assertEquals(measureUnit, dto.getMeasureUnit()),
                // Constructor sin argumentos funciona
                () -> assertNotNull(noArgsEntity)
        );
    }
    
    @Test
    void testCreateRecipeIngredientParamsDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        String quantity = "1";
        String measureUnit = MeasureUnit.UNIDAD.toString();
        
        // Ejecutar código
        CreateRecipeIngredientParamsDTO dto
                = new CreateRecipeIngredientParamsDTO(id, quantity, measureUnit);
        CreateRecipeIngredientParamsDTO noArgsEntity = new CreateRecipeIngredientParamsDTO();
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, dto.getIngredientID()),
                () -> assertEquals(quantity, dto.getQuantity()),
                () -> assertEquals(measureUnit, dto.getMeasureUnit()),
                // Constructor sin argumentos funciona
                () -> assertNotNull(noArgsEntity)
        );
    }
    
    @Test
    void testRecipePictureDTO() {
        // Crear datos de prueba
        int order = 1;
        String pictureData = "encoded image bytes";
        
        // Ejecutar código
        RecipePictureDTO dto = new RecipePictureDTO(order, pictureData);
        RecipePictureDTO noArgsEntity = new RecipePictureDTO();
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(order, dto.getOrder()),
                () -> assertEquals(pictureData, dto.getPictureData()),
                // Constructor sin argumentos funciona
                () -> assertNotNull(noArgsEntity)
        );
    }
    
    @Test
    void testCreateRecipePictureParamsDTO() {
        // Crear datos de prueba
        int order = 1;
        String pictureData = "encoded image bytes";
        
        // Ejecutar código
        CreateRecipePictureParamsDTO dto
                = new CreateRecipePictureParamsDTO(order, pictureData);
        CreateRecipePictureParamsDTO noArgsEntity = new CreateRecipePictureParamsDTO();
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(order, dto.getOrder()),
                () -> assertEquals(pictureData, dto.getData()),
                // Constructor sin argumentos funciona
                () -> assertNotNull(noArgsEntity)
        );
    }
    
    @Test
    void testRecipeStepDTO() {
        // Crear datos de prueba
        int step = 1;
        String text = "Recipe text";
    
        // Ejecutar código
        RecipeStepDTO dto = new RecipeStepDTO(step, text);
        RecipeStepDTO noArgsEntity = new RecipeStepDTO();
    
        // Comprobar resultados
        assertAll(
                () -> assertEquals(step, dto.getStep()),
                () -> assertEquals(text, dto.getText()),
                // Constructor sin argumentos funciona
                () -> assertNotNull(noArgsEntity)
        );
    }
    
    @Test
    void testCreateRecipeStepParamsDTO() {
        // Crear datos de prueba
        int step = 1;
        String text = "Recipe text";
        
        // Ejecutar código
        CreateRecipeStepParamsDTO dto
                = new CreateRecipeStepParamsDTO(step, text);
        CreateRecipeStepParamsDTO noArgsEntity = new CreateRecipeStepParamsDTO();
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(step, dto.getStep()),
                () -> assertEquals(text, dto.getText()),
                // Constructor sin argumentos funciona
                () -> assertNotNull(noArgsEntity)
        );
    }
    
}
