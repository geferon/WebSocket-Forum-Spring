package net.geferon.foro.modelos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.JoinFormula;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="categorias")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class, 
		property = "id")
public class CategoriaVO {
	public static final int TOTAL_ITEMS_PER_PAGE = 40;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	@Column(unique=true)
	private String nombre;
	@NotNull
	private String descripcion;
	@NotNull
	private String color;

	@NotNull
	private String icono; // Material icons, no need for another table
	//@ManyToOne
	//@JoinColumn(name="idIcono")
	//private IconoVO icono;

	@JsonIgnore
	@OneToMany(mappedBy="categoria", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<HiloVO> hilos = new ArrayList<HiloVO>();

	@JsonIgnore
	@ManyToOne()
	@JoinFormula("("
			+ "SELECT h.id "
			+ "FROM hilos h INNER JOIN publicaciones p "
			+ "ON p.id_hilo = h.id "
			+ "WHERE h.id_categoria = id "
			+ "ORDER BY p.publicado_en DESC "
			+ "LIMIT 1"
		+ ")")
	private HiloVO ultimoHilo;
}
