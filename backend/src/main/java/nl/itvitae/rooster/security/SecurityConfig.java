package nl.itvitae.rooster.security;

import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.user.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final MyUserDetailsService userDetailsService;
  private final JwtAuthenticationFilter authFilter;

  @Value("${app.frontend.url}")
  private String frontendUrl;

  public static final String ROLE_ADMIN = "ADMIN";

  private static final String[] ADMIN_ONLY = {"api/v1/fields/**", "api/v1/groups/**",
      "api/v1/teachers/**", "api/v1/lessons/**"};
  private static final String[] ADMIN_ONLY_POST = {"api/v1/freedays/**"};
  private static final String[] ADMIN_ONLY_DELETE = {"api/v1/freedays/**"};
  private static final String[] ADMIN_ONLY_PUT = {"api/v1/scheduleddays/**"};

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(CsrfConfigurer::disable)
        .authorizeHttpRequests(requests -> requests
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(HttpMethod.POST, ADMIN_ONLY_POST).hasRole(ROLE_ADMIN)
            .requestMatchers(HttpMethod.DELETE, ADMIN_ONLY_DELETE).hasRole(ROLE_ADMIN)
            .requestMatchers(HttpMethod.PUT, ADMIN_ONLY_PUT).hasRole(ROLE_ADMIN)
            .requestMatchers(ADMIN_ONLY).hasRole(ROLE_ADMIN)
            .anyRequest().permitAll())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authProvider())
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // Allow specific frontend origins
    config.setAllowedOrigins(Arrays.asList(frontendUrl));

    // Allow standard HTTP methods
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

    // Allow common headers
    config.setAllowedHeaders(Arrays.asList("*"));

    // Allow sending credentials (if needed)
    config.setAllowCredentials(true);

    // Register the config for all endpoints
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();

  }

  @Bean
  public AuthenticationProvider authProvider() {
    var authprovider = new DaoAuthenticationProvider();
    authprovider.setUserDetailsService(userDetailsService);
    authprovider.setPasswordEncoder(passwordEncoder());
    return authprovider;
  }
}

