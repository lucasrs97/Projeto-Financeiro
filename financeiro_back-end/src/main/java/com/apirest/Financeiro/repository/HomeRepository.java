package com.apirest.Financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apirest.Financeiro.model.Receita;

public interface HomeRepository extends JpaRepository<Receita, Long> {

}
