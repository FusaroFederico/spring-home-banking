package com.banking.home.spring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    	
			this.jwtService = jwtService;
			this.userDetailsService = userDetailsService;
	}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
    	
    	// se la richiesta è per una risorsa statica non applica il filtro jwt
    	String requestPath = request.getRequestURI();

    	if (requestPath.endsWith(".html") || requestPath.endsWith(".css") || requestPath.endsWith(".js")
    	        || requestPath.endsWith(".png") || requestPath.endsWith(".jpg")
    	        || requestPath.equals("/") || requestPath.equals("/favicon.ico")) {
    	    filterChain.doFilter(request, response);
    	    return;
    	}
    	
    	// Estraiamo l'header Authorization
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        	// Se l'header non è presente o non inizia con "Bearer ", passiamo al prossimo filtro
        	filterChain.doFilter(request, response);
            return;
        }
        
        	// Estrae il token JWT
            final String token = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(token);
            
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // Verifica la validità del token
                if (jwtService.isTokenValid(token, userDetails)) {
                	// Crea un oggetto Authentication e lo imposta nel contesto di sicurezza
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            		userDetails, null, userDetails.getAuthorities());
                    
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            // prosegue la catena dei filtri
            filterChain.doFilter(request, response);
        
    }
}