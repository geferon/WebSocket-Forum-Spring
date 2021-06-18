package net.geferon.foro.modelos.dtos;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.geferon.foro.modelos.CategoriaVO;
import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.modelos.PublicacionVO;
import net.geferon.foro.modelos.UsuarioVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HiloNewDTO {
	private String titulo;
	private CategoriaVO categoria;
	private String contenido;
	
	public HiloVO toHiloVO(UsuarioVO creador) {
		HiloVO hilo = new HiloVO();
		hilo.setTitulo(this.titulo);
		hilo.setCategoria(this.categoria);
		hilo.setCreador(creador);

		PublicacionVO nuevaPublicacion = new PublicacionVO();
		nuevaPublicacion.setCreador(creador);
		nuevaPublicacion.setContenido(this.contenido);
		nuevaPublicacion.setHilo(hilo);
		hilo.getPublicaciones().add(nuevaPublicacion);
		
		return hilo;
	}
}

