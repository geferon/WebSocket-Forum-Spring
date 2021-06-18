package net.geferon.foro.modelos.dtos;

import java.time.LocalDateTime;

import lombok.Data;
import net.geferon.foro.modelos.HiloVO;

@Data
public class HiloPerfilSafe {
	public static HiloPerfilSafe of(HiloVO hilo) {
		if (hilo == null) return null;
		return new HiloPerfilSafe(hilo);
	}
	
	private HiloPerfilSafe(HiloVO hilo) {
		//this.id = hilo.getId();
		this.id = hilo.getId();
		this.titulo = hilo.getTitulo();
		this.fechaCreacion = hilo.getFechaCreacion();
		this.creador = UsuarioSafe.of(hilo.getCreador());
		this.categoria = CategoriaPerfilSafe.of(hilo.getCategoria());
	}
	private Long id;
	private String titulo;
	private LocalDateTime fechaCreacion = LocalDateTime.now();
	private UsuarioSafe creador;
	private CategoriaPerfilSafe categoria;
}
