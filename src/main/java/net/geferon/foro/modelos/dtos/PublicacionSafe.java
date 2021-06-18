package net.geferon.foro.modelos.dtos;

import java.time.LocalDateTime;

import lombok.Data;
import net.geferon.foro.modelos.PublicacionVO;

@Data
public class PublicacionSafe {
	public static PublicacionSafe of(PublicacionVO publicacion) {
		if (publicacion == null) return null;
		return new PublicacionSafe(publicacion);
	}
	private PublicacionSafe(PublicacionVO publicacion) {
		this.id = publicacion.getId();
		this.idPublicacion = publicacion.getIdPublicacion();
		this.creador = UsuarioSafe.of(publicacion.getCreador());
		this.publicadoEn = publicacion.getPublicadoEn();
		this.editadoEn = publicacion.getEditadoEn();
		this.contenido = publicacion.getContenido();
		this.pagina = publicacion.getPagina();
	}
	
	private Long id;
	private int idPublicacion;
	private UsuarioSafe creador;
	private LocalDateTime publicadoEn = LocalDateTime.now();
	private LocalDateTime editadoEn = this.publicadoEn;
	private String contenido;
	private int pagina;
}
