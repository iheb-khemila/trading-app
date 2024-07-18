package investment.api.security;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import investment.api.dtos.UserDto;

public class CustomAuthenticationProviderService implements AuthenticationManager {

    private final CustomUserDetailsService customUserDetailsService;

    private final CustomPasswordEncoder passwordEncoder;

    public CustomAuthenticationProviderService(CustomUserDetailsService customUserDetailsService, CustomPasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // Create a userDTO object.
        UserDto user;

        try {
            // Load the user using the CustomUserDetailsService.
            user = customUserDetailsService.loadUserByUsername(authentication.getName());
        } catch (Exception e) {
            // Throw BadCredentialsException if the user can't be loaded by Username.
            throw new BadCredentialsException("Invalid Username or Password");
        }

        // Hash the password provided using the same salt the users password was hashed with.
        var hashedPassword = passwordEncoder.hashPassword(authentication.getCredentials().toString(), Optional.ofNullable(user.getSalt()));

        // Check if the hashed password provided matches the users hashed password.
        if(!passwordEncoder.matches(user.getPasswordHash(), hashedPassword.getHashedPassword())) {
            // Throw BadCredentialsException if the user can't be loaded by Username.
            throw new BadCredentialsException("Invalid Username or Password");
        }

        // Return UsernamePasswordAuthenticationToken so that the user can be authenticated.
        return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials().toString());
    }
}
