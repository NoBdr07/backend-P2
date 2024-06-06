package com.bdr.backend.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private JwtEncoder jwtEncoder;

	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
	private String jwtKey = "clesecretedaumoins16characteres.";

	public JwtService(JwtEncoder jwtEncoder) {
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

			logger.info("Generated JWT: {}", token);
			logger.info("Secret key: {}", jwtKey);

			return token;
		} catch (Exception e) {
			logger.error("Error generating token", e);
			throw e;
		}
	}
}
