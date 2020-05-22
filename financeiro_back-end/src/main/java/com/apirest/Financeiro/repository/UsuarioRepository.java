package com.apirest.Financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apirest.Financeiro.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	//	Método do Spring Data responsável por buscar um único usuário por id.
	Usuario findById(long id);

}
