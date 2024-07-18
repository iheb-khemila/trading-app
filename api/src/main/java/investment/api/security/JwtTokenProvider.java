package investment.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(Authentication authentication) {
        // Get the users username
        String username = authentication.getName();

        // Get the current date.
        Date currentDate = new Date();

        // Create an expiry date.
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        // Build a JWT and return.
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt( new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return token;
    }
    public String getUsernameFromJWT(String token){

        // Get the claims from the JWT
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Return the username which is stored in the subject.
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            // Try to parse the claims
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // Handle any exceptions by throwing AuthenticationCredentialsNotFoundException as JWT is expired or invalid.
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or invalid.", ex.fillInStackTrace());
        }
    }

}
