package com.loan.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
	    String path = request.getServletPath();
	    return path.startsWith("/swagger-ui")
	        || path.startsWith("/v3/api-docs");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		
		if (request.getServletPath().equals("/api/auth/login")) {
		    filterChain.doFilter(request, response);
		    return;
		}

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			String username = jwtUtil.extractUsername(token);

			if (SecurityContextHolder.getContext().getAuthentication() == null && jwtUtil.validateToken(token)) {

				UserDetails user = userDetailsService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
						user.getAuthorities());

				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(auth);

			}

		}

		filterChain.doFilter(request, response);
	}

}
