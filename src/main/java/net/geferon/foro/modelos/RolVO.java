package net.geferon.foro.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="roles")
public class RolVO {
	@Id
	@Enumerated(EnumType.STRING)
	@Column(length = 20, unique = true)
	private ERol nombre;
}
