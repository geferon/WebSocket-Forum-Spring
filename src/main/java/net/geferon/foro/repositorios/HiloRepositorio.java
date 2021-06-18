package net.geferon.foro.repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.geferon.foro.modelos.CategoriaVO;
import net.geferon.foro.modelos.HiloVO;

public interface HiloRepositorio extends JpaRepository<HiloVO, Long> {
	public Page<HiloVO> findByTituloContaining(String contenido, Pageable pageable);
	public Page<HiloVO> findAllByCategoria(CategoriaVO categoria, Pageable pageable);
	
	@Query(
			value = "SELECT h "
			+ "FROM CategoriaVO c INNER JOIN c.hilos h LEFT JOIN h.ultimaPublicacion p "
			+ "WHERE c = ?1 "
			+ "ORDER BY COALESCE(p.publicadoEn, h.fechaCreacion) DESC",
			countQuery = "SELECT COUNT(h) "
				+ "FROM CategoriaVO c INNER JOIN c.hilos h "
				+ "WHERE c = ?1"

			/*
			value = "SELECT h.* "
					+ "FROM hilos h"
					+ " INNER JOIN publicaciones p"
					+ " ON p.id_categoria = h.id"
					+ "WHERE h.id_categoria = ?1 AND p.id = ("
						+ "SELECT p2.id, MAX(p2.publicado_en) "
						+ "FROM publicaciones p2 "
						+ "WHERE p2.id_hilo = p.id_hilo "
						+ "GROUP BY p2.id, p2.publicado_en"
					+ ") "
					+ "ORDER BY p.publicado_en DESC",
			countQuery = "SELECT COUNT(h.id) "
				+ "FROM hilos h "
				+ "WHERE h.id_categoria = ?1",
			nativeQuery = true
				*/
		)
	public Page<HiloVO> findAllByCategoriaWithPagination(CategoriaVO categoria, Pageable pageable);
}
