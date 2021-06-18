package net.geferon.foro.modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="hilos")
public class HiloVO {
	public static final int TOTAL_ITEMS_PER_PAGE = 20;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String titulo;
	@CreationTimestamp
	private LocalDateTime fechaCreacion = LocalDateTime.now();

	@NotNull
	@ManyToOne()
	@JoinColumn(name="idCreador")
	private UsuarioVO creador;

	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCategoria")
	private CategoriaVO categoria;

	@JsonIgnore
	@OneToMany(mappedBy="hilo", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<PublicacionVO> publicaciones = new ArrayList<PublicacionVO>();
	@JsonIgnore
	@OneToMany(mappedBy="hilo", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<SubscripcionVO> subscripciones = new ArrayList<SubscripcionVO>();

	private Integer publicacionesTotales = 1;
	
	@Formula("CEIL(publicaciones_totales / " + HiloVO.TOTAL_ITEMS_PER_PAGE + ")")
	private Integer paginasTotales;
	
	@ManyToOne()
	@JoinFormula("("
			+ "SELECT p.id "
			+ "FROM publicaciones p "
			+ "WHERE p.id_hilo = id "
			+ "ORDER BY p.publicado_en DESC "
			+ "LIMIT 1"
		+ ")")
	private PublicacionVO ultimaPublicacion;
}
