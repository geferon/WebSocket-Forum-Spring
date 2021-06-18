package net.geferon.foro.modelos.dtos;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.modelos.PublicacionVO;
import net.geferon.foro.modelos.UsuarioVO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HiloDTO {
	@NotNull
	private String titulo;
}
