package investment.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Add filter to check/distribute JWT token based on the authentication.
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // Add cors configuration.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Disable csrf.
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                // Allow anyone to hit any authentication endpoints.
                .authorizeHttpRequests(
                        (requests) -> requests
                                .requestMatchers("api/authentication/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                // Create session management to store JWT.
                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


    // Create password encryption algorithm.
    @Bean
    public MessageDigest messageDigest() throws Exception {
        return MessageDigest.getInstance("MD5");
    }

    @Bean
    public SecureRandom secureRandom() throws Exception {
        return new SecureRandom();
    }

    // Create Custom Authentication manager using the custom userDetailsService and Password Encoder
    @Bean
    public AuthenticationManager authenticationManager() {
        return new CustomAuthenticationProviderService(userDetailsService(), passwordEncoder());
    }

    @Bean
    public CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean CustomPasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    // Configure cors.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081")); // Allow UI origin
        configuration.setAllowedMethods(Arrays.asList("*")); // Allow all methods
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all header
        configuration.setAllowCredentials(true); // Allow set credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Register configuration on all routes.

        return source;
    }
}
