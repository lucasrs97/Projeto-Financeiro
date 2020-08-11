package com.apirest.Financeiro.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<?> salvarUsuario(@RequestBody @Valid Usuario usuario) {
		String senhaSemCriptografia = usuario.getSenha();
		
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		
		//	SE O ID FOR IGUAL A 0, ENTÃO É UMA INCLUSÃO
		if(usuario.getId() == 0) {
			//	SE O E-MAIL JÁ EXISTE NO BANCO, É LANÇADO O STATUS CODE 406. CASO CONTRÁRIO, O CADASTRO SEGUE NORMALMENTE
			if(usuarioRepository.findByEmail(usuario.getEmail()) != null) {
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			} else {
				LocalDate dataCadastro = LocalDate.now();
				usuario.setDataCadastro(dataCadastro);
			}
		}
		usuarioRepository.save(usuario);
		
		usuario.setSenha(senhaSemCriptografia);	
		return new ResponseEntity<>(usuario, HttpStatus.OK);

	}
	
	@DeleteMapping("/delete-usuario")
	public void deletarUsuario(@RequestBody Usuario usuario) {
		usuarioRepository.delete(usuario);
	}
	
}
