package com.apirest.Financeiro.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.Financeiro.model.Patrimonio;
import com.apirest.Financeiro.repository.PatrimonioRepository;

@RestController
@RequestMapping(value="/")
@CrossOrigin(origins="*")
public class PatrimonioController {
	
	@Autowired
	PatrimonioRepository patrimonioRepository;
	
	@PostMapping("/guardar_patrimonio")
	public void salvarPatrimonio(@RequestBody Patrimonio patrimonio) {
		patrimonio.setTipo(1);
//		LocalDate data = LocalDate.now().minusMonths(1);
//		patrimonio.setData(data);
		
		
		patrimonioRepository.save(patrimonio);
	}
	
	@PostMapping("/resgatar_patrimonio")
	public void retirarPatrimonio(@RequestBody Patrimonio patrimonio) {
		patrimonio.setTipo(2);
//		LocalDate data = LocalDate.now().minusMonths(1);
//		patrimonio.setData(data);
		
		patrimonioRepository.save(patrimonio);
	}
	
	@GetMapping("/patrimonio")
	public List<Patrimonio> ListarPatrimonios() {
		return patrimonioRepository.findAll();
	}
	
	@GetMapping("/listarHistoricoPatrimonio")
	public Map<List<String>, List<BigDecimal>> listarPatrimonioMensal() {
		
		Map< List<String>, List<BigDecimal> > patrimonioMensal = new HashMap< List<String>, List<BigDecimal> >();
		
		List<String> mesAno = listarMeses();
		
		List<BigDecimal> patrimonio = new ArrayList<BigDecimal>();
		
		for (String stringMesAno : mesAno) {
			String mes = stringMesAno.substring(0, 2);
			String ano = stringMesAno.substring(3, 7);
			
			patrimonio.add(listarHistoricoMensalPatrimonio(mes, ano));
		}
		
		patrimonioMensal.put(mesAno, patrimonio);
		
		return patrimonioMensal;
	}
	
	public List<String> listarMeses() {
		
		LocalDate mesAnoCadastro = LocalDate.now();
		mesAnoCadastro = mesAnoCadastro.minusMonths(2);
		
		int mes = mesAnoCadastro.getMonth().getValue();
		int ano = mesAnoCadastro.getYear();
		
		List<String> arrayMesAno = new ArrayList<String>();
		
		while(mes <= LocalDate.now().getMonth().getValue()) {
			
			if(mes == 10 || mes == 11 || mes == 12) {
				arrayMesAno.add(mes + "/" + ano);
			} else {
				arrayMesAno.add("0" + mes + "/" + ano);
			}
			
			mes = mes + 1;
		}
		
		return arrayMesAno;
	}
	
	public BigDecimal listarHistoricoMensalPatrimonio(String mes, String ano) {
		return patrimonioRepository.buscarPatrimonioNoMesAno(mes, ano);
	}
	
	

}
