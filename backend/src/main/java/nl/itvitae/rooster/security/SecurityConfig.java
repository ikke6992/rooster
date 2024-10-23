package nl.itvitae.rooster.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  public static final String ROLE_ADMIN = "ADMIN";

  private static final String[] ADMIN_ONLY = {"api/v1/fields/**", "api/v1/groups/**", "api/v1/teachers/**"};
  private static final String[] ADMIN_ONLY_POST = {"api/v1/freedays/**"};
  private static final String[] ADMIN_ONLY_DELETE = {"api/v1/freedays/**"};
  private static final String[] ADMIN_ONLY_PUT = {"api/v1/scheduleddays/**"};

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(CorsConfigurer::disable)
        .csrf(CsrfConfigurer::disable)
        .authorizeHttpRequests(requests -> requests
            .requestMatchers(HttpMethod.POST, ADMIN_ONLY_POST).hasRole(ROLE_ADMIN)
            .requestMatchers(HttpMethod.DELETE, ADMIN_ONLY_DELETE).hasRole(ROLE_ADMIN)
            .requestMatchers(HttpMethod.PUT, ADMIN_ONLY_PUT).hasRole(ROLE_ADMIN)
            .requestMatchers(ADMIN_ONLY).hasRole(ROLE_ADMIN)
            .anyRequest().permitAll())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}

