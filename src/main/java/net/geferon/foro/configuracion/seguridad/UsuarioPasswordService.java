package net.geferon.foro.configuracion.seguridad;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

import net.geferon.foro.modelos.UsuarioVO;
import net.geferon.foro.repositorios.servicios.ServicioUsuario;
import net.geferon.foro.servicios.UserDetailsImpl;

@Service
@Transactional
public class UsuarioPasswordService implements UserDetailsPasswordService {
	@Autowired
	private ServicioUsuario servUsuario;

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		UsuarioVO usuario = ((UserDetailsImpl) user).getUsuario();
		usuario.setPassword(newPassword);
		servUsuario.save(usuario);
		return user;
	}
}
