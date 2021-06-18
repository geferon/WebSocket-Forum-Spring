package net.geferon.foro.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import net.geferon.foro.modelos.CategoriaVO;
import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.modelos.PublicacionVO;
import net.geferon.foro.modelos.SubscripcionVO;
import net.geferon.foro.modelos.dtos.CategoriaSafe;
import net.geferon.foro.modelos.dtos.HiloSafe;
import net.geferon.foro.modelos.dtos.PublicacionPerfilSafe;
import net.geferon.foro.modelos.dtos.PublicacionSafe;
import net.geferon.foro.repositorios.servicios.ServicioCategoria;
import net.geferon.foro.repositorios.servicios.ServicioHilo;
import net.geferon.foro.repositorios.servicios.ServicioPublicacion;
import net.geferon.foro.repositorios.servicios.ServicioSubscripcion;

@Component
public class SocketHelper {
	//private Logger logger = LoggerFactory.getLogger(SocketHelper.class);
	@Autowired
    private SimpMessagingTemplate template;

	@Autowired
	ServicioHilo servHilos;
	@Autowired
	ServicioPublicacion servPubs;
	@Autowired
	ServicioCategoria servCategorias;
	@Autowired
	ServicioSubscripcion servSubs;

	public void publicacionCreada(PublicacionVO publicacion) {
		servPubs.refresh(publicacion);
		HiloVO hilo = publicacion.getHilo();
		servHilos.refresh(hilo);
		this.template.convertAndSend(
				"/topic/hilos/" + hilo.getId() + "/publicaciones/inserted",
				PublicacionSafe.of(publicacion)
			);
		this.template.convertAndSend(
				"/topic/usuarios/" + hilo.getCreador().getId() + "/publicaciones/inserted",
				PublicacionPerfilSafe.of(publicacion)
			);
		
		for (SubscripcionVO subscripcion : hilo.getSubscripciones()) {
			if (subscripcion.getUsuario() != publicacion.getCreador()) {
				servSubs.refresh(subscripcion);
				subscripcionCreadaOActualizada(subscripcion);
			}
		}
		
		this.hiloActualizado(hilo);
		//this.categoriaActualizada(hiloUpdated.getCategoria());
	}

	public void publicacionActualizada(PublicacionVO publicacion) {
		HiloVO hilo = publicacion.getHilo();
		this.template.convertAndSend(
				"/topic/hilos/" + hilo.getId() + "/publicaciones/updated",
				PublicacionSafe.of(publicacion)
			);
		this.template.convertAndSend(
				"/topic/usuarios/" + hilo.getCreador().getId() + "/publicaciones/updated",
				PublicacionPerfilSafe.of(publicacion)
			);
		
	}
	
	public void publicacionBorrada(PublicacionVO publicacion) {
		HiloVO hilo = publicacion.getHilo();
		this.template.convertAndSend(
				"/topic/hilos/" + hilo.getId() + "/publicaciones/deleted",
				PublicacionSafe.of(publicacion)
			);
		this.template.convertAndSend(
				"/topic/usuarios/" + hilo.getCreador().getId() + "/publicaciones/deleted",
				PublicacionSafe.of(publicacion)
			);
	}
	
	public void subscripcionCreadaOActualizada(SubscripcionVO subscripcion) {
		this.template.convertAndSendToUser(
				subscripcion.getUsuario().getUsername(),
				"/topic/subscripciones",
				subscripcion
			);
	}

	public void subscripcionBorrada(SubscripcionVO subscripcion) {
		this.template.convertAndSendToUser(
				subscripcion.getUsuario().getUsername(),
				"/topic/subscripciones/deleted",
				subscripcion
			);
	}
	
	public void hiloCreado(HiloVO hilo) {
		this.template.convertAndSend(
				"/topic/categorias/" + hilo.getCategoria().getId() + "/hilos/inserted",
				HiloSafe.of(hilo)
			);
		
		this.categoriaActualizada(hilo.getCategoria());
	}
	
	public void hiloActualizado(HiloVO hilo) {
		servHilos.refresh(hilo);
		CategoriaVO categoria = hilo.getCategoria();
		this.template.convertAndSend(
				"/topic/categorias/" + categoria.getId() + "/hilos/updated",
				HiloSafe.of(hilo)
			);
		
		if (categoria.getUltimoHilo().getId() == hilo.getId()) {
			this.categoriaActualizada(categoria);
		}
	}
	
	public void hiloBorrado(HiloVO hilo) {
		this.template.convertAndSend(
				"/topic/categorias/" + hilo.getCategoria().getId() + "/hilos/deleted",
				HiloSafe.of(hilo)
			);
		
		this.template.convertAndSend(
				"/topic/categorias/updated", CategoriaSafe.of(hilo.getCategoria())
			);
	}
	
	public void categoriaCreada(CategoriaVO categoria) {
		this.template.convertAndSend(
				"/topic/categorias/inserted", CategoriaSafe.of(categoria)
			);
	}
	
	public void categoriaActualizada(CategoriaVO categoria) {
		this.template.convertAndSend(
				"/topic/categorias/updated", CategoriaSafe.of(categoria)
			);
	}
	
	public void categoriaBorrada(CategoriaVO categoria) {
		this.template.convertAndSend(
				"/topic/categorias/deleted", CategoriaSafe.of(categoria)
			);
	}
}
