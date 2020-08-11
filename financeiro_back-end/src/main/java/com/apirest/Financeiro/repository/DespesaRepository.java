package com.apirest.Financeiro.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apirest.Financeiro.model.Despesa;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

	@Query (value = "SELECT * FROM DESPESAS WHERE IDENTIFICADOR_USUARIO = ?1", nativeQuery = true)
	public List<Despesa> listarDespesasUsuario(Long identificadorUsuario);
	
	@Query (value = "SELECT SUM(VALOR) FROM DESPESAS WHERE IDENTIFICADOR_USUARIO = ?1", nativeQuery = true)
	public BigDecimal totalDespesas(Long identificadorUsuario);
	
	@Query (value = "SELECT SUM(VALOR) FROM DESPESAS WHERE CATEGORIA = ?1 AND IDENTIFICADOR_USUARIO = ?2", nativeQuery = true)
	public BigDecimal totalDespesasPorCategoria(String categoria, Long identificadorUsuario);
	
	@Query (value = "SELECT SUM(VALOR) FROM DESPESAS WHERE IDENTIFICADOR_USUARIO = ?1 AND MONTH(data) = ?2 AND YEAR(data) = ?3", nativeQuery = true)
	public BigDecimal totalDespesasPorMesAno(Long identificadorUsuario, String mes, String ano);

}
