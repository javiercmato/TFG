package es.udc.fic.tfg.backendtfg.common.infrastructure.security;

import es.udc.fic.tfg.backendtfg.common.application.JwtGenerator;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    
    /* Cómo obtener el AuthenticationManager: https://stackoverflow.com/a/71449312/11295728 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtGenerator jwtGenerator) throws Exception {
        // Crear un filtro que procese el JWT que hemos creado
        JwtHttpConfigurer jwtConfigurer = new JwtHttpConfigurer(jwtGenerator);
        
        http
                // Desactivar CSRF porque no usamos
                .cors().and().csrf().disable()
                // No guardar datos de la sesión del usuario
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // Aplicar filtro creado para poder usar JWT
                .and().apply(jwtConfigurer)
                // Permitir las peticiones que indiquemos
                .and().authorizeHttpRequests()
                // USER ENDPOINTS
                .antMatchers(HttpMethod.GET,    "/api/users/*").permitAll()                                             // findUserById, findUserByNickname
                .antMatchers(HttpMethod.POST,   "/api/users/register").permitAll()                                      // signUp
                .antMatchers(HttpMethod.POST,   "/api/users/login").permitAll()                                         // login
                .antMatchers(HttpMethod.POST,   "/api/users/login/token").permitAll()                                   // loginFromToken
                .antMatchers(HttpMethod.PUT,    "/api/users/*/changePassword").permitAll()                              // changePassword
                .antMatchers(HttpMethod.DELETE, "/api/users/*").permitAll()                                             // deleteUser
                .antMatchers(HttpMethod.PUT,    "/api/users/*").permitAll()                                             // updateProfile
                .antMatchers(HttpMethod.PUT,    "/api/users/admin/ban/*").hasRole(UserRole.ADMIN.toString())            // banUserAsAdmin
                .antMatchers(HttpMethod.POST,   "/api/users/*/lists").permitAll()                                       // createPrivateList
                .antMatchers(HttpMethod.GET,    "/api/users/*/lists").permitAll()                                       // getPrivateListsByUser
                .antMatchers(HttpMethod.GET,    "/api/users/*/lists/*").permitAll()                                     // getPrivateListDetails
                .antMatchers(HttpMethod.POST,   "/api/users/*/lists/*/add/*").permitAll()                               // addRecipeToPrivateList
                .antMatchers(HttpMethod.DELETE, "/api/users/*/lists/*/remove/*").permitAll()                            // removeRecipeFromPrivateList
                .antMatchers(HttpMethod.DELETE, "/api/users/*/lists/*").permitAll()                                     // deletePrivateList
                // INGREDIENT ENDPOINTS
                .antMatchers(HttpMethod.POST,   "/api/ingredients/").permitAll()                                        // createIngredient
                .antMatchers(HttpMethod.GET,    "/api/ingredients/").permitAll()                                        // findAllIngredients
                .antMatchers(HttpMethod.GET,    "/api/ingredients/find").permitAll()                                    // findIngredientsByName, findIngredientsByType
                .antMatchers(HttpMethod.POST,   "/api/ingredients/types").permitAll()                                   // createIngredientTypeAsAdmin
                .antMatchers(HttpMethod.GET,    "/api/ingredients/types").permitAll()                                   // getAllIngredientTypes
                .antMatchers(HttpMethod.GET,    "/api/ingredients/measures").permitAll()                                // getAllMeasureUnits
                // RECIPE ENDPOINTS
                .antMatchers(HttpMethod.POST,   "/api/recipes/categories").permitAll()                                  // createCategoryAsAdmin
                .antMatchers(HttpMethod.GET,    "/api/recipes/categories").permitAll()                                  // getAllCategories
                .antMatchers(HttpMethod.POST,   "/api/recipes/").permitAll()                                            // createRecipe
                .antMatchers(HttpMethod.GET,    "/api/recipes/*").permitAll()                                           // getRecipeDetails
                .antMatchers(HttpMethod.GET,    "/api/recipes/find/").permitAll()                                       // findRecipes
                .antMatchers(HttpMethod.DELETE, "/api/recipes/*").permitAll()                                           // deleteRecipe
                .antMatchers(HttpMethod.PUT,    "/api/recipes/admin/ban/*").hasRole(UserRole.ADMIN.toString())          // banRecipeAsAdmin
                // SOCIAL ENDPOINTS
                .antMatchers(HttpMethod.POST,   "/api/social/comments/*").permitAll()                                   // addComment
                .antMatchers(HttpMethod.GET,    "/api/social/comments/*").permitAll()                                   // getRecipeComments
                .antMatchers(HttpMethod.PUT,    "/api/social/comments/admin/ban/*").permitAll()                         // banCommentAsAdmin
                .antMatchers(HttpMethod.POST,   "/api/social/rate/*").permitAll()                                       // rateRecipe
                .antMatchers(HttpMethod.PUT,    "/api/social/follow/*/*").permitAll()                                   // followUser
                
        
        
                // DENEGAR EL RESTO DE PETICIONES
                .anyRequest().denyAll();
        
        return http.build();
    }
    

    /**
     * Configuración de seguridad para permitir peticiones CORS.
     * También controla qué tipo de contenido aceptar en las peticiones (origen, cabeceras, método HTTP, etc)
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.addAllowedOrigin("*");            // Permite peticiones de cualquier origen
        config.addAllowedMethod("*");            // Permite peticiones con cualquier verbo HTTP
        config.addAllowedHeader("*");            // Permite peticiones con cualquier cabecera
        
        // Aplica la configuración a todas las URL expuestas por el servicio
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }

}
