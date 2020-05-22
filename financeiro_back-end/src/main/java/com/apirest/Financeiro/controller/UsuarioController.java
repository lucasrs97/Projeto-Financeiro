package com.apirest.Financeiro.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.Financeiro.model.Despesa;
import com.apirest.Financeiro.model.Receita;
import com.apirest.Financeiro.model.Usuario;
import com.apirest.Financeiro.repository.DespesaRepository;
import com.apirest.Financeiro.repository.ReceitaRepository;
import com.apirest.Financeiro.repository.UsuarioRepository;

@RestController
@RequestMapping(value="/")
@CrossOrigin(origins="*")
public class UsuarioController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	ReceitaRepository receitaRepository;
	
	@Autowired
	DespesaRepository despesaRepository;
	
	//	MÉTODO NÃO NECESSÁRIO
	@GetMapping("/usuarios")
	public List<Usuario> ListarUsuarios() {
		return usuarioRepository.findAll();
	}
	
	@GetMapping("/usuario/{id}")
	public Usuario ListarUsuarioId(@PathVariable(value="id") long id) {
		return usuarioRepository.findById(id);
	}
	
	@PostMapping("/usuario")
	public void salvarUsuario(@RequestBody Usuario usuario) {
		usuarioRepository.save(usuario);
	}
	
	@PutMapping("/usuario")
	public void atualizarUsuario(@RequestBody Usuario usuario) {
		usuarioRepository.save(usuario);
	}
	
	@DeleteMapping("/usuario")
	public void deletarUsuario(@RequestBody Usuario usuario) {
		usuarioRepository.delete(usuario);
	}
	
	@GetMapping("/home")
	public List<BigDecimal> listarDados() {
		
		List<Receita> receitas = receitaRepository.findAll();
		
		BigDecimal totalReceitas = new BigDecimal(0);
		BigDecimal caixa = new BigDecimal(0);
		
		for (Receita receita : receitas) {
			totalReceitas = totalReceitas.add(receita.getValor());
			caixa = caixa.add(receita.getValor());
		}
		
		List<Despesa> despesas = despesaRepository.findAll();
		BigDecimal totalDespesas = new BigDecimal(0);
		for (Despesa despesa : despesas) {
			totalDespesas = totalDespesas.add(despesa.getValor());
		}
		
		caixa = totalReceitas.subtract(totalDespesas);
		
		List<BigDecimal> listaDados = new ArrayList<BigDecimal>();
		
		listaDados.add(totalReceitas);
		listaDados.add(totalDespesas);
		listaDados.add(caixa);
		
		return listaDados;
		
	}
	
}
