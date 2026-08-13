package com.gorkemuysal.jwt;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

	private static final String SECRET_KEY = "3X5wOrq48m37GjVHV3XtSVkK4yb7RynoDZqeEdFvLX3";

	public String generateToken(UserDetails userDetails) {

		return Jwts.builder().subject(userDetails.getUsername()).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).signWith(getKey()).compact();
	}

	public <T> T exportToken(String token, Function<Claims, T> claimsFunction) {
		Claims claims = Jwts.parser().verifyWith((SecretKey) getKey()).build().parseSignedClaims(token).getPayload();

		return claimsFunction.apply(claims);

	}

	public String getUsernameByToken(String token) {
		return exportToken(token, Claims::getSubject);
	}

	public boolean isTokenExpired(String token) {
		Date expiredDate = exportToken(token, Claims::getExpiration);
		return expiredDate.before(new Date());
	}

	public SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
