package com.apirest.Financeiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.Financeiro.model.Usuario;
import com.apirest.Financeiro.repository.UsuarioRepository;

@RestController
@RequestMapping(value="/")
@CrossOrigin(origins="*")
public class LoginController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Usuario usuarioLogin) {
		
		Usuario usuarioBanco = usuarioRepository.findByEmail(usuarioLogin.getEmail());
		
		if(usuarioBanco == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			if(passwordEncoder().matches(usuarioLogin.getSenha(), usuarioBanco.getSenha())) {
				
				usuarioBanco.setSenha(usuarioLogin.getSenha());
				
				return new ResponseEntity<>(usuarioBanco, HttpStatus.OK);
//				return new ResponseEntity<>(usuarioLogin, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
		}
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

}
