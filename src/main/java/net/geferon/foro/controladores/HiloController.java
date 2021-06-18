package net.geferon.foro.controladores;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import net.geferon.foro.modelos.CategoriaVO;
import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.modelos.SubscripcionVO;
import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.modelos.dtos.HiloDTO;
import net.geferon.foro.modelos.dtos.HiloNewDTO;
import net.geferon.foro.modelos.dtos.HiloSafe;
import net.geferon.foro.repositorios.servicios.ServicioCategoria;
import net.geferon.foro.repositorios.servicios.ServicioHilo;
import net.geferon.foro.repositorios.servicios.ServicioSubscripcion;
import net.geferon.foro.repositorios.servicios.ServicioUsuario;
import net.geferon.foro.servicios.UserDetailsImpl;

@RestController
@RequestMapping("api/hilo")
public class HiloController {
	@Autowired
	ServicioHilo serv;
	@Autowired
	ServicioCategoria servCat;
	@Autowired
	ServicioUsuario servUsuario;
	@Autowired
	ServicioSubscripcion servSubs;
	@Autowired
	SocketHelper helper;
	
	@Autowired
	private SeguridadControladores seguridad;

	@GetMapping(params = { "page", "size", "categoria" })
	public Page<HiloSafe> findPaginated(@RequestParam("page") int page, @RequestParam("categoria") int categoria,
			UriComponentsBuilder uriBuilder, HttpServletResponse response) {
		Pageable pageRequest = PageRequest.of(page, CategoriaVO.TOTAL_ITEMS_PER_PAGE);
		
		Optional<CategoriaVO> categoriaObj = servCat.findById(categoria);
		
		if (categoriaObj.isEmpty()) {
			throw new ResourceNotFoundException();
		}
		
		Page<HiloVO> resultPage = serv.findAllByCategoriaWithPagination(categoriaObj.get(), pageRequest);
		//Page<HiloVO> resultPage = serv.findAllByCategoriaWithPagination(categoria, pageRequest);
		if (page >= resultPage.getTotalPages() && resultPage.getTotalPages() > 1) {
			throw new ResourceNotFoundException();
		}
		
		Page<HiloSafe> resultPageSafe = resultPage.map(c -> {
			return HiloSafe.of(c);
		});

		return resultPageSafe;
	}
	
	@GetMapping("{id}")
	public ResponseEntity<HiloSafe> findById(@PathVariable() Long id) {
		return ResponseEntity.of(
				serv.findById(id)
				.map(h -> HiloSafe.of(h))
			);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping()
	@Transactional
	public ResponseEntity<HiloVO> create(@RequestBody HiloNewDTO hilo, Authentication authentication) {
		UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
		UsuarioVO usuario = details.getUsuario();
		HiloVO hiloSaved = serv.saveAndFlush(hilo.toHiloVO(usuario));

		serv.refresh(hiloSaved);
		
		// Subscribimos al creador al hilo
		SubscripcionVO.PrimaryKey id = new SubscripcionVO.PrimaryKey(usuario.getId(), hiloSaved.getId());
		SubscripcionVO subscripcion = new SubscripcionVO();
		subscripcion.setId(id);
		subscripcion.setUsuario(usuario);
		subscripcion.setHilo(hiloSaved);
		subscripcion.setUltimaPublicacionLeida(hiloSaved.getUltimaPublicacion());
		
		SubscripcionVO subscripcionNueva = servSubs.saveAndFlush(subscripcion);
		servSubs.refresh(subscripcionNueva);
		
		helper.hiloCreado(hiloSaved);

		helper.subscripcionCreadaOActualizada(subscripcionNueva);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(hiloSaved);
	}

	@PreAuthorize("isAuthenticated()")
	@PutMapping("{id}")
	public ResponseEntity<HiloVO> update(@PathVariable() Long id, @RequestBody HiloDTO hilo,
			Authentication authentication) {
		Optional<HiloVO> hiloFound = serv.findById(id);
		if (hiloFound.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		HiloVO hiloObj = hiloFound.get();
		
		if (!seguridad.canEditHilo(hiloObj, authentication)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		BeanUtils.copyProperties(hilo, hiloObj);
		
		HiloVO hiloSaved = serv.saveAndFlush(hiloObj);
		serv.refresh(hiloSaved);

		helper.hiloActualizado(hiloSaved);
		
		return ResponseEntity.ok(hiloSaved);
	}

	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("{id}")
	public ResponseEntity<HiloVO> delete(@PathVariable Long id, Authentication authentication) {
		Optional<HiloVO> hilo = serv.findById(id);
		if (hilo.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		if (!seguridad.canDeleteHilo(hilo.get(), authentication)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	
		serv.delete(hilo.get());
		
		helper.hiloBorrado(hilo.get());
		
		return ResponseEntity.ok(hilo.get());
	}

}
