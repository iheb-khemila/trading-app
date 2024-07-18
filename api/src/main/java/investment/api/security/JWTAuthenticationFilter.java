package investment.api.security;

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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenGenerator;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getJWTFromRequest(request);
        // If the user has a token and it's valid.
        if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            // Get the username from the JWT.
            String username = tokenGenerator.getUsernameFromJWT(token);

            // Load the user by username.
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Create an authentication token.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());

            // Set session authentication.
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }


    private String getJWTFromRequest(HttpServletRequest request) {
        // Get the bearer token from the Authorization header.
        String bearerToken = request.getHeader("Authorization");

        // Check it has a valid and begins with Bearer.
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Remove the Bearer part and return JWT.
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
