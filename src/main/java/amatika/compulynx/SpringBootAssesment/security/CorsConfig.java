package amatika.compulynx.SpringBootAssesment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true); // Allow credentials such as cookies, authorization headers
        config.addAllowedOrigin("http://localhost:4200"); // Allow specific origin
        config.addAllowedHeader("*"); // Allow any headers (e.g., Content-Type, Authorization)
        config.addAllowedMethod("*"); // Allow any HTTP method (GET, POST, etc.)
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
