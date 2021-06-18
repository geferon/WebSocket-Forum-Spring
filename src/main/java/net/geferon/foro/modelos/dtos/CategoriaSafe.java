package net.geferon.foro.modelos.dtos;


import lombok.Data;
import net.geferon.foro.modelos.CategoriaVO;

@Data
public class CategoriaSafe {
	public static CategoriaSafe of(CategoriaVO categoria) {
		if (categoria == null) return null;
		return new CategoriaSafe(categoria);
	}
	public CategoriaSafe(CategoriaVO categoria, Integer id) {
		this(categoria);
	}
	private CategoriaSafe(CategoriaVO categoria) {
		this.id = categoria.getId();
		this.nombre = categoria.getNombre();
		this.descripcion = categoria.getDescripcion();
		this.color = categoria.getColor();
		this.icono = categoria.getIcono();
		this.ultimoHilo = HiloSafe.of(categoria.getUltimoHilo());
	}
	private Integer id;
	private String nombre;
	private String descripcion;
	private String color;
	private String icono;
	private HiloSafe ultimoHilo;
}
