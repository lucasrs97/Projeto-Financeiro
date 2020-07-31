package com.apirest.Financeiro.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
import com.apirest.Financeiro.model.Patrimonio;
import com.apirest.Financeiro.model.Receita;
import com.apirest.Financeiro.repository.DespesaRepository;
import com.apirest.Financeiro.repository.PatrimonioRepository;
import com.apirest.Financeiro.repository.ReceitaRepository;
import com.apirest.Financeiro.repository.UsuarioRepository;

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
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	private Long retornaIdUsuarioLogado(String email) {
		return usuarioRepository.findByEmail(email).getId();
	}
	
	
	@GetMapping("/home")
	public List<Object> listar() {
		
		List<Object> dadosHome = new ArrayList<Object>();
		
		List<BigDecimal> dadosCard = recuperaDadosCard();
		
		List<String> mesesGraficoBarra = listarMesesGraficoBarra();
		List<BigDecimal> valoresGraficoBarra = listarValoresGraficoBarra();
		
		mesesGraficoBarra.add("08/2020");
		valoresGraficoBarra.add(new BigDecimal(1220));
		
		mesesGraficoBarra.add("09/2020");
		valoresGraficoBarra.add(new BigDecimal(3450));
		
		mesesGraficoBarra.add("10/2020");
		valoresGraficoBarra.add(new BigDecimal(5430));
		
		mesesGraficoBarra.add("11/2020");
		valoresGraficoBarra.add(new BigDecimal(4100));
		
		mesesGraficoBarra.add("12/2020");
		valoresGraficoBarra.add(new BigDecimal(6000));
		
		List<BigDecimal> porcetagemReceitas = listarPercentuaisReceitasGraficoRosquinha();
		List<BigDecimal> porcetagemDespesas = listarPercentuaisDespesasGraficoRosquinha();
		
		dadosHome.add(dadosCard);
		
		dadosHome.add(mesesGraficoBarra);
		dadosHome.add(valoresGraficoBarra);
		
		dadosHome.add(porcetagemReceitas);
		dadosHome.add(porcetagemDespesas);
		
		return dadosHome;
	}
	
	/*	INÍCIO	-	RECUPERAÇÃO DE DADOS DOS CARDS	*/
	public List<BigDecimal> recuperaDadosCard() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email;    
		if (principal instanceof UserDetails) {
			email = ((UserDetails)principal).getUsername();
		} else {
			email = principal.toString();
		}
		Long id = retornaIdUsuarioLogado(email);
		
		//	RECEITAS
		List<Receita> receitas = receitaRepository.listarReceitasUsuario(id);
		
		BigDecimal totalReceitas = new BigDecimal(0);
		for (Receita receita : receitas) {
			totalReceitas = totalReceitas.add(receita.getValor());
		}
		//	RECEITAS
		
		//	DESPESAS
		List<Despesa> despesas = despesaRepository.listarDespesasUsuario(id);
		
		BigDecimal totalDespesas = new BigDecimal(0);
		for (Despesa despesa : despesas) {
			totalDespesas = totalDespesas.add(despesa.getValor());
		}
		//	DESPESAS
		
		//	PATRIMÔNIO
		List<Patrimonio> listaPatrimonio = patrimonioRepository.listarPatrimonioUsuario(id);
		
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
	/*	FIM		-	RECUPERAÇÃO DE DADOS DOS CARDS	*/
	
	/*	INÍCIO	-	RECUPERAÇÃO DE DADOS DO GRÁFICO DE BARRAS	*/
	public List<BigDecimal> listarValoresGraficoBarra() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email;    
		if (principal instanceof UserDetails) {
			email = ((UserDetails)principal).getUsername();
		} else {
			email = principal.toString();
		}
		Long id = retornaIdUsuarioLogado(email);
		
		List<String> arrayMesAno = listarMesesGraficoBarra();
		List<BigDecimal> arrayPatrimonio = new ArrayList<BigDecimal>();
		
		BigDecimal patrimonioMesAtual = new BigDecimal(0);
		BigDecimal patrimonioAnterior = new BigDecimal(0);
		
		for (String mesAno : arrayMesAno) {
			String mes = mesAno.substring(0, 2);
			String ano = mesAno.substring(3, 7);
			
			patrimonioMesAtual = listarHistoricoMensalPatrimonio(id, mes, ano);
			
			if(patrimonioMesAtual == null) {
				arrayPatrimonio.add(patrimonioAnterior);
			} else {
				arrayPatrimonio.add(patrimonioAnterior.add(patrimonioMesAtual));
				patrimonioAnterior = patrimonioMesAtual;
			}
		}
		
		return arrayPatrimonio;
	}
	
	public List<String> listarMesesGraficoBarra() {
		
		LocalDate mesAnoCadastro = LocalDate.now();
		mesAnoCadastro = mesAnoCadastro.minusMonths(1);
		
		int mes = mesAnoCadastro.getMonth().getValue();
		int ano = mesAnoCadastro.getYear();
		
		List<String> arrayMesAno = new ArrayList<String>();
		
		while(mes <= LocalDate.now().getMonth().getValue()) {
			
			if(mes == 10 || mes == 11 || mes == 12) {
				arrayMesAno.add(mes + "/" + ano);
			} else {
				arrayMesAno.add("0" + mes + "/" + ano);
			}
			
			mes = mes + 1;
		}
		
		return arrayMesAno;
	}
	
	public BigDecimal listarHistoricoMensalPatrimonio(Long identificadorUsuario, String mes, String ano) {
		return patrimonioRepository.buscarPatrimonioNoMesAno(identificadorUsuario, mes, ano);
	}
	/*	FIM		-	RECUPERAÇÃO DE DADOS DO GRÁFICO DE BARRAS	*/
	
	/*	INÍCIO	-	RECUPERAÇÃO DE DADOS DO GRÁFICO DE DOUGHNUT	*/
	public List<BigDecimal> listarPercentuaisReceitasGraficoRosquinha() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email;    
		if (principal instanceof UserDetails) {
			email = ((UserDetails)principal).getUsername();
		} else {
			email = principal.toString();
		}
		Long id = retornaIdUsuarioLogado(email);
		
		BigDecimal totalSalario = receitaRepository.totalReceitasPorCategoria("Salário", id);
		BigDecimal totalRendimentos = receitaRepository.totalReceitasPorCategoria("Rendimentos", id);
		BigDecimal totalPresente = receitaRepository.totalReceitasPorCategoria("Presente", id);
		BigDecimal totalOutros = receitaRepository.totalReceitasPorCategoria("Outros", id);
		
		BigDecimal totalReceitas = receitaRepository.totalReceitas(id);
		
		List<BigDecimal> porcentagemCategoria = new ArrayList<BigDecimal>();
		
		if(totalSalario != null) {
			porcentagemCategoria.add( totalSalario.multiply(new BigDecimal(100)).divide(totalReceitas, 2, RoundingMode.HALF_UP) );
		} else {
			porcentagemCategoria.add(new BigDecimal(0));
		}
		
		if(totalRendimentos != null) {
			porcentagemCategoria.add( totalRendimentos.multiply(new BigDecimal(100)).divide(totalReceitas, 2, RoundingMode.HALF_UP) );
		} else {
			porcentagemCategoria.add(new BigDecimal(0));
		}
		
		if(totalPresente != null) {
			porcentagemCategoria.add( totalPresente.multiply(new BigDecimal(100)).divide(totalReceitas, 2, RoundingMode.HALF_UP) );
		} else {
			porcentagemCategoria.add(new BigDecimal(0));
		}
		
		if(totalOutros != null) {
			porcentagemCategoria.add( totalOutros.multiply(new BigDecimal(100)).divide(totalReceitas, 2, RoundingMode.HALF_UP) );
		} else {
			porcentagemCategoria.add(new BigDecimal(0));
		}
		
		return porcentagemCategoria;
	}
	
	public List<BigDecimal> listarPercentuaisDespesasGraficoRosquinha() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email;    
		if (principal instanceof UserDetails) {
			email = ((UserDetails)principal).getUsername();
		} else {
			email = principal.toString();
		}
		Long id = retornaIdUsuarioLogado(email);
		
		BigDecimal totalAlimentacao = despesaRepository.totalDespesasPorCategoria("Alimentação", id);
		BigDecimal totalTransporte = despesaRepository.totalDespesasPorCategoria("Transporte", id);
		BigDecimal totalRoupa = despesaRepository.totalDespesasPorCategoria("Roupa", id);
		BigDecimal totalPagamentos = despesaRepository.totalDespesasPorCategoria("Pagamentos", id);
		BigDecimal totalInvestimentos = despesaRepository.totalDespesasPorCategoria("Investimentos", id);
		
		BigDecimal totalReceitas = despesaRepository.totalDespesas(id);
		
		List<BigDecimal> porcentagemCategoria = new ArrayList<BigDecimal>();
		
		if(totalAlimentacao != null) {
			porcentagemCategoria.add( totalAlimentacao.multiply(new BigDecimal(100)).divide(totalReceitas, 2, RoundingMode.HALF_UP) );
		} else {
			porcentagemCategoria.add(new BigDecimal(0));
		}
		
		if(totalTransporte != null) {
			porcentagemCategoria.add( totalTransporte.multiply(new BigDecimal(100)).divide(totalReceitas, 2, RoundingMode.HALF_UP) );
		} else {
			porcentagemCategoria.add(new BigDecimal(0));
		}
		
		if(totalRoupa != null) {
			porcentagemCategoria.add( totalRoupa.multiply(new BigDecimal(100)).divide(totalReceitas, 2, RoundingMode.HALF_UP) );
		} else {
			porcentagemCategoria.add(new BigDecimal(0));
		}
		
		if(totalPagamentos != null) {
			porcentagemCategoria.add( totalPagamentos.multiply(new BigDecimal(100)).divide(totalReceitas, 2, RoundingMode.HALF_UP) );
		} else {
			porcentagemCategoria.add(new BigDecimal(0));
		}
		
		if(totalInvestimentos != null) {
			porcentagemCategoria.add( totalInvestimentos.multiply(new BigDecimal(100)).divide(totalReceitas, 2, RoundingMode.HALF_UP) );
		} else {
			porcentagemCategoria.add(new BigDecimal(0));
		}
		
		return porcentagemCategoria;
	}
	/*	FIM	-	RECUPERAÇÃO DE DADOS DO GRÁFICO DE DOUGHNUT	*/
	
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
		List<Receita> arrayReceitas = new ArrayList<Receita>();
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email;    
		if (principal instanceof UserDetails) {
			email = ((UserDetails)principal).getUsername();
		} else {
			email = principal.toString();
		}
		Long id = retornaIdUsuarioLogado(email);
		
		List<Receita> receitas = receitaRepository.listarReceitasUsuario(id);
		
		for (Receita receita : receitas) {
			arrayReceitas.add(receita);
		}
		
		return arrayReceitas;
	}
	
	public List<Despesa> listarDespesas() {
		List<Despesa> arrayDespesas = new ArrayList<Despesa>();
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email;    
		if (principal instanceof UserDetails) {
			email = ((UserDetails)principal).getUsername();
		} else {
			email = principal.toString();
		}
		Long id = retornaIdUsuarioLogado(email);
		
		List<Despesa> despesas = despesaRepository.listarDespesasUsuario(id);
		
		for (Despesa despesa : despesas) {
			arrayDespesas.add(despesa);
		}
		
		return arrayDespesas;
	}
	/*	FIM	-	RECUPERAÇÃO DE DADOS DA TELA DE LANÇAMENTOS	*/

}
