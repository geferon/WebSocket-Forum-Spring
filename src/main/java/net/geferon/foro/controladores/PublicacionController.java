package net.geferon.foro.controladores;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import net.geferon.foro.excepciones.ResourceNotFoundException;
import net.geferon.foro.helpers.SeguridadControladores;
import net.geferon.foro.helpers.SocketHelper;
import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.modelos.PublicacionVO;
import net.geferon.foro.modelos.SubscripcionVO;
import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.modelos.dtos.PublicacionCreateDTO;
import net.geferon.foro.modelos.dtos.PublicacionEditDTO;
import net.geferon.foro.modelos.dtos.PublicacionPerfilSafe;
import net.geferon.foro.modelos.dtos.PublicacionSafe;
import net.geferon.foro.repositorios.servicios.ServicioHilo;
import net.geferon.foro.repositorios.servicios.ServicioPublicacion;
import net.geferon.foro.repositorios.servicios.ServicioSubscripcion;
import net.geferon.foro.repositorios.servicios.ServicioUsuario;
import net.geferon.foro.servicios.UserDetailsImpl;

@RestController
@RequestMapping("api/publicacion")
public class PublicacionController {
	@Autowired
	ServicioPublicacion serv;
	@Autowired
	ServicioUsuario servUsuario;
	@Autowired
	ServicioHilo servHilo;
	@Autowired
	ServicioSubscripcion servSubs;
	@Autowired
	SocketHelper helper;

	@Autowired
	private SeguridadControladores seguridad;
	
	@GetMapping(params = { "page", "size", "usuario" })
	public Page<PublicacionPerfilSafe> findByUser(
		@RequestParam("page") int page, @RequestParam("usuario") int usuario,
		@RequestParam(name = "size", required = false) Integer size,
		UriComponentsBuilder uriBuilder, HttpServletResponse response
	) {
		if (size == null || size > HiloVO.TOTAL_ITEMS_PER_PAGE) {
			size = HiloVO.TOTAL_ITEMS_PER_PAGE;
		}
		
		Optional<UsuarioVO> usuarioObj = servUsuario.findById(usuario);
		
		if (usuarioObj.isEmpty()) {
			throw new ResourceNotFoundException();
		}
		
		Pageable pageRequest = PageRequest.of(page, HiloVO.TOTAL_ITEMS_PER_PAGE);
		Page<PublicacionVO> resultPage = serv.findAllByCreador(usuarioObj.get(), pageRequest);
		if (page > resultPage.getTotalPages() && resultPage.getTotalPages() > 1) {
			throw new ResourceNotFoundException();
		}

		return resultPage.map(p -> PublicacionPerfilSafe.of(p));
	}

	@GetMapping(params = { "page", "size", "hilo" })
	public Page<PublicacionSafe> findPaginated(
		@RequestParam("page") int page, @RequestParam("hilo") Long hilo,
		@RequestParam(name = "size", required = false) Integer size,
		UriComponentsBuilder uriBuilder, HttpServletResponse response
	) {
		if (size == null || size > HiloVO.TOTAL_ITEMS_PER_PAGE) {
			size = HiloVO.TOTAL_ITEMS_PER_PAGE;
		}
				
		Optional<HiloVO> hiloObj = servHilo.findById(hilo);
		
		if (hiloObj.isEmpty()) {
			throw new ResourceNotFoundException();
		}
		
		Pageable pageRequest = PageRequest.of(page, HiloVO.TOTAL_ITEMS_PER_PAGE);
		Page<PublicacionVO> resultPage = serv.findAllByHilo(hiloObj.get(), pageRequest);
		if (page > resultPage.getTotalPages() && resultPage.getTotalPages() > 1) {
			throw new ResourceNotFoundException();
		}

		return resultPage.map(p -> PublicacionSafe.of(p));
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping()
	public ResponseEntity<PublicacionSafe> create(@RequestBody PublicacionCreateDTO publicacion, Authentication authentication) {
		UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
		UsuarioVO usuario = details.getUsuario();

		// Realizado mediante este metodo para que el usuario no pueda modificar ciertos campos a los que se
		// supone que no tendria que poder cambiar, como el creador, la fecha de creacion o la fecha de edicion
		PublicacionVO publicacionNueva = new PublicacionVO();
		publicacionNueva.setCreador(usuario);
		BeanUtils.copyProperties(publicacion, publicacionNueva, "hilo");
		
		HiloVO hilo = servHilo.findById(publicacion.getHilo().getId()).get();
		publicacionNueva.setHilo(hilo);

		hilo.setPublicacionesTotales(hilo.getPublicacionesTotales() + 1);
		publicacionNueva.setIdPublicacion(hilo.getPublicacionesTotales());

		// Guardamos ambas, primero la publicacion y luego el hilo
		servHilo.save(hilo);
		PublicacionVO publicacionSaved = serv.saveAndFlush(publicacionNueva);
		serv.refresh(publicacionSaved);
		
		// Comprobamos tambien si el usuario esta suscrito, y si es asi, actualizamos la subscripcion ya que
		// no tenemos que actualizarla al ser el el creador de la publicacion
		SubscripcionVO.PrimaryKey subId = new SubscripcionVO.PrimaryKey(usuario.getId(), hilo.getId());
		Optional<SubscripcionVO> subscripcionFound = servSubs.findById(subId);
		if (subscripcionFound.isPresent()) {
			SubscripcionVO subscripcion = subscripcionFound.get();
			subscripcion.setUltimaPublicacionLeida(publicacionSaved);
			servSubs.saveAndFlush(subscripcion);
		}

		this.helper.publicacionCreada(publicacionSaved);

		return ResponseEntity.status(HttpStatus.CREATED).body(PublicacionSafe.of(publicacionSaved));
	}

	@PreAuthorize("isAuthenticated()")
	@PutMapping("{id}")
	public ResponseEntity<PublicacionVO> update(@PathVariable() Long id, @RequestBody PublicacionEditDTO publicacion,
			Authentication authentication) {
		Optional<PublicacionVO> publicacionFind = serv.findById(id);
		if (publicacionFind.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		PublicacionVO publicacionToUpdate = publicacionFind.get();
		
		// Comprobamos que el usuario tiene permisos para editar la publicacion
		if (!seguridad.canEditPublicacion(publicacionToUpdate, authentication)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		// Realizado mediante este metodo para que el usuario no pueda modificar ciertos campos a los que se
		// supone que no tendria que poder cambiar, como la fecha de creacion o la fecha de edicion
		BeanUtils.copyProperties(publicacion, publicacionToUpdate);
		//publicacionToUpdate.setEditadoEn(LocalDateTime.now());
		
		PublicacionVO publicacionSaved = serv.save(publicacionToUpdate);

		this.helper.publicacionActualizada(publicacionSaved);
		
		return ResponseEntity.ok(publicacionSaved);
	}

	/*
	@PreAuthorize("isAuthenticated() && @seguridadControladores.canDeletePublicacion(#id,authentication)")
	@DeleteMapping("{id}")
	public ResponseEntity<PublicacionVO> delete(@PathVariable Long id) {
		Optional<PublicacionVO> publicacion = serv.findById(id);
		if (publicacion.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		serv.delete(publicacion.get());

		this.helper.publicacionBorrada(publicacion.get());
		
		return ResponseEntity.ok(publicacion.get());
	}
	*/

}
