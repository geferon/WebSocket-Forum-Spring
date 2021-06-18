package net.geferon.foro.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="subscripciones")
public class SubscripcionVO {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Embeddable
	public static class PrimaryKey implements Serializable {
		private static final long serialVersionUID = -7239699345405687634L;
		private Integer id_usuario;
		private Long id_hilo;
	}

	@EmbeddedId
	@JsonIgnore
	private PrimaryKey id;

	@JsonIgnore
	@ManyToOne
	@MapsId("id_usuario")
	@JoinColumn(name="idUsuario")
	private UsuarioVO usuario;
	@ManyToOne
	@MapsId("id_hilo")
	@JoinColumn(name="idHilo")
	private HiloVO hilo;

	@CreationTimestamp
	private LocalDateTime subscritoEn = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name="idUltimaPublicacion")
	private PublicacionVO ultimaPublicacionLeida;

	@ManyToOne()
	@JoinFormula("("
			+ "SELECT p.id "
			+ "FROM publicaciones p "
			+ "WHERE p.id_hilo = id_hilo AND p.id > id_ultima_publicacion "
			+ "ORDER BY p.publicado_en ASC "
			+ "LIMIT 1"
		+ ")")
	private PublicacionVO nextPublicacion;
	
	@Formula("(SELECT COUNT(p.id) FROM publicaciones p WHERE p.id_hilo = id_hilo AND p.id > id_ultima_publicacion)")
	private Integer publicacionesPendientes;


	@Formula("(SELECT MAX(p.publicado_en) FROM publicaciones p WHERE p.id_hilo = id_hilo)")
	private LocalDateTime ultimaPublicacionFecha;
}
