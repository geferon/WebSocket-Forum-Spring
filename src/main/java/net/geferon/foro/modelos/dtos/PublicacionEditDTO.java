package net.geferon.foro.modelos.dtos;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionEditDTO {
	@NotNull
	private String contenido;
}
