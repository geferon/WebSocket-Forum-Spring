package net.geferon.foro.modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="usuarios")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class, 
		property = "id")
public class UsuarioVO {
	public static String DEFAULT_PICTURE = "perfil/default.jpg";
	
	@NonNull
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NonNull
	@NotBlank
	@Column(unique=true)
	@Size(max=30)
	private String username;
	@NonNull
	@NotBlank
	@Column(unique=true)
	@Email
	@Size(max=50)
	private String email;
	@NonNull
	@NotBlank
	@Size(max=140)
    @JsonIgnore
	private String password;

	private LocalDateTime fechaPasswordChanged = this.fechaRegistro;

	@CreatedDate
	private LocalDateTime fechaRegistro = LocalDateTime.now();
	//private Boolean emailVerified = false;
	private String avatar = DEFAULT_PICTURE;
	

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="usuariosroles",
			joinColumns=@JoinColumn(name="idUsuario"),
			inverseJoinColumns=@JoinColumn(name="idRol")
		)
	@NonNull
	private Set<RolVO> roles = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy="usuario", fetch=FetchType.LAZY)
	private List<SubscripcionVO> subscripciones = new ArrayList<SubscripcionVO>();
	@JsonIgnore
	@OneToMany(mappedBy="creador", fetch=FetchType.LAZY)
	private List<PublicacionVO> publicaciones = new ArrayList<PublicacionVO>();
	@JsonIgnore
	@OneToMany(mappedBy="creador", fetch=FetchType.LAZY)
	private List<HiloVO> hilos = new ArrayList<HiloVO>();
}
