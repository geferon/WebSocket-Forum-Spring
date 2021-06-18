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

import net.geferon.foro.modelos.SubscripcionVO;
import net.geferon.foro.modelos.SubscripcionVO.PrimaryKey;
import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.repositorios.SubscripcionRepositorio;
import net.geferon.foro.repositorios.servicios.ServicioSubscripcion;

@Service
public class ServicioSubscripcionImpl implements ServicioSubscripcion {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	SubscripcionRepositorio rep;

	@Override
	public List<SubscripcionVO> findByUsuario(UsuarioVO usuario) {
		return rep.findByUsuario(usuario);
	}

	@Override
	public <S extends SubscripcionVO> S save(S entity) {
		return rep.save(entity);
	}

	@Override
	public <S extends SubscripcionVO> Optional<S> findOne(Example<S> example) {
		return rep.findOne(example);
	}

	@Override
	public Page<SubscripcionVO> findAll(Pageable pageable) {
		return rep.findAll(pageable);
	}

	@Override
	public List<SubscripcionVO> findAll() {
		return rep.findAll();
	}

	@Override
	public List<SubscripcionVO> findAll(Sort sort) {
		return rep.findAll(sort);
	}

	@Override
	public List<SubscripcionVO> findAllById(Iterable<PrimaryKey> ids) {
		return rep.findAllById(ids);
	}

	@Override
	public <S extends SubscripcionVO> List<S> saveAll(Iterable<S> entities) {
		return rep.saveAll(entities);
	}

	@Override
	public Optional<SubscripcionVO> findById(PrimaryKey id) {
		return rep.findById(id);
	}

	@Override
	public void flush() {
		rep.flush();
	}

	@Override
	public <S extends SubscripcionVO> S saveAndFlush(S entity) {
		return rep.saveAndFlush(entity);
	}

	@Override
	public boolean existsById(PrimaryKey id) {
		return rep.existsById(id);
	}

	@Override
	public void deleteInBatch(Iterable<SubscripcionVO> entities) {
		rep.deleteInBatch(entities);
	}

	@Override
	public <S extends SubscripcionVO> Page<S> findAll(Example<S> example, Pageable pageable) {
		return rep.findAll(example, pageable);
	}

	@Override
	public void deleteAllInBatch() {
		rep.deleteAllInBatch();
	}

	@Override
	public SubscripcionVO getOne(PrimaryKey id) {
		return rep.getOne(id);
	}

	@Override
	public <S extends SubscripcionVO> long count(Example<S> example) {
		return rep.count(example);
	}

	@Override
	public <S extends SubscripcionVO> boolean exists(Example<S> example) {
		return rep.exists(example);
	}

	@Override
	public <S extends SubscripcionVO> List<S> findAll(Example<S> example) {
		return rep.findAll(example);
	}

	@Override
	public long count() {
		return rep.count();
	}

	@Override
	public void deleteById(PrimaryKey id) {
		rep.deleteById(id);
	}

	@Override
	public <S extends SubscripcionVO> List<S> findAll(Example<S> example, Sort sort) {
		return rep.findAll(example, sort);
	}

	@Override
	public void delete(SubscripcionVO entity) {
		rep.delete(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends SubscripcionVO> entities) {
		rep.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		rep.deleteAll();
	}

	@Override
	@Transactional
	public void refresh(SubscripcionVO entity) {
		em.refresh(entity);
	}

}
