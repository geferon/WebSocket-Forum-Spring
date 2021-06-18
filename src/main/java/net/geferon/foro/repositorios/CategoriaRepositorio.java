package net.geferon.foro.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import net.geferon.foro.modelos.CategoriaVO;
import net.geferon.foro.modelos.dtos.CategoriaSafe;

public interface CategoriaRepositorio extends CrudRepository<CategoriaVO, Integer> {
	public Optional<CategoriaVO> findByNombreContaining(String nombre);

	@Query(value = "SELECT new net.geferon.foro.modelos.dtos.CategoriaSafe(c, c.id) "
			+ "FROM CategoriaVO c")
	public Iterable<CategoriaSafe> getAllSafe();
}
