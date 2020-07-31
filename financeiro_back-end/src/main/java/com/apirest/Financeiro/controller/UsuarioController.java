package com.apirest.Financeiro.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.Financeiro.model.Usuario;
import com.apirest.Financeiro.repository.UsuarioRepository;

@RestController
@RequestMapping(value="/")
@CrossOrigin(origins="*")
public class UsuarioController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@PostMapping("/usuario")
	public void salvarUsuario(@RequestBody Usuario usuario) {
		
		//	SENHA CRIPTOGRAFADA
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		
		//	DATA ATUAL DO SISTEMA
		LocalDate dataCadastro = LocalDate.now();
		usuario.setDataCadastro(dataCadastro);
		
		usuarioRepository.save(usuario);
	}
	
	@DeleteMapping("/usuario")
	public void deletarUsuario(@RequestBody Usuario usuario) {
		usuarioRepository.delete(usuario);
	}
	
}
