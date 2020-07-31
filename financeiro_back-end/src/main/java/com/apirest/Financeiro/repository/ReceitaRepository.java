package com.apirest.Financeiro.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apirest.Financeiro.model.Receita;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {

	@Query (value = "SELECT * FROM RECEITAS WHERE IDENTIFICADOR_USUARIO = ?1", nativeQuery = true)
	public List<Receita> listarReceitasUsuario(Long identificadorUsuario);
	
	@Query (value = "SELECT SUM(VALOR) FROM RECEITAS WHERE IDENTIFICADOR_USUARIO = ?1", nativeQuery = true)
	public BigDecimal totalReceitas(Long identificadorUsuario);
	
	@Query (value = "SELECT SUM(VALOR) FROM RECEITAS WHERE CATEGORIA = ?1 AND IDENTIFICADOR_USUARIO = ?2", nativeQuery = true)
	public BigDecimal totalReceitasPorCategoria(String categoria, Long identificadorUsuario);
	
}
