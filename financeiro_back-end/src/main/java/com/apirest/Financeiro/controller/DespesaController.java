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

import com.apirest.Financeiro.model.Despesa;
import com.apirest.Financeiro.repository.DespesaRepository;

@RestController
@RequestMapping(value="/")
@CrossOrigin(origins="*")
public class DespesaController {
	
	@Autowired
	DespesaRepository despesaRepository;
	
	@GetMapping("/despesas")
	public List<Despesa> listardespesas() {
		return despesaRepository.findAll();
	}
	
	@PostMapping("/despesa")
	public void salvarDespesa(@RequestBody Despesa despesa) {
		despesaRepository.save(despesa);	
	}
	
	@DeleteMapping("/delete-despesa")
	public void deletarDespesa(@RequestBody Despesa despesa) {
		despesaRepository.delete(despesa);
	}

}
