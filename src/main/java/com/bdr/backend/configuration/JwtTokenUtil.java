package com.bdr.backend.configuration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenUtil {
	
	// Get the secret key from the environment variables
	@Value("${jwt_secret}")
	private String jwtKey;

	private JwtEncoder jwtEncoder;

	public JwtTokenUtil(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	public String generateToken(String email) {
		try {
			Instant now = Instant.now();
			JwtClaimsSet claims = JwtClaimsSet.builder().issuer("self").subject(email).claim("login", email)
					.issuedAt(now).expiresAt(now.plus(1, ChronoUnit.DAYS)).build();
			JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
					.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

			String token = this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

			return token;
		} catch (Exception e) {
			throw e;
		}
	}
}
