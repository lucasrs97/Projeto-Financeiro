package com.apirest.Financeiro.controller;

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

import com.apirest.Financeiro.model.Receita;
import com.apirest.Financeiro.repository.ReceitaRepository;

@RestController
@RequestMapping(value="/")
@CrossOrigin(origins="*")
public class ReceitaController {
	
	@Autowired
	ReceitaRepository receitaRepository;
	
	@GetMapping("/receitas")
	public List<Receita> listarReceitas() {
		return receitaRepository.findAll();
	}
	
	@GetMapping("/receita/{id}")
	public Receita listarReceitaId(@PathVariable(value="id") long id) {
		return receitaRepository.findById(id);
	}
	
	@PostMapping("/receita")
	public void salvarReceita(@RequestBody Receita receita) {
		receitaRepository.save(receita);
	}
	
	@PutMapping("/receita")
	public void atualizarReceita(@RequestBody Receita receita) {
		receitaRepository.save(receita);
	}
	
	@DeleteMapping("/receita")
	public void deletarReceita(@RequestBody Receita receita) {
		receitaRepository.delete(receita);
	}

}
