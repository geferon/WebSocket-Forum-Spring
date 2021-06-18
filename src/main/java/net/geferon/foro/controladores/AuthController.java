package net.geferon.foro.controladores;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.impl.DefaultClaims;
import net.geferon.foro.helpers.JwtUtils;
import net.geferon.foro.modelos.ERol;
import net.geferon.foro.modelos.RolVO;
import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.modelos.dtos.NewPasswordDTO;
import net.geferon.foro.repositorios.servicios.ServicioRol;
import net.geferon.foro.repositorios.servicios.ServicioUsuario;
import net.geferon.foro.respuestas.JwtRegisterResponse;
import net.geferon.foro.respuestas.JwtResponse;
import net.geferon.foro.respuestas.LoginFormulario;
import net.geferon.foro.respuestas.MessageResponse;
import net.geferon.foro.respuestas.RegistroFormulario;
import net.geferon.foro.servicios.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
public class AuthController {
	@Autowired
	DaoAuthenticationProvider authenticationManager;
	//AuthenticationManager authenticationManager;

	@Autowired
	ServicioUsuario servUsuarios;

	@Autowired
	ServicioRol servRoles;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginFormulario loginPeticion) throws Exception {
		Authentication authentication = null;
		
		try {
			authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginPeticion.getUsername(), loginPeticion.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String jwt = jwtUtils.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegistroFormulario registroPeticion) {
		if (servUsuarios.existsByUsername(registroPeticion.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Este usuario ya esta en uso!"));
		}

		if (servUsuarios.existsByEmail(registroPeticion.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Este correo ya esta en uso!"));
		}

		// Create new user's account
		UsuarioVO user = new UsuarioVO(0, registroPeticion.getUsername(), 
							 registroPeticion.getEmail(),
							 encoder.encode(registroPeticion.getPassword()));

		Set<RolVO> roles = new HashSet<>();

		RolVO userRole = servRoles.findByNombre(ERol.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
		roles.add(userRole);

		user.setRoles(roles);
		servUsuarios.save(user);
		
		// Ahora hacemos el proceso de autenticar
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(registroPeticion.getUsername(), registroPeticion.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String jwt = jwtUtils.generateToken(userDetails);

		return ResponseEntity.ok(new JwtRegisterResponse(jwt, "Usuario registrado satisfactoriamente!"));
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, Authentication authentication) throws Exception {
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
		//String newToken = jwtUtils.doGenerateRefreshToken(claims, claims.get("sub", String.class));
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		UsuarioVO usuarioTemp = userDetails.getUsuario();
		UsuarioVO usuario = servUsuarios.findById(usuarioTemp.getId()).get();

		if (
			usuario.getFechaPasswordChanged() != null &&
			usuario.getFechaPasswordChanged()
				.atZone(ZoneId.systemDefault()).toInstant()
				.isAfter(claims.getIssuedAt().toInstant())
		) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String newToken = jwtUtils.doGenerateRefreshToken(
				jwtUtils.generateClaims(userDetails),
				authentication.getName()
			);

		return ResponseEntity.ok(new JwtResponse(newToken));
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/password")
	public ResponseEntity<?> changePassword(@RequestBody NewPasswordDTO passwordForm, Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		UsuarioVO usuario = userDetails.getUsuario();
		
		if (!encoder.matches(passwordForm.getOldPassword(), usuario.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		usuario.setPassword(encoder.encode(passwordForm.getNewPassword()));
		servUsuarios.save(usuario);
		
		return ResponseEntity.ok().build();
	}
}
