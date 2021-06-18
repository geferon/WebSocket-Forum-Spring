package net.geferon.foro.repositorios.servicios;

import java.util.Optional;

import net.geferon.foro.modelos.CategoriaVO;
import net.geferon.foro.modelos.dtos.CategoriaSafe;

public interface ServicioCategoria {

	<S extends CategoriaVO> S save(S entity);

	<S extends CategoriaVO> Iterable<S> saveAll(Iterable<S> entities);

	Optional<CategoriaVO> findById(Integer id);

	boolean existsById(Integer id);

	Iterable<CategoriaVO> findAll();

	Iterable<CategoriaVO> findAllById(Iterable<Integer> ids);
	
	Iterable<CategoriaSafe> getAllSafe();

	long count();

	void deleteById(Integer id);

	void delete(CategoriaVO entity);

	void deleteAll(Iterable<? extends CategoriaVO> entities);

	void deleteAll();

	public Optional<CategoriaVO> findByNombreContaining(String nombre);

}