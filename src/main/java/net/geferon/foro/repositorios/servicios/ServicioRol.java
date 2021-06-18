package net.geferon.foro.repositorios.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import net.geferon.foro.modelos.ERol;
import net.geferon.foro.modelos.RolVO;

public interface ServicioRol {

	Optional<RolVO> findByNombre(ERol nombre);

	<S extends RolVO> S save(S entity);

	<S extends RolVO> Optional<S> findOne(Example<S> example);

	Page<RolVO> findAll(Pageable pageable);

	List<RolVO> findAll();

	List<RolVO> findAll(Sort sort);

	List<RolVO> findAllById(Iterable<Long> ids);

	<S extends RolVO> List<S> saveAll(Iterable<S> entities);

	Optional<RolVO> findById(Long id);

	void flush();

	<S extends RolVO> S saveAndFlush(S entity);

	boolean existsById(Long id);

	void deleteInBatch(Iterable<RolVO> entities);

	<S extends RolVO> Page<S> findAll(Example<S> example, Pageable pageable);

	void deleteAllInBatch();

	RolVO getOne(Long id);

	<S extends RolVO> long count(Example<S> example);

	<S extends RolVO> boolean exists(Example<S> example);

	<S extends RolVO> List<S> findAll(Example<S> example);

	long count();

	void deleteById(Long id);

	<S extends RolVO> List<S> findAll(Example<S> example, Sort sort);

	void delete(RolVO entity);

	void deleteAll(Iterable<? extends RolVO> entities);

	void deleteAll();

}