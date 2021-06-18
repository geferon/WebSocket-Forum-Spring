package net.geferon.foro.repositorios.serviciosImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.geferon.foro.modelos.ERol;
import net.geferon.foro.modelos.RolVO;
import net.geferon.foro.repositorios.RolRepositorio;
import net.geferon.foro.repositorios.servicios.ServicioRol;

@Service
public class ServicioRolImpl implements ServicioRol {
	@Autowired
	RolRepositorio rep;

	@Override
	public Optional<RolVO> findByNombre(ERol nombre) {
		return rep.findByNombre(nombre);
	}

	@Override
	public <S extends RolVO> S save(S entity) {
		return rep.save(entity);
	}

	@Override
	public <S extends RolVO> Optional<S> findOne(Example<S> example) {
		return rep.findOne(example);
	}

	@Override
	public Page<RolVO> findAll(Pageable pageable) {
		return rep.findAll(pageable);
	}

	@Override
	public List<RolVO> findAll() {
		return rep.findAll();
	}

	@Override
	public List<RolVO> findAll(Sort sort) {
		return rep.findAll(sort);
	}

	@Override
	public List<RolVO> findAllById(Iterable<Long> ids) {
		return rep.findAllById(ids);
	}

	@Override
	public <S extends RolVO> List<S> saveAll(Iterable<S> entities) {
		return rep.saveAll(entities);
	}

	@Override
	public Optional<RolVO> findById(Long id) {
		return rep.findById(id);
	}

	@Override
	public void flush() {
		rep.flush();
	}

	@Override
	public <S extends RolVO> S saveAndFlush(S entity) {
		return rep.saveAndFlush(entity);
	}

	@Override
	public boolean existsById(Long id) {
		return rep.existsById(id);
	}

	@Override
	public void deleteInBatch(Iterable<RolVO> entities) {
		rep.deleteInBatch(entities);
	}

	@Override
	public <S extends RolVO> Page<S> findAll(Example<S> example, Pageable pageable) {
		return rep.findAll(example, pageable);
	}

	@Override
	public void deleteAllInBatch() {
		rep.deleteAllInBatch();
	}

	@Override
	public RolVO getOne(Long id) {
		return rep.getOne(id);
	}

	@Override
	public <S extends RolVO> long count(Example<S> example) {
		return rep.count(example);
	}

	@Override
	public <S extends RolVO> boolean exists(Example<S> example) {
		return rep.exists(example);
	}

	@Override
	public <S extends RolVO> List<S> findAll(Example<S> example) {
		return rep.findAll(example);
	}

	@Override
	public long count() {
		return rep.count();
	}

	@Override
	public void deleteById(Long id) {
		rep.deleteById(id);
	}

	@Override
	public <S extends RolVO> List<S> findAll(Example<S> example, Sort sort) {
		return rep.findAll(example, sort);
	}

	@Override
	public void delete(RolVO entity) {
		rep.delete(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends RolVO> entities) {
		rep.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		rep.deleteAll();
	}

	
	
}
