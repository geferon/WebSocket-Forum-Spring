package net.geferon.foro.respuestas;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginFormulario {
	@NotBlank
	private String username;

	@NotBlank
	private String password;
}
