package com.apirest.Financeiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

/*	ESTA ANOTAÇÃO FOI NEECSSÁRIA, POIS SEM ELA O SPRING NÃO ESTAVA CONSEGUINDO ENCONTRAR O BEAN "HOME"
 *  NA HORA DE ESCANEAR AS CLASSES.
 */
//@ComponentScan("com.apirest.Financeiro")
public class FinanceiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceiroApplication.class, args);
	}

}
