package net.geferon.foro.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.geferon.foro.modelos.SubscripcionVO;
import net.geferon.foro.modelos.UsuarioVO;

public interface SubscripcionRepositorio extends JpaRepository<SubscripcionVO, SubscripcionVO.PrimaryKey> {
	public List<SubscripcionVO> findByUsuario(UsuarioVO usuario);
}
