package com.apirest.Financeiro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.apirest.Financeiro.model.Usuario;
import com.apirest.Financeiro.repository.UsuarioRepository;

@Service("userDetailsService")
public class ImplementsUserDetailsService implements UserDetailsService {

	@Autowired
	UsuarioRepository ur;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Usuario usuario = ur.findByEmail(email);
		
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado.");
		}
		
		return usuario;
	}

}
