package net.geferon.foro.repositorios.serviciosImpl;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.repositorios.UsuarioRepositorio;
import net.geferon.foro.repositorios.servicios.ServicioUsuario;
import net.geferon.foro.servicios.UserDetailsImpl;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario, UserDetailsService {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	UsuarioRepositorio rep;

	@Override
	public <S extends UsuarioVO> S save(S entity) {
		return rep.save(entity);
	}

	@Override
	public <S extends UsuarioVO> Iterable<S> saveAll(Iterable<S> entities) {
		return rep.saveAll(entities);
	}

	@Override
	public Optional<UsuarioVO> findById(Integer id) {
		return rep.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return rep.existsById(id);
	}

	@Override
	public Iterable<UsuarioVO> findAll() {
		return rep.findAll();
	}

	@Override
	public Iterable<UsuarioVO> findAllById(Iterable<Integer> ids) {
		return rep.findAllById(ids);
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
	public void delete(UsuarioVO entity) {
		rep.delete(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends UsuarioVO> entities) {
		rep.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		rep.deleteAll();
	}

	@Override
	public Optional<UsuarioVO> findByUsername(String username) {
		return rep.findByUsername(username);
	}

	@Override
	public Optional<UsuarioVO> findByEmail(String email) {
		return rep.findByEmail(email);
	}

	@Override
	public Boolean existsByEmail(String email) {
		return rep.existsByEmail(email);
	}

	@Override
	public Boolean existsByUsername(String username) {
		return rep.existsByUsername(username);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
		throws UsernameNotFoundException {
		UsuarioVO usuario = rep.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("No se ha podido encontrar el usuario: " + username));
		
		return new UserDetailsImpl(usuario);
	}
	
	@Override
	@Transactional
	public void refresh(UsuarioVO entity) {
		em.refresh(entity);
	}
}
