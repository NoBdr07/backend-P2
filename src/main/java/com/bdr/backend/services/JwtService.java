package com.bdr.backend.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.bdr.backend.models.entities.User;
import com.bdr.backend.servicesImpl.UserServiceImpl;

@Service
public class JwtService {
	
	/** Get the secret key from the environment variable */
	@Value("${jwt_secret}")
	private String jwtKey;

	@Autowired
	private JwtEncoder jwtEncoder;
	
	@Autowired
	private UserServiceImpl userService;

	/**
	 * Generate a token from the user email
	 *
	 * @param email The user email
	 * @return a String token
	 */
	public String generateToken(String email) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .subject(email)
                .claim("login", email)
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS)).build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

	/**
	 * Get the user ID from the token
	 * @return An integer of user id
	 */
	public Integer getUserIdFromToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String login = jwt.getClaim("login");

		User user = userService.getUserByEmail(login).get();
		return userService.convertToDto(user).getUserId();
	}
}
