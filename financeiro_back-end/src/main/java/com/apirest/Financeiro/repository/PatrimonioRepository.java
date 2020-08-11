package com.apirest.Financeiro.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apirest.Financeiro.model.Patrimonio;

public interface PatrimonioRepository extends JpaRepository<Patrimonio, Long> {
	
	@Query(
	
			value = "SELECT SUM(REGISTROS) as patrimonio " + 
					"FROM ( " + 
					"	SELECT sum(valor)		as REGISTROS FROM patrimonio WHERE identificador_usuario = ?1 AND tipo = 1 AND MONTH(data) = ?2 AND YEAR(data) = ?3 " + 
					"    UNION " + 
					"    SELECT sum(valor) * -1	as REGISTROS FROM patrimonio WHERE identificador_usuario = ?1 AND tipo = 2 AND MONTH(data) = ?2 AND YEAR(data) = ?3 " + 
					") AS REGISTROS",
			nativeQuery = true
			
	)
	public BigDecimal buscarPatrimonioPorMesAno(Long identificadorUsuario, String mes, String ano);
	
	@Query (value = "SELECT * FROM patrimonio WHERE identificador_usuario = ?1", nativeQuery = true)
	public List<Patrimonio> listarPatrimonioUsuario(Long identificadorUsuario);

}
