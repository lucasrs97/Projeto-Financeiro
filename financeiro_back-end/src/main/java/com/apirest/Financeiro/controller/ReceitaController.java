package com.apirest.Financeiro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("/receita")
	public void salvarReceita(@RequestBody Receita receita) {
		receitaRepository.save(receita);
	}
	
	@DeleteMapping("/delete-receita")
	public void deletarReceita(@RequestBody Receita receita) {
		receitaRepository.delete(receita);
	}

}
