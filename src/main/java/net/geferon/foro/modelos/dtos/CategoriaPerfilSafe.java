package net.geferon.foro.modelos.dtos;

import lombok.Data;
import net.geferon.foro.modelos.CategoriaVO;

@Data
public class CategoriaPerfilSafe {
	public static CategoriaPerfilSafe of(CategoriaVO categoria) {
		if (categoria == null) return null;
		return new CategoriaPerfilSafe(categoria);
	}
	private CategoriaPerfilSafe(CategoriaVO categoria) {
		this.id = categoria.getId();
		this.nombre = categoria.getNombre();
		this.descripcion = categoria.getDescripcion();
		this.color = categoria.getColor();
		this.icono = categoria.getIcono();
	}
	private Integer id;
	private String nombre;
	private String descripcion;
	private String color;
	private String icono;
}
