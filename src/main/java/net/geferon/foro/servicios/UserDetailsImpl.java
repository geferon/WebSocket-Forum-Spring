package net.geferon.foro.servicios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import net.geferon.foro.modelos.RolVO;
import net.geferon.foro.modelos.UsuarioVO;

public class UserDetailsImpl implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1296842398004983018L;

	@Getter
	@JsonIgnore
	private UsuarioVO usuario;

	public UserDetailsImpl(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<RolVO> roles = usuario.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (RolVO rol : roles) {
			authorities.add(new SimpleGrantedAuthority(rol.getNombre().name()));
		}

		return authorities;
	}
	
	public Integer getId() {
		return usuario.getId();
	}
	
	public String getEmail() {
		return usuario.getEmail();
	}

	@Override
	public String getPassword() {
		return usuario.getPassword();
	}

	@Override
	public String getUsername() {
		return usuario.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
