package net.geferon.foro.modelos.dtos;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;

import lombok.Data;
import net.geferon.foro.modelos.UsuarioVO;

@Data
public class UsuarioSafe {
	public static UsuarioSafe of(UsuarioVO usuario) {
		if (usuario == null) return null;
		return new UsuarioSafe(usuario);
	}
	private UsuarioSafe(UsuarioVO usuario) {
		this.id = usuario.getId();
		this.username = usuario.getUsername();
		this.fechaRegistro = usuario.getFechaRegistro();
		this.avatar = usuario.getAvatar();
		this.roles = usuario.getRoles().stream().map(r -> r.getNombre().toString()).collect(Collectors.toSet());
	}
	
	private Integer id;

	private String username;

	@CreatedDate
	private LocalDateTime fechaRegistro = LocalDateTime.now();
	private String avatar;
	
	private Set<String> roles = new HashSet<>();
}
