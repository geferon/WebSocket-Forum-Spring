package net.geferon.foro.repositorios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.modelos.PublicacionVO;
import net.geferon.foro.modelos.UsuarioVO;

public interface PublicacionRepositorio extends JpaRepository<PublicacionVO, Long> {
	//@Query("SELECT new net.geferon.foro.modelos.PublicacionReaccion(r.icono.id, COUNT(r.id)) FROM ReaccionVO r WHERE r.publicacion = :publicacion GROUP BY r.icono")
	//public List<PublicacionReaccion> getResumenReacciones(PublicacionVO publicacion);
	public List<PublicacionVO> findByContenidoContaining(String contenido);
	public Page<PublicacionVO> findAllByHilo(HiloVO hilo, Pageable pageable);
	public Page<PublicacionVO> findAllByCreador(UsuarioVO creador, Pageable pageable);
}
