package com.example.chrombit.filter;

import com.example.chrombit.service.JpaUserDetailsService;
import com.example.chrombit.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // get the jw token from request header
        // validate jwt token
        String bearerToken = request.getHeader("Authorization");
        String username = null;
        String token = null;

        // check if token exists or has bearer text
        if(bearerToken != null && bearerToken.startsWith("Bearer")) {
            // extract jwt token from bearerToken
            token = bearerToken.substring(7);

            try {
                // extract username from the token
                username = jwtUtil.extractUsername(token);

                // get userDetails for this user
                UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(username);

                // security checks
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(upat);
                } else {
                    System.out.println("Invalid Token");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid Bearer Token Format");
        }

        // if all is well, forward the filter to the request endpoint
        filterChain.doFilter(request, response);
    }
}