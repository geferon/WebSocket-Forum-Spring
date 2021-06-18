package net.geferon.foro.controladores;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.geferon.foro.helpers.ImageUtils;
import net.geferon.foro.helpers.SeguridadControladores;
import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.modelos.dtos.UsuarioDTO;
import net.geferon.foro.modelos.dtos.UsuarioSafe;
import net.geferon.foro.repositorios.servicios.ServicioUsuario;
import net.geferon.foro.servicios.AlmacenamientoServicio;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
	@Autowired
	private ServicioUsuario servUsuario;
	
	@Autowired
	private AlmacenamientoServicio almacenamiento;
	
	@Autowired
	private SeguridadControladores seguridad;
	
	@GetMapping("{id}")
	public ResponseEntity<UsuarioSafe> get(@PathVariable() Integer id) {
		return ResponseEntity.of(
				servUsuario.findById(id)
				.map(u -> UsuarioSafe.of(u))
			);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("{id}")
	public ResponseEntity<UsuarioSafe> post(
			@PathVariable() Integer id, @ModelAttribute UsuarioDTO usuario,
			@RequestParam(value="avatar", required=false) MultipartFile avatar,
			Authentication authentication
		) {
		Optional<UsuarioVO> usuarioDB = servUsuario.findById(id);
		
		if (usuarioDB.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		UsuarioVO usuarioDefinitive = usuarioDB.get();
		
		// Verificamos que el usuario tiene permisos para editar el perfil
		if (!seguridad.canEditUser(usuarioDefinitive, authentication)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		// Aplicamos los cambios
		BeanUtils.copyProperties(usuario, usuarioDefinitive);
		
		// Realizamos el cambio de foto de perfil
		
		if (avatar != null) {
			ImageUtils.ResultImage result = null;
			try {
				result = ImageUtils.squareImage(avatar.getInputStream());
			} catch (Exception err) {
				err.printStackTrace();
				return ResponseEntity.badRequest().build();
			}

			// Verificamos que la imagen sea valida
			String formatName = result.getFormat();
			
			// Borramos la anterior y guardamos la nueva
			String localizacionFoto = "perfil/" + usuarioDefinitive.getId() + "_" + UUID.randomUUID().toString() + "." + formatName;
			if (!usuarioDefinitive.getAvatar().equals(UsuarioVO.DEFAULT_PICTURE)) {
				almacenamiento.delete(usuarioDefinitive.getAvatar());
			}

			try {
				almacenamiento.save(new ByteArrayInputStream(result.getOs().toByteArray()), localizacionFoto);
				usuarioDefinitive.setAvatar(localizacionFoto);
			} catch (Exception e) {
				return ResponseEntity.badRequest().build();
			}
		}
		
		// Guardamos todo
		servUsuario.save(usuarioDefinitive);
		
		return ResponseEntity.ok(UsuarioSafe.of(usuarioDefinitive));
	}
}
