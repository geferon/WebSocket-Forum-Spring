package net.geferon.foro.repositorios.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import net.geferon.foro.modelos.CategoriaVO;
import net.geferon.foro.modelos.HiloVO;

public interface ServicioHilo {

	Page<HiloVO> findByTituloContaining(String contenido, Pageable pageable);

	Page<HiloVO> findAllByCategoria(CategoriaVO categoria, Pageable pageable);
	
	Page<HiloVO> findAllByCategoriaWithPagination(CategoriaVO categoria, Pageable pageable);

	<S extends HiloVO> S save(S entity);

	<S extends HiloVO> Optional<S> findOne(Example<S> example);

	Page<HiloVO> findAll(Pageable pageable);

	List<HiloVO> findAll();

	List<HiloVO> findAll(Sort sort);

	List<HiloVO> findAllById(Iterable<Long> ids);

	<S extends HiloVO> List<S> saveAll(Iterable<S> entities);

	Optional<HiloVO> findById(Long id);

	void flush();

	<S extends HiloVO> S saveAndFlush(S entity);

	boolean existsById(Long id);

	void deleteInBatch(Iterable<HiloVO> entities);

	<S extends HiloVO> Page<S> findAll(Example<S> example, Pageable pageable);

	void deleteAllInBatch();

	HiloVO getOne(Long id);

	<S extends HiloVO> long count(Example<S> example);

	<S extends HiloVO> boolean exists(Example<S> example);

	<S extends HiloVO> List<S> findAll(Example<S> example);

	long count();

	void deleteById(Long id);

	<S extends HiloVO> List<S> findAll(Example<S> example, Sort sort);

	void delete(HiloVO entity);

	void deleteAll(Iterable<? extends HiloVO> entities);

	void deleteAll();

	void refresh(HiloVO entity);

}