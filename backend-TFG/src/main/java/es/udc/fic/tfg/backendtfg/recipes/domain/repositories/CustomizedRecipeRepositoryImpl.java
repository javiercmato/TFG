package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import org.springframework.data.domain.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

public class CustomizedRecipeRepositoryImpl implements CustomizedRecipeRepository {
    @PersistenceContext
    private EntityManager em;
    
    @SuppressWarnings("unchecked")
    @Override
    public Slice<Recipe> findByCriteria(String name, UUID categoryID, List<UUID> ingredientsID, int page, int pageSize) {
        String[] nameWords = getWords(name);
        StringBuilder queryStringBuilder = new StringBuilder();
        
        queryStringBuilder.append("SELECT DISTINCT recipe FROM Recipe recipe");
        // Une recetas con las entidades relacionadas para obtener sus datos
        queryStringBuilder.append(" LEFT JOIN FETCH recipe.ingredients recipeIngredients");
        queryStringBuilder.append(" LEFT JOIN FETCH recipeIngredients.ingredient ingredient");
        queryStringBuilder.append(" LEFT JOIN FETCH recipe.pictures");
        
        /* [1] CONSTRUIR CONSULTA */
        boolean hasName = (nameWords.length != 0 );
        boolean hasCategory = (categoryID != null);
        boolean hasIngredients = (ingredientsID != null);
        boolean hasCriteria = hasName || hasCategory || hasIngredients;
        
        /* Si hay algún criterio, los añade a la consulta. Sino recupera todas las recetas */
        if (hasCriteria) { queryStringBuilder.append(" WHERE "); }
        
        /* Añade aquellos criterios que haya recibido */
        if (hasCategory) {
            queryStringBuilder.append("recipe.category.id = :categoryID");
        }
        
        if (hasName) {
            // Si hay algún criterio previo, une la busqueda del nombre con un AND
            if (hasCategory) { queryStringBuilder.append(" AND "); }
            for (int i=0; i < nameWords.length-1; i++) {
                queryStringBuilder.append("LOWER(recipe.name) LIKE LOWER(:word" + i + ") AND ");
            }
    
            queryStringBuilder.append("LOWER(recipe.name) LIKE LOWER(:word" + (nameWords.length-1) + ")");
        }
        
        if (hasIngredients) {
            // Si hay algún criterio previo, une la búsqueda de los ingredientes con un AND
            if (hasCategory || hasName) { queryStringBuilder.append(" AND "); }
                queryStringBuilder.append("(ingredient.id IN :ingredientIDs)");
        }
    
        queryStringBuilder.append(" ORDER BY recipe.creationDate DESC");
        String queryString = queryStringBuilder.toString();
        
        /* [2] SUSTITUIR PARÁMETROS */
        int firstResultPosition = page*pageSize;
        Query query = em.createQuery(queryString)
                        .setFirstResult(firstResultPosition)
                        .setMaxResults(pageSize+1);             // Tamaño de página + 1 para saber si hay más elementos
        if (hasCategory) { query.setParameter("categoryID", categoryID); }
        if (hasName) {
            for (int i=0; i < nameWords.length; i++) {
                query.setParameter("word" + i, "%" + nameWords[i] + "%");
            }
        }
        if (hasIngredients) { query.setParameter("ingredientIDs", ingredientsID); }
        
        /* [3] EJECUTAR CONSULTA Y OBTENER RESULTADOS */
        List<Recipe> results = query.getResultList();
        boolean hasNextPage = results.size() == (pageSize + 1);
        if (hasNextPage) { results.remove(results.size() - 1); }
        
        return new SliceImpl<>(results, PageRequest.of(page, pageSize), hasNextPage);
    }
    
    
    @Override
    public Slice<Recipe> findByAuthor(UUID userID, int page, int pageSize) {
    
        String queryString = "SELECT DISTINCT recipe FROM Recipe recipe" +
                // Une recetas con las entidades relacionadas para obtener sus datos
                " LEFT JOIN FETCH recipe.ingredients recipeIngredients" +
                " LEFT JOIN FETCH recipeIngredients.ingredient ingredient" +
                " LEFT JOIN FETCH recipe.pictures" +
            
                /* [1] CONSTRUIR CONSULTA */
                " WHERE recipe.author.id = :authorID" +
                " ORDER BY recipe.creationDate DESC";
    
        /* [2] SUSTITUIR PARÁMETROS */
        int firstResultPosition = page*pageSize;
        Query query = em.createQuery(queryString)
                        .setFirstResult(firstResultPosition)
                        .setMaxResults(pageSize+1);             // Tamaño de página + 1 para saber si hay más elementos
        query.setParameter("authorID", userID);
        
        /* [3] EJECUTAR CONSULTA Y OBTENER RESULTADOS */
        List<Recipe> results = query.getResultList();
        boolean hasNextPage = results.size() == (pageSize + 1);
        if (hasNextPage) { results.remove(results.size() - 1); }
    
        return new SliceImpl<>(results, PageRequest.of(page, pageSize), hasNextPage);
    }
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    private String[] getWords(String keywords) {
        if (keywords == null || keywords.length() == 0) {
            return new String[0];
        } else {
            return keywords.split("\\s");
        }
        
    }

}
