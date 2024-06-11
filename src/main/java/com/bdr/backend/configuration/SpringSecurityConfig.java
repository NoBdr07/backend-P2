package com.bdr.backend.configuration;

import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
	@Value("${jwt.secret}")
    private String jwtKey;
	
	
	
	/**
	 * Creates a CorsFilter bean to configure CORS settings.
	 *
	 * @return The configured CorsFilter.
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); 
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200")); 
		config.addAllowedHeader("*"); 
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source); 
	}

	// List of routes that do not require authentication
	private static final String[] AUTH_WHITELIST = { "/v3/api-docs/**", "/swagger-ui/**", "/api/auth/register",
			"/api/auth/login", "OPTIONS/**", "/uploads/**"};

	/**
	 * Configure the security filter chain.
	 * 
	 * @param http The HttpSecurity object to configure.
	 * @return The configured SecurityFilterChain object.
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth
						.anyRequest().authenticated())
				.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
				.build();
	}
	
	/**
	 * Configure the web security.
	 * 
	 * @return The WebSecurityCustomizer object that allows 
	 * routes from the AUTH_WHITELIST to be ignored.
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(AUTH_WHITELIST);
	}

	/**
	 * Creates a JwtDecoder bean to decode JWTs.
	 *
	 * @return The JwtDecoder bean.
	 */
	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), "HmacSHA256");
		return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
	}
	
	/**
	 * Creates a PasswordEncoder bean to encode passwords.
	 *
	 * @return The PasswordEncoder bean.
	 */
	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
	}
	
	/**
	 * Creates a PasswordEncoder bean to encode passwords.
	 *
	 * @return The PasswordEncoder bean.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
