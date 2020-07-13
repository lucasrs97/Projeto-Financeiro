package com.apirest.Financeiro.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apirest.Financeiro.model.Patrimonio;

public interface PatrimonioRepository extends JpaRepository<Patrimonio, Long> {
	
	@Query(
	
			value = "SELECT SUM(REGISTROS) as PATRIMONIO " + 
					"FROM ( " + 
					"	SELECT sum(valor)		as REGISTROS FROM patrimonio WHERE tipo = 1 AND MONTH(data) = ?1 AND YEAR(data) = ?2 " + 
					"    UNION " + 
					"    SELECT sum(valor) * -1	as REGISTROS FROM patrimonio WHERE tipo = 2 AND MONTH(data) = ?1 AND YEAR(data) = ?2 " + 
					") AS REGISTROS",
			nativeQuery = true
			
	)
	public BigDecimal buscarPatrimonioNoMesAno(String mes, String ano);

}
