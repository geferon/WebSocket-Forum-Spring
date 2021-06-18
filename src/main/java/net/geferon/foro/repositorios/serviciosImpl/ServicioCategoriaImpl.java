package net.geferon.foro.repositorios.serviciosImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.geferon.foro.modelos.CategoriaVO;
import net.geferon.foro.modelos.dtos.CategoriaSafe;
import net.geferon.foro.repositorios.CategoriaRepositorio;
import net.geferon.foro.repositorios.servicios.ServicioCategoria;

@Service
public class ServicioCategoriaImpl implements ServicioCategoria {
	@Autowired
	CategoriaRepositorio rep;

	@Override
	public <S extends CategoriaVO> S save(S entity) {
		return rep.save(entity);
	}

	@Override
	public <S extends CategoriaVO> Iterable<S> saveAll(Iterable<S> entities) {
		return rep.saveAll(entities);
	}

	@Override
	public Optional<CategoriaVO> findById(Integer id) {
		return rep.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return rep.existsById(id);
	}

	@Override
	public Iterable<CategoriaVO> findAll() {
		return rep.findAll();
	}

	@Override
	public Iterable<CategoriaVO> findAllById(Iterable<Integer> ids) {
		return rep.findAllById(ids);
	}
	
	@Override
	public Iterable<CategoriaSafe> getAllSafe() {
		return rep.getAllSafe();
	}

	@Override
	public long count() {
		return rep.count();
	}

	@Override
	public void deleteById(Integer id) {
		rep.deleteById(id);
	}

	@Override
	public void delete(CategoriaVO entity) {
		rep.delete(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends CategoriaVO> entities) {
		rep.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		rep.deleteAll();
	}

	@Override
	public Optional<CategoriaVO> findByNombreContaining(String nombre) {
		return rep.findByNombreContaining(nombre);
	}

}
