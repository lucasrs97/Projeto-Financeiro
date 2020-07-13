package com.apirest.Financeiro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	//	MÉTODO NÃO NECESSÁRIO
	@GetMapping("/usuarios")
	public List<Usuario> ListarUsuarios() {
		return usuarioRepository.findAll();
	}
	
	@GetMapping("/usuario/{id}")
	public Usuario ListarUsuarioId(@PathVariable(value="id") long id) {
		return usuarioRepository.findById(id);
	}
	
	@PostMapping("/usuario")
	public void salvarUsuario(@RequestBody Usuario usuario) {
		usuarioRepository.save(usuario);
	}
	
	@PutMapping("/usuario")
	public void atualizarUsuario(@RequestBody Usuario usuario) {
		usuarioRepository.save(usuario);
	}
	
	@DeleteMapping("/usuario")
	public void deletarUsuario(@RequestBody Usuario usuario) {
		usuarioRepository.delete(usuario);
	}
	
}
