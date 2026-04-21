package com.loan.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	private static final long EXPIRATION_TIME = 1000 * 60 * 10; 
	
	
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
				.signWith(getKey(), SignatureAlgorithm.HS256)
				 .compact();
	}

	public String extractUsername(String token) {
		 return parse(token).getBody().getSubject();
		 }
	
	public boolean validateToken(String token) {
		 return !parse(token).getBody().getExpiration().before(new Date());
		 }

	
	 private Jws<Claims> parse(String token) {
		 return Jwts.parserBuilder()
		 .setSigningKey(getKey())
		 .build()
		 .parseClaimsJws(token);
	 }


	 private Key getKey() {
		 return Keys.hmacShaKeyFor(secret.getBytes());
		 }



}
