package com.toby959.api.infra.security;

import com.toby959.api.domain.users.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private TokenService service;
    private IUserRepository userRepository;

    public SecurityFilter(TokenService service, IUserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }
/*
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            System.out.println("El filtro esta siendo llamado");
            // token header
            var token = request.getHeader("Authorization");
    // Mod        if (token == null || token == "") {
    // Mod            throw new RuntimeException("El token enviado no es válido.");
            if(token != null) {
                token = token.replace("Bearer ", "");
                System.out.println(token);
                System.out.println(service.getSubject(token));
            }
            filterChain.doFilter(request, response);
        }
 */

//#######################################--  runn  --##############################
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("El filtro esta siendo llamado");

        var authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");
            var subject = service.getSubject(token);
            System.out.println(token);                        //  data token
            System.out.println(service.getSubject(token));    //  data user
            if (subject != null) {
                var user = userRepository.findByLogin(subject);
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

//#####################################################################
/*    @Override   -- prueba --
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("El filtro está siendo llamado");

        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.replace("Bearer ", "");
            var subject = service.getSubject(token);
            System.out.println(token);                        // data token
            System.out.println(subject);                      // data user

            if (subject != null) {
                var user = userRepository.findByLogin(subject);
                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("Usuario no encontrado: " + subject);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
                    return;
                }
            } else {
                System.out.println("Token inválido o expirado");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
 */
}

