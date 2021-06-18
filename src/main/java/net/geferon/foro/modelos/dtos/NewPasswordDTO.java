package net.geferon.foro.modelos.dtos;

import lombok.Data;

@Data
public class NewPasswordDTO {
	private String oldPassword;
	private String newPassword;
}
