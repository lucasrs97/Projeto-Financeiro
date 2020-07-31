package com.apirest.Financeiro.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
		
		patrimonioRepository.save(patrimonio);
	}
	
	@PostMapping("/resgatar_patrimonio")
	public void retirarPatrimonio(@RequestBody Patrimonio patrimonio) {
		patrimonio.setTipo(2);
		
		patrimonioRepository.save(patrimonio);
	}

}
