package com.apirest.Financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apirest.Financeiro.model.Despesa;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

	//	Método do Spring Data responsável por buscar uma única despesa por id.
	Despesa findById(long id);

}
