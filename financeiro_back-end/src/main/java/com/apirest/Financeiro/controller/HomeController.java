package com.apirest.Financeiro.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.Financeiro.model.Despesa;
import com.apirest.Financeiro.model.Patrimonio;
import com.apirest.Financeiro.model.Receita;
import com.apirest.Financeiro.repository.DespesaRepository;
import com.apirest.Financeiro.repository.PatrimonioRepository;
import com.apirest.Financeiro.repository.ReceitaRepository;

@RestController
@RequestMapping(value="/")
@CrossOrigin(origins="*")
public class HomeController {
	
	@Autowired
	ReceitaRepository receitaRepository;
	
	@Autowired
	DespesaRepository despesaRepository;
	
	@Autowired
	PatrimonioRepository patrimonioRepository;
	
	@GetMapping("/home")
	public List<BigDecimal> listarDados() {
		
		//	RECEITAS
		List<Receita> receitas = receitaRepository.findAll();
		
		BigDecimal totalReceitas = new BigDecimal(0);
		for (Receita receita : receitas) {
			totalReceitas = totalReceitas.add(receita.getValor());
		}
		//	RECEITAS
		
		//	DESPESAS
		List<Despesa> despesas = despesaRepository.findAll();
		
		BigDecimal totalDespesas = new BigDecimal(0);
		for (Despesa despesa : despesas) {
			totalDespesas = totalDespesas.add(despesa.getValor());
		}
		//	DESPESAS
		
		//	PATRIMÔNIO
		List<Patrimonio> listaPatrimonio = patrimonioRepository.findAll();
		
		BigDecimal totalPatrimonio = new BigDecimal(0);		
		for (Patrimonio patrimonio : listaPatrimonio) {
			if(patrimonio.getTipo() == 1) {							//	DINHEIRO GUARDADO
				totalPatrimonio = totalPatrimonio.add(patrimonio.getValor());
			} else if (patrimonio.getTipo() == 2) {					//	DINHEIRO RESGATADO
				totalPatrimonio = totalPatrimonio.subtract(patrimonio.getValor());
			}
		}
		//	PATRIMÔNIO
		
		//	CAIXA MENSAL
		BigDecimal caixa = new BigDecimal(0);
		caixa = totalReceitas.subtract(totalDespesas);
		caixa = caixa.subtract(totalPatrimonio);
		//	CAIXA MENSAL
		
		List<BigDecimal> listaDados = new ArrayList<BigDecimal>();
		
		listaDados.add(totalReceitas);
		listaDados.add(totalDespesas);
		listaDados.add(caixa);
		listaDados.add(totalPatrimonio);
		
		return listaDados;
		
	}
	
	@GetMapping("/releases")
	public Map<Integer, List<Object>> listarLancamentos() {
		
		Map<Integer, List<Object>> lancamentos = new HashMap<Integer, List<Object>>();
		
		List<Object> lancamentosReceitas = new ArrayList<>();
		List<Object> lancamentosDespesas = new ArrayList<>();
		
		List<Receita> listaReceitas = listarReceitas();
		List<Despesa> listaDespesas = listarDespesas();
		
		for (Receita receitas : listaReceitas) {
			lancamentosReceitas.add(receitas);
		}
		
		for (Despesa despesas : listaDespesas) {
			lancamentosDespesas.add(despesas);
		}
		
		lancamentos.put(1, lancamentosReceitas);
		lancamentos.put(2, lancamentosDespesas);
		
		return lancamentos;
	}
	
	public List<Receita> listarReceitas() {
		List<Receita> arrayReceitas = new ArrayList<Receita>();
		
		List<Receita> receitas = receitaRepository.findAll();
		
		for (Receita receita : receitas) {
			arrayReceitas.add(receita);
		}
		
		return arrayReceitas;
	}
	
	public List<Despesa> listarDespesas() {
		List<Despesa> arrayDespesas = new ArrayList<Despesa>();
		
		List<Despesa> despesas = despesaRepository.findAll();
		
		for (Despesa despesa : despesas) {
			arrayDespesas.add(despesa);
		}
		
		return arrayDespesas;
	}

}
