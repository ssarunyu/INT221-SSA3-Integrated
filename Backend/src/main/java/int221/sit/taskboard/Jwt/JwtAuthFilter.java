package int221.sit.taskboard.Jwt;

import int221.sit.taskboard.exceptions.NotCreatedException;
import int221.sit.taskboard.services.JwtUserDetailsService;
import int221.sit.taskboard.exceptions.BadRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);

            if (!isValidJwtStructure(jwtToken)) {
                throw new BadRequestException("Invalid JWT Token Structure");
            }

            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }  catch (ExpiredJwtException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
                return;
            }   catch (SignatureException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT signature " + ex.getMessage());
                return;
            }   catch (MalformedJwtException ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Malformed JWT token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isValidJwtStructure(String jwtToken) {
        return jwtToken.matches("[A-Za-z0-9-]+\\.[A-Za-z0-9-]+\\.[A-Za-z0-9-_]+");
    }
}

