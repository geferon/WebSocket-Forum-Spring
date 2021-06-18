package net.geferon.foro.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import net.geferon.foro.modelos.HiloVO;
import net.geferon.foro.modelos.PublicacionVO;
import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.servicios.UserDetailsImpl;

@Component
public class SeguridadControladores {
	public boolean canEditHilo(HiloVO hilo, Authentication auth) {
		return canEditUser(hilo.getCreador(), auth);
	}
	
	public boolean canDeleteHilo(HiloVO hilo, Authentication auth) {
		return canEditUser(hilo.getCreador(), auth);
	}
	
	public boolean canEditUser(UsuarioVO usuario, Authentication auth) {
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		return usuario.getId().equals(userDetails.getId()) ||
				auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	}

	public boolean canEditPublicacion(PublicacionVO publicacionToUpdate, Authentication auth) {
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		return publicacionToUpdate.getCreador().getId().equals(userDetails.getUsuario().getId()) ||
				auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	}
}
