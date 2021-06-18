package net.geferon.foro.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.geferon.foro.modelos.ERol;
import net.geferon.foro.modelos.RolVO;

public interface RolRepositorio extends JpaRepository<RolVO, Long> {
	Optional<RolVO> findByNombre(ERol nombre);
}
