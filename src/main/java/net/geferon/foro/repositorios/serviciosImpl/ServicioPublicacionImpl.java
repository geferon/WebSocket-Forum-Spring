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

import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.modelos.PublicacionVO;
import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.repositorios.PublicacionRepositorio;
import net.geferon.foro.repositorios.servicios.ServicioPublicacion;

@Service
public class ServicioPublicacionImpl implements ServicioPublicacion {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	PublicacionRepositorio rep;

	/*
	@Override
	public List<PublicacionReaccion> getResumenReacciones(PublicacionVO publicacion) {
		return rep.getResumenReacciones(publicacion);
	}
	*/

	@Override
	public List<PublicacionVO> findByContenidoContaining(String contenido) {
		return rep.findByContenidoContaining(contenido);
	}

	@Override
	public Page<PublicacionVO> findAllByHilo(HiloVO hilo, Pageable pageable) {
		return rep.findAllByHilo(hilo, pageable);
	}

	@Override
	public <S extends PublicacionVO> S save(S entity) {
		return rep.save(entity);
	}

	@Override
	public Page<PublicacionVO> findAllByCreador(UsuarioVO creador, Pageable pageable) {
		return rep.findAllByCreador(creador, pageable);
	}

	@Override
	public <S extends PublicacionVO> Optional<S> findOne(Example<S> example) {
		return rep.findOne(example);
	}

	@Override
	public Page<PublicacionVO> findAll(Pageable pageable) {
		return rep.findAll(pageable);
	}

	@Override
	public List<PublicacionVO> findAll() {
		return rep.findAll();
	}

	@Override
	public List<PublicacionVO> findAll(Sort sort) {
		return rep.findAll(sort);
	}

	@Override
	public List<PublicacionVO> findAllById(Iterable<Long> ids) {
		return rep.findAllById(ids);
	}

	@Override
	public <S extends PublicacionVO> List<S> saveAll(Iterable<S> entities) {
		return rep.saveAll(entities);
	}

	@Override
	public Optional<PublicacionVO> findById(Long id) {
		return rep.findById(id);
	}

	@Override
	public void flush() {
		rep.flush();
	}

	@Override
	public <S extends PublicacionVO> S saveAndFlush(S entity) {
		return rep.saveAndFlush(entity);
	}

	@Override
	public boolean existsById(Long id) {
		return rep.existsById(id);
	}

	@Override
	public void deleteInBatch(Iterable<PublicacionVO> entities) {
		rep.deleteInBatch(entities);
	}

	@Override
	public <S extends PublicacionVO> Page<S> findAll(Example<S> example, Pageable pageable) {
		return rep.findAll(example, pageable);
	}

	@Override
	public void deleteAllInBatch() {
		rep.deleteAllInBatch();
	}

	@Override
	public PublicacionVO getOne(Long id) {
		return rep.getOne(id);
	}

	@Override
	public <S extends PublicacionVO> long count(Example<S> example) {
		return rep.count(example);
	}

	@Override
	public <S extends PublicacionVO> boolean exists(Example<S> example) {
		return rep.exists(example);
	}

	@Override
	public <S extends PublicacionVO> List<S> findAll(Example<S> example) {
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
	public <S extends PublicacionVO> List<S> findAll(Example<S> example, Sort sort) {
		return rep.findAll(example, sort);
	}

	@Override
	public void delete(PublicacionVO entity) {
		rep.delete(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends PublicacionVO> entities) {
		rep.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		rep.deleteAll();
	}
	
	@Override
	@Transactional
	public void refresh(PublicacionVO entity) {
		em.refresh(entity);
	}
	
}
