package net.geferon.foro.repositorios.servicios;

import java.util.Optional;

import net.geferon.foro.modelos.UsuarioVO;

public interface ServicioUsuario {

	<S extends UsuarioVO> S save(S entity);

	<S extends UsuarioVO> Iterable<S> saveAll(Iterable<S> entities);

	Optional<UsuarioVO> findById(Integer id);

	boolean existsById(Integer id);

	Iterable<UsuarioVO> findAll();

	Iterable<UsuarioVO> findAllById(Iterable<Integer> ids);

	long count();

	void deleteById(Integer id);

	void delete(UsuarioVO entity);

	void deleteAll(Iterable<? extends UsuarioVO> entities);

	void deleteAll();

	public Optional<UsuarioVO> findByUsername(String nombreUsuario);
	public Optional<UsuarioVO> findByEmail(String correo);
	Boolean existsByEmail(String email);
	Boolean existsByUsername(String username);

	void refresh(UsuarioVO entity);
}