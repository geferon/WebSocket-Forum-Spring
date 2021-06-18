package net.geferon.foro.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.geferon.foro.modelos.UsuarioVO;

public interface UsuarioRepositorio extends JpaRepository<UsuarioVO, Integer> {
	public Optional<UsuarioVO> findByUsername(String username);
	public Optional<UsuarioVO> findByEmail(String correo);
	public Boolean existsByEmail(String email);
	public Boolean existsByUsername(String username);
}
