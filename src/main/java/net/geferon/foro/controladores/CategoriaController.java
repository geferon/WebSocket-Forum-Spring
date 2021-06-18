package net.geferon.foro.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.geferon.foro.helpers.SocketHelper;
import net.geferon.foro.modelos.CategoriaVO;
import net.geferon.foro.modelos.dtos.CategoriaSafe;
import net.geferon.foro.repositorios.servicios.ServicioCategoria;

@RestController
@RequestMapping("api/categoria")
public class CategoriaController {
	@Autowired
	ServicioCategoria serv;
	@Autowired
	SocketHelper helper;
	
	@GetMapping()
	public List<CategoriaSafe> getAll() {
		return (List<CategoriaSafe>) serv.getAllSafe();
	}
	
	@GetMapping("{id}")
	public ResponseEntity<CategoriaVO> get(@PathVariable() int id) {
		return ResponseEntity.of(serv.findById(id));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping()
	public ResponseEntity<CategoriaVO> create(@RequestBody CategoriaVO categoria) {
		CategoriaVO categoriaNueva = serv.save(categoria);
		
		helper.categoriaCreada(categoriaNueva);
		
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(categoriaNueva);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("{id}")
	public ResponseEntity<CategoriaVO> update(@PathVariable() int id, @RequestBody CategoriaVO categoria) {
		if (serv.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		categoria.setId(id);
		CategoriaVO categoriaActualizada = serv.save(categoria);
		
		helper.categoriaActualizada(categoriaActualizada);
		
		return ResponseEntity.ok(categoriaActualizada);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("{id}")
	public ResponseEntity<CategoriaVO> delete(@PathVariable int id) {
		Optional<CategoriaVO> categoria = serv.findById(id);
		if (categoria.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		serv.delete(categoria.get());
		
		helper.categoriaBorrada(categoria.get());
		
		return ResponseEntity.ok(categoria.get());
	}
	
}
