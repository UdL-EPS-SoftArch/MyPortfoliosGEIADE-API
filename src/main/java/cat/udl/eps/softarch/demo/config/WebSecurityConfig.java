package cat.udl.eps.softarch.demo.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Value("${allowed-origins}")
    String[] allowedOrigins;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(HttpMethod.GET, "/identity").authenticated()

                // Users
                /*
                .requestMatchers(HttpMethod.GET, "/users").authenticated()
                .requestMatchers(HttpMethod.POST, "/users").anonymous()
                .requestMatchers(HttpMethod.GET, "/users/{username}").anonymous() 
                .requestMatchers(HttpMethod.POST, "/users/*").denyAll() */

                //Admins
                .requestMatchers(HttpMethod.GET, "/admins").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/admins").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/admins/{username}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/admins/{username}/suspend").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/admins/*").denyAll()

                //Creators
                .requestMatchers(HttpMethod.GET, "/creators").permitAll()
                .requestMatchers(HttpMethod.POST, "/creators").anonymous()
                .requestMatchers(HttpMethod.GET, "/creators/{username}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/creators/{username}").hasRole("ADMIN")  
                .requestMatchers(HttpMethod.POST, "/creators/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/creators/{username}/suspend").hasRole("ADMIN")

                //Profile
                .requestMatchers(HttpMethod.GET, "/profiles").permitAll()
                .requestMatchers(HttpMethod.POST, "/profiles").hasRole("CREATOR")
                .requestMatchers(HttpMethod.GET, "/profiles/{username}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/profiles/{username}").hasRole("CREATOR")  
                .requestMatchers(HttpMethod.POST, "/profiles/*").hasRole("ADMIN")



                .requestMatchers(HttpMethod.POST, "/*/*").authenticated()
                .requestMatchers(HttpMethod.PUT, "/*/*").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/*/*").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/*/*").authenticated()
                .anyRequest().permitAll())
            .csrf((csrf) -> csrf.disable())
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
            .httpBasic((httpBasic) -> httpBasic.realmName("demo"));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList(allowedOrigins));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
