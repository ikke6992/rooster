package nl.itvitae.rooster.security;

import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.user.MyUserDetailsService;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final MyUserDetailsService userDetailsService;
  private final JwtAuthenticationFilter authFilter;

  public static final String ROLE_ADMIN = "ADMIN";

  private static final String[] ADMIN_ONLY = {"api/v1/fields/**", "api/v1/groups",
      "api/v1/teachers/**"};
  private static final String[] ADMIN_ONLY_POST = {"api/v1/freedays/**"};
  private static final String[] ADMIN_ONLY_DELETE = {"api/v1/freedays/**"};
  private static final String[] ADMIN_ONLY_PUT = {"api/v1/scheduleddays/**"};

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(CorsConfigurer::disable)
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

