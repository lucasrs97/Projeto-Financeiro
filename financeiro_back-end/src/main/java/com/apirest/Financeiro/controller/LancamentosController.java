package com.apirest.Financeiro.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.Financeiro.model.Despesa;
import com.apirest.Financeiro.model.Receita;
import com.apirest.Financeiro.repository.DespesaRepository;
import com.apirest.Financeiro.repository.ReceitaRepository;
import com.apirest.Financeiro.repository.UsuarioRepository;

@RestController
@RequestMapping(value="/")
@CrossOrigin(origins="*")
public class LancamentosController {
	
	@Autowired
	ReceitaRepository receitaRepository;
	
	@Autowired
	DespesaRepository despesaRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	private Long retornaIdUsuarioLogado() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email;    
		if (principal instanceof UserDetails) {
			email = ((UserDetails)principal).getUsername();
		} else {
			email = principal.toString();
		}
		Long id = usuarioRepository.findByEmail(email).getId();
		
		return id;
	}
	
	/*	INÍCIO	-	RECUPERAÇÃO DE DADOS DA TELA DE LANÇAMENTOS	*/
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
		Long id = retornaIdUsuarioLogado();
		
		List<Receita> arrayReceitas = new ArrayList<Receita>();
		
		List<Receita> receitas = receitaRepository.listarReceitasUsuario(id);
		
		for (Receita receita : receitas) {
			arrayReceitas.add(receita);
		}
		
		return arrayReceitas;
	}
	
	public List<Despesa> listarDespesas() {
		Long id = retornaIdUsuarioLogado();
		
		List<Despesa> arrayDespesas = new ArrayList<Despesa>();
		
		List<Despesa> despesas = despesaRepository.listarDespesasUsuario(id);
		
		for (Despesa despesa : despesas) {
			arrayDespesas.add(despesa);
		}
		
		return arrayDespesas;
	}
	/*	FIM	-	RECUPERAÇÃO DE DADOS DA TELA DE LANÇAMENTOS	*/

}
