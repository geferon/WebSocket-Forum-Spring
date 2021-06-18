package net.geferon.foro.modelos.dtos;

import java.time.LocalDateTime;

import lombok.Data;
import net.geferon.foro.modelos.HiloVO;

@Data
public class HiloSafe {
	public static HiloSafe of(HiloVO hilo) {
		if (hilo == null) return null;
		return new HiloSafe(hilo);
	}
	
	public HiloSafe(HiloVO hilo, Long id) {
		this(hilo);
	}
	private HiloSafe(HiloVO hilo) {
		//this.id = hilo.getId();
		this.id = hilo.getId();
		this.titulo = hilo.getTitulo();
		this.fechaCreacion = hilo.getFechaCreacion();
		this.creador = UsuarioSafe.of(hilo.getCreador());
		if (hilo.getCategoria() != null) this.categoria = hilo.getCategoria().getId();
		this.ultimaPublicacion = PublicacionSafe.of(hilo.getUltimaPublicacion());
		this.publicacionesTotales = hilo.getPublicacionesTotales();
		this.paginasTotales = hilo.getPaginasTotales();
	}
	private Long id;
	private String titulo;
	private LocalDateTime fechaCreacion = LocalDateTime.now();
	private UsuarioSafe creador;
	private int categoria;
	private PublicacionSafe ultimaPublicacion;
	private Integer publicacionesTotales;
	private Integer paginasTotales;
}
