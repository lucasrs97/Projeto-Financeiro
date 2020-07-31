package com.apirest.Financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="despesas")
public class Despesa implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private BigDecimal valor;
	
	@Column(nullable = false)
	private LocalDate data;
	
//	@Column(nullable = false)
//	private String conta;
	
	@Column(nullable = false)
	private String descricao;
	
	@Column(nullable = false)
	private String categoria;
	
	private Long identificadorUsuario;

	/*	MÉTODOS GET E SET */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public String getValorFormatado() {
		
		String formato = "###,###.00"; 
		DecimalFormat df = new DecimalFormat(formato);
		
		String valorFormatado = df.format(getValor());
		
		return "R$ " + valorFormatado;
	}

	/*
	 * Método privado para que apenas o método de formatação da data o acesse.
	 * Desta forma, o Controller não enxergará o método e não o incluirá no JSON enviado como resposta à requisição.
	 * 
	 * **RETIRADO**
	 */
	public LocalDate getData() {
		/*
		 *	Por algum motivo que eu ainda não consegui decifrar, a data está vindo com um dia a menos.
			No banco ela é cadastrada corretamente, mas no método get só vem com o dia anterior!
			A solução encontrada foi usar o "plusDays" do LocalDate para acrescentar um dia na data e,
			dessa forma, retorna a data que está cadastrada no banco.
		 */
		LocalDate dataCorreta = data.plusDays(1);
		
		return dataCorreta;
	}
	
	public void setData(LocalDate data) {
		this.data = data;
	}
	
	public String getDataFormatada() {
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		return getData().format(formato);
	}

//	public String getConta() {
//		return conta;
//	}
//
//	public void setConta(String conta) {
//		this.conta = conta;
//	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Long getIdentificadorUsuario() {
		return identificadorUsuario;
	}
	
	public void setIdentificadorUsuario(Long identificadorUsuario) {
		this.identificadorUsuario = identificadorUsuario;
	}
	
	public String getTipo() {
		return "Despesa";
	}
	/*	MÉTODOS GET E SET */

}
