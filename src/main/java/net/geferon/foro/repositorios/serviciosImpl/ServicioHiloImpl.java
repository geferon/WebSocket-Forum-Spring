package net.geferon.foro.repositorios.serviciosImpl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.geferon.foro.modelos.CategoriaVO;
import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.repositorios.HiloRepositorio;
import net.geferon.foro.repositorios.servicios.ServicioHilo;

@Service
public class ServicioHiloImpl implements ServicioHilo {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	HiloRepositorio rep;

	@Override
	public Page<HiloVO> findByTituloContaining(String contenido, Pageable pageable) {
		return rep.findByTituloContaining(contenido, pageable);
	}

	@Override
	public Page<HiloVO> findAllByCategoria(CategoriaVO categoria, Pageable pageable) {
		return rep.findAllByCategoria(categoria, pageable);
	}
	
	@Override
	public Page<HiloVO> findAllByCategoriaWithPagination(CategoriaVO categoria, Pageable pageable) {
		return rep.findAllByCategoriaWithPagination(categoria, pageable);
	}

	@Override
	public <S extends HiloVO> S save(S entity) {
		return rep.save(entity);
	}

	@Override
	public <S extends HiloVO> Optional<S> findOne(Example<S> example) {
		return rep.findOne(example);
	}

	@Override
	public Page<HiloVO> findAll(Pageable pageable) {
		return rep.findAll(pageable);
	}

	@Override
	public List<HiloVO> findAll() {
		return rep.findAll();
	}

	@Override
	public List<HiloVO> findAll(Sort sort) {
		return rep.findAll(sort);
	}

	@Override
	public List<HiloVO> findAllById(Iterable<Long> ids) {
		return rep.findAllById(ids);
	}

	@Override
	public <S extends HiloVO> List<S> saveAll(Iterable<S> entities) {
		return rep.saveAll(entities);
	}

	@Override
	public Optional<HiloVO> findById(Long id) {
		return rep.findById(id);
	}

	@Override
	public void flush() {
		rep.flush();
	}

	@Override
	public <S extends HiloVO> S saveAndFlush(S entity) {
		return rep.saveAndFlush(entity);
	}

	@Override
	public boolean existsById(Long id) {
		return rep.existsById(id);
	}

	@Override
	public void deleteInBatch(Iterable<HiloVO> entities) {
		rep.deleteInBatch(entities);
	}

	@Override
	public <S extends HiloVO> Page<S> findAll(Example<S> example, Pageable pageable) {
		return rep.findAll(example, pageable);
	}

	@Override
	public void deleteAllInBatch() {
		rep.deleteAllInBatch();
	}

	@Override
	public HiloVO getOne(Long id) {
		return rep.getOne(id);
	}

	@Override
	public <S extends HiloVO> long count(Example<S> example) {
		return rep.count(example);
	}

	@Override
	public <S extends HiloVO> boolean exists(Example<S> example) {
		return rep.exists(example);
	}

	@Override
	public <S extends HiloVO> List<S> findAll(Example<S> example) {
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
	public <S extends HiloVO> List<S> findAll(Example<S> example, Sort sort) {
		return rep.findAll(example, sort);
	}

	@Override
	public void delete(HiloVO entity) {
		rep.delete(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends HiloVO> entities) {
		rep.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		rep.deleteAll();
	}

	@Override
	@Transactional
	public void refresh(HiloVO entity) {
		em.refresh(entity);
	}
}
