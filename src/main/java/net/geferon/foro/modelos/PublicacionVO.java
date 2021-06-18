package net.geferon.foro.modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="publicaciones")
public class PublicacionVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int idPublicacion = 1;

	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCreador")
	private UsuarioVO creador;

	@NotNull
	@Lob
	@Size(max=10000)
	@Column(columnDefinition="LONGTEXT")
	private String contenido;

	@CreationTimestamp
	private LocalDateTime publicadoEn = LocalDateTime.now();
	@UpdateTimestamp
	private LocalDateTime editadoEn = this.publicadoEn;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idHilo")
	//@Getter(onMethod = @__( @JsonIgnore ))
    //@Setter
	@JsonBackReference
	private HiloVO hilo;
	
	@Formula("(CEIL(id_publicacion / " + HiloVO.TOTAL_ITEMS_PER_PAGE + "))")
	private int pagina;
	
	/*
	@Formula("(SELECT )")
	private Map<String, number> reaccionesTotales;
	*/
}
