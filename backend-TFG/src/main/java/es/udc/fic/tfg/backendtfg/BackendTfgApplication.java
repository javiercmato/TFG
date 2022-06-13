package es.udc.fic.tfg.backendtfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BackendTfgApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BackendTfgApplication.class, args);
    }
    
    /* ******************** BEANS GLOBALES ******************** */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
        bean.setBasename("classpath:i18n/messages");
        bean.setDefaultEncoding("UTF-8");
        
        return bean;
    }
    
}
