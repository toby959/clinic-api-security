package com.toby959.api.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true)  -- ejercicio video --
@EnableGlobalMethodSecurity(prePostEnabled = true) // -- chat --
//Habilita @PreAuthorize y @PostAuthorize          // -- chat --
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;

    public SecurityConfiguration(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

//    @Bean                       --- deprecated ----
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.csrf().disable().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().build();
//    }

//#############################################################################################
/*
    @Bean   // --  Authorization version for all  --
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(c -> c.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .anyRequest().authenticated()))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
}
 */


//#############################################
//    @Bean               -- video --
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
//                        .requestMatchers(HttpMethod.DELETE, "/doctors").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/patients").hasRole("ADMIN")
//                        .anyRequest().authenticated())
//                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }

//#####################################################  Two #############################
/*    @Bean      // mio --
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll() // Permitir acceso a login
                        .requestMatchers(HttpMethod.POST, "/api/v1/doctors/register").hasRole("ROL_ADMIN") // Crear doctor
                      //  .requestMatchers(HttpMethod.PUT, "/api/v1/doctors/{id}").hasRole("ADMIN") // Actualizar doctor
                      //  .requestMatchers(HttpMethod.DELETE, "/api/v1/doctors/{id}").hasRole("ADMIN") // Eliminar doctor
                     //   .requestMatchers(HttpMethod.GET, "/api/v1/doctors").hasAnyRole("ADMIN", "USER") // Obtener doctores
                        .anyRequest().authenticated()) // Cualquier otra solicitud requiere autenticación
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
 */
//#####################################################  Two #############################
//---------------------------------------  chat   -------------------------------------------------
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs stateless
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Deshabilitar sesiones
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST, "/login").permitAll() // Permitir acceso al login
                    .requestMatchers("/css/**", "/js/**", "/img/**", "/").permitAll() // Permitir acceso a recursos estáticos y al frontend
                    .requestMatchers(HttpMethod.POST, "/user/register").permitAll() // Permitir registro de usuarios
                    .requestMatchers("/swagger-ui/**").permitAll() // Permitir acceso a Swagger
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/swagger-resources/**", "/webjars/**").permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Añadir filtro de seguridad personalizado
            .formLogin(AbstractHttpConfigurer::disable); // Deshabilitar el manejo de formularios de login

    return http.build();
}


//----------------------------------------------------------------------------------------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
