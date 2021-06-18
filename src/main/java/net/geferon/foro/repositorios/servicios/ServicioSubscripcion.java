package net.geferon.foro.repositorios.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import net.geferon.foro.modelos.SubscripcionVO;
import net.geferon.foro.modelos.SubscripcionVO.PrimaryKey;
import net.geferon.foro.modelos.UsuarioVO;

public interface ServicioSubscripcion {

	List<SubscripcionVO> findByUsuario(UsuarioVO usuario);

	<S extends SubscripcionVO> S save(S entity);

	<S extends SubscripcionVO> Optional<S> findOne(Example<S> example);

	Page<SubscripcionVO> findAll(Pageable pageable);

	List<SubscripcionVO> findAll();

	List<SubscripcionVO> findAll(Sort sort);

	List<SubscripcionVO> findAllById(Iterable<PrimaryKey> ids);

	<S extends SubscripcionVO> List<S> saveAll(Iterable<S> entities);

	Optional<SubscripcionVO> findById(PrimaryKey id);

	void flush();

	<S extends SubscripcionVO> S saveAndFlush(S entity);

	boolean existsById(PrimaryKey id);

	void deleteInBatch(Iterable<SubscripcionVO> entities);

	<S extends SubscripcionVO> Page<S> findAll(Example<S> example, Pageable pageable);

	void deleteAllInBatch();

	SubscripcionVO getOne(PrimaryKey id);

	<S extends SubscripcionVO> long count(Example<S> example);

	<S extends SubscripcionVO> boolean exists(Example<S> example);

	<S extends SubscripcionVO> List<S> findAll(Example<S> example);

	long count();

	void deleteById(PrimaryKey id);

	<S extends SubscripcionVO> List<S> findAll(Example<S> example, Sort sort);

	void delete(SubscripcionVO entity);

	void deleteAll(Iterable<? extends SubscripcionVO> entities);

	void deleteAll();

	void refresh(SubscripcionVO entity);

}