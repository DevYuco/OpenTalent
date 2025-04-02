package opentalent.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import opentalent.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Necesario para @PreAuthorize
public class SecurityConfig{

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/auth/**").permitAll() // login, registro, refresh
            	    .requestMatchers("/public/**").permitAll() // contenido para invitados
            	    .requestMatchers("/admin/**").hasRole("ADMIN") //Contenido para rol admin
            	    .requestMatchers("/empresa/**").hasRole("EMPRESA")//Contenido para rol empresa
            	    .requestMatchers("/usuario/**").hasRole("USER")//Contenido para rol user
            	    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}