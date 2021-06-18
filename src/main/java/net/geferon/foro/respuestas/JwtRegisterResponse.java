package net.geferon.foro.respuestas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRegisterResponse {
	private String token;
	private String message;
}
