package net.geferon.foro.respuestas;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RegistroFormulario {
	@NotBlank
	@Size(min=4, max=30)
	private String username;
    @NotBlank
    @Size(min=6, max=40)
	private String password;
	@NotBlank
	@Size(max=50)
	@Email
	private String email;
}
