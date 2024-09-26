package com.app.security;
 
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class); // Correct logger initialization

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Get Authorization header
		String requestHeader = request.getHeader("Authorization");

		// Logging header value
		logger.info("Authorization Header: {}", requestHeader);

		String username = null;
		String token = null;

		// Check if header contains Bearer token
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			token = requestHeader.substring(7); // Extract the token

			try {
				username = this.jwtHelper.getUsernameFromToken(token); // Retrieve username from token
			} catch (IllegalArgumentException e) {
				logger.error("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.warn("JWT Token has expired");
			} catch (MalformedJwtException e) {
				logger.error("Invalid JWT token");
			} catch (Exception e) {
				logger.error("Error occurred while parsing JWT token", e);
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		// Once we get the token, validate it
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			// If token is valid, set authentication
			if (this.jwtHelper.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Set authentication in the context
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				logger.warn("JWT Token validation failed");
			}
		}

		// Continue the filter chain
		filterChain.doFilter(request, response);
	}
}
