package net.geferon.foro.controladores;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.geferon.foro.helpers.SocketHelper;
import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.modelos.PublicacionVO;
import net.geferon.foro.modelos.SubscripcionVO;
import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.modelos.dtos.CreateSubscriptionDTO;
import net.geferon.foro.modelos.dtos.LastReadDTO;
import net.geferon.foro.repositorios.servicios.ServicioHilo;
import net.geferon.foro.repositorios.servicios.ServicioPublicacion;
import net.geferon.foro.repositorios.servicios.ServicioSubscripcion;
import net.geferon.foro.servicios.UserDetailsImpl;

@RestController
@RequestMapping("api/subscripcion")
@PreAuthorize("isAuthenticated()")
public class SubscripcionesController {
	@Autowired
	ServicioSubscripcion serv;
	@Autowired
	ServicioHilo servHilo;
	@Autowired
	ServicioPublicacion servPublicacion;
	@Autowired
	SocketHelper helper;

	@GetMapping()
	public ResponseEntity<List<SubscripcionVO>> getSubscriptions(Authentication authentication) {
		UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
		UsuarioVO usuario = details.getUsuario();

		return ResponseEntity.ok(serv.findByUsuario(usuario)); 
	}
	
	@PostMapping()
	public ResponseEntity<SubscripcionVO> createSubscription(@RequestBody CreateSubscriptionDTO body, Authentication authentication) {
		UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
		UsuarioVO usuario = details.getUsuario();
		
		Optional<HiloVO> hiloFound = servHilo.findById(body.getIdHilo());
		if (hiloFound.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		HiloVO hilo = hiloFound.get();
		

		SubscripcionVO.PrimaryKey id = new SubscripcionVO.PrimaryKey(usuario.getId(), hilo.getId());
		SubscripcionVO subscripcion = new SubscripcionVO();
		subscripcion.setId(id);
		subscripcion.setUsuario(usuario);
		subscripcion.setHilo(hilo);
		subscripcion.setUltimaPublicacionLeida(hilo.getUltimaPublicacion());
		
		SubscripcionVO subscripcionGuardada = serv.saveAndFlush(subscripcion);
		serv.refresh(subscripcionGuardada);
		helper.subscripcionCreadaOActualizada(subscripcionGuardada);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(serv.findById(id).get());
	}

	@DeleteMapping(params = { "hilo" })
	public ResponseEntity<SubscripcionVO> deleteSubscription(
			@RequestParam("hilo") Long hilo,
			Authentication authentication
		) {
		UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
		UsuarioVO usuario = details.getUsuario();

		SubscripcionVO.PrimaryKey id = new SubscripcionVO.PrimaryKey(usuario.getId(), hilo);
		
		Optional<SubscripcionVO> subscripcion = serv.findById(id);
		
		if (subscripcion.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		serv.delete(subscripcion.get());
		helper.subscripcionBorrada(subscripcion.get());
		
		return ResponseEntity.ok(subscripcion.get());
	}
	
	@PostMapping("read")
	@Transactional
	public ResponseEntity<?> markAsRead(@RequestBody LastReadDTO lastRead, Authentication authentication) {
		UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
		UsuarioVO usuario = details.getUsuario();

		Optional<PublicacionVO> publicacionFound = servPublicacion.findById(lastRead.idPublicacion);

		if (publicacionFound.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		PublicacionVO publicacion = publicacionFound.get();

		SubscripcionVO.PrimaryKey id = new SubscripcionVO.PrimaryKey(usuario.getId(), publicacion.getHilo().getId());
		Optional<SubscripcionVO> subscripcionFound = serv.findById(id);
		if (subscripcionFound.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		SubscripcionVO subscripcion = subscripcionFound.get();
		subscripcion.setUltimaPublicacionLeida(publicacion);

		SubscripcionVO subsSaved = serv.saveAndFlush(subscripcion);
		serv.refresh(subsSaved);
		helper.subscripcionCreadaOActualizada(subscripcion);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(subsSaved);
	}
	
}
