package net.geferon.foro.repositorios.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.modelos.PublicacionVO;
import net.geferon.foro.modelos.UsuarioVO;

public interface ServicioPublicacion {

	//List<PublicacionReaccion> getResumenReacciones(PublicacionVO publicacion);

	List<PublicacionVO> findByContenidoContaining(String contenido);

	Page<PublicacionVO> findAllByHilo(HiloVO hilo, Pageable pageable);

	<S extends PublicacionVO> S save(S entity);

	Page<PublicacionVO> findAllByCreador(UsuarioVO creador, Pageable pageable);

	<S extends PublicacionVO> Optional<S> findOne(Example<S> example);

	Page<PublicacionVO> findAll(Pageable pageable);

	List<PublicacionVO> findAll();

	List<PublicacionVO> findAll(Sort sort);

	List<PublicacionVO> findAllById(Iterable<Long> ids);

	<S extends PublicacionVO> List<S> saveAll(Iterable<S> entities);

	Optional<PublicacionVO> findById(Long id);

	void flush();

	<S extends PublicacionVO> S saveAndFlush(S entity);

	boolean existsById(Long id);

	void deleteInBatch(Iterable<PublicacionVO> entities);

	<S extends PublicacionVO> Page<S> findAll(Example<S> example, Pageable pageable);

	void deleteAllInBatch();

	PublicacionVO getOne(Long id);

	<S extends PublicacionVO> long count(Example<S> example);

	<S extends PublicacionVO> boolean exists(Example<S> example);

	<S extends PublicacionVO> List<S> findAll(Example<S> example);

	long count();

	void deleteById(Long id);

	<S extends PublicacionVO> List<S> findAll(Example<S> example, Sort sort);

	void delete(PublicacionVO entity);

	void deleteAll(Iterable<? extends PublicacionVO> entities);

	void deleteAll();

	void refresh(PublicacionVO entity);

}