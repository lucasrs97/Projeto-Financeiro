package com.apirest.Financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apirest.Financeiro.model.Receita;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {

	//	Método do Spring Data responsável por buscar uma única receita por id.
	Receita findById(long id);
	
}
