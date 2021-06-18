package net.geferon.foro.modelos.dtos;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastReadDTO {
	@NotNull
	public Long idPublicacion;
	@NotNull
	public LocalDateTime lastRead;
}
