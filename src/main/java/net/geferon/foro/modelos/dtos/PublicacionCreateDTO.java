package net.geferon.foro.modelos.dtos;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.geferon.foro.modelos.HiloVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionCreateDTO {
	@NotNull
	private String contenido;
	private HiloVO hilo;
}
