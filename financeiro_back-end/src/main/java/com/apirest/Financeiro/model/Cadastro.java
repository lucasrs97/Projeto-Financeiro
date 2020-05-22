package com.apirest.Financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

public class Cadastro implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false)
	private BigDecimal valor;
	
	@Column(nullable = false)
	private Date data;
	
	@Column(nullable = false)
	private String conta;
	
	@Column(nullable = false)
	private String descricao;
	
	@Column(nullable = false)
	private String categoria;
	
	@ManyToOne
	private Usuario usuario;
	
	/*	MÉTODOS GET E SET */
	
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	/*	MÉTODOS GET E SET */

}
