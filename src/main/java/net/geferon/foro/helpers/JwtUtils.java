package net.geferon.foro.helpers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.servicios.UserDetailsImpl;

@Component
public class JwtUtils {
	@Value("${geferon.app.jwtSecret}")
	private String jwtSecret;

	@Value("${geferon.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${geferon.app.jwtRefreshExpirationMs}")
	private int jwtRefreshExpirationMs;
	
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}
	
	public Map<String, Object> generateClaims(UserDetailsImpl userDetails) {
		Map<String, Object> claims = new HashMap<>();
		UsuarioVO usuario = userDetails.getUsuario();
		claims.put("id", usuario.getId());
		claims.put("username", usuario.getUsername());
		claims.put("roles", usuario.getRoles().stream().map(item -> item.getNombre()).collect(Collectors.toList()));
		claims.put("avatar", usuario.getAvatar());
		claims.put("email", usuario.getEmail());
		return claims;
	}
	

	public String generateToken(UserDetailsImpl userDetails) {
		return doGenerateToken(generateClaims(userDetails), userDetails.getUsername());
	}
	
	/*
	public String generateRefreshToken(UserDetailsImpl userDetails) {
		return doGenerateRefreshToken(generateClaims(userDetails), userDetails.getUsername());
	}
	*/
	
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
