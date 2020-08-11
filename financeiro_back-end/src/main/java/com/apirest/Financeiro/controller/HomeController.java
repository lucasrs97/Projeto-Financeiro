package com.apirest.Financeiro.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.Financeiro.model.Patrimonio;
import com.apirest.Financeiro.model.Usuario;
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
	
	
	@GetMapping("/home")
	public List<Object> listar() {
		
		List<Object> dadosHome = new ArrayList<Object>();
		
		List<BigDecimal> dadosCard = recuperaDadosCard();
		
		List<String> mesesGraficos = listarMesesGraficos();
		
		List<BigDecimal> valoresGraficoEvolucaoPatrimonial = listarValoresEvolucaoPatrimonial();
		
		List<BigDecimal> porcetagemReceitasGraficoDoughnut = listarPercentuaisReceitas();
		List<BigDecimal> porcetagemDespesasGraficoDoughnut = listarPercentuaisDespesas();
		
		List<BigDecimal> valoresGraficoBalancoReceita = listarBalancoReceitas();
		List<BigDecimal> valoresGraficoBalancoDespesa = listarBalancoDespesas();
		List<BigDecimal> valoresGraficoBalancoPatrimonio = listarBalancoPatrimonio();
		
		dadosHome.add(dadosCard);
		
		dadosHome.add(mesesGraficos);
		
		dadosHome.add(valoresGraficoEvolucaoPatrimonial);
		
		dadosHome.add(porcetagemReceitasGraficoDoughnut);
		dadosHome.add(porcetagemDespesasGraficoDoughnut);
		
		dadosHome.add(valoresGraficoBalancoReceita);
		dadosHome.add(valoresGraficoBalancoDespesa);
		dadosHome.add(valoresGraficoBalancoPatrimonio);
		
		return dadosHome;
	}
	
	/*	INÍCIO	-	RECUPERAÇÃO DE DADOS DOS CARDS	*/
	public List<BigDecimal> recuperaDadosCard() {
		
		Long id = retornaIdUsuarioLogado();
		
		//	RECEITAS
		BigDecimal totalReceitas = receitaRepository.totalReceitas(id);
		//	RECEITAS
		
		//	DESPESAS
		BigDecimal totalDespesas = despesaRepository.totalDespesas(id);
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
	
	/*	INÍCIO	-	RECUPERAÇÃO DOS MESES DE CADASTRO PARA OS GRÁFICOS	*/
	public List<String> listarMesesGraficos() {
		
		Long id = retornaIdUsuarioLogado();
		
		Optional<Usuario> optUsuario = usuarioRepository.findById(id);
		if(optUsuario.isPresent()) {
			Usuario usuario = optUsuario.get();
			
			LocalDate mesAnoCadastro = usuario.getDataCadastro();
			
			int mes = mesAnoCadastro.getMonth().getValue();
			int ano = mesAnoCadastro.getYear();
			
			List<String> arrayMesAno = new ArrayList<String>();
			
			while(mes <= LocalDate.now().getMonth().getValue() && ano <= LocalDate.now().getYear()) {
				
				if(mes == 10 || mes == 11 || mes == 12) {
					arrayMesAno.add(mes + "/" + ano);
				} else {
					arrayMesAno.add("0" + mes + "/" + ano);
				}
				
				mes = mes + 1;
			}	
			return arrayMesAno;
		
		} else {
			
			return null;
		}
	}
	/*	FIM		-	RECUPERAÇÃO DOS MESES DE CADASTRO PARA OS GRÁFICOS	*/
	
	/*	INÍCIO	-	RECUPERAÇÃO DE DADOS DO GRÁFICO DE EVOLUÇÃO PATRIMONIAL	*/
	public List<BigDecimal> listarValoresEvolucaoPatrimonial() {
		
		Long id = retornaIdUsuarioLogado();
		
		List<String> arrayMesAno = listarMesesGraficos();
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
				patrimonioAnterior = (patrimonioAnterior.add(patrimonioMesAtual));
			}
		}
		
		return arrayPatrimonio;
	}
	
	public BigDecimal listarHistoricoMensalPatrimonio(Long identificadorUsuario, String mes, String ano) {
		return patrimonioRepository.buscarPatrimonioPorMesAno(identificadorUsuario, mes, ano);
	}
	/*	FIM		-	RECUPERAÇÃO DE DADOS DO GRÁFICO DE EVOLUÇÃO PATRIMONIAL	*/
	
	/*	INÍCIO	-	RECUPERAÇÃO DE DADOS DO GRÁFICO DE PERCENTUAIS	*/
	public List<BigDecimal> listarPercentuaisReceitas() {
		
		Long id = retornaIdUsuarioLogado();
		
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
	
	public List<BigDecimal> listarPercentuaisDespesas() {
		
		Long id = retornaIdUsuarioLogado();
		
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
	/*	FIM	-		RECUPERAÇÃO DE DADOS DO GRÁFICO DE PERCENTUAIS	*/
	
	/*	INÍCIO	-	RECUPERAÇÃO DE DADOS DO GRÁFICO DE BALANÇO RECEITA X DESPESA X PATRIMÔNIO	*/
	public List<BigDecimal> listarBalancoReceitas() {
		Long id = retornaIdUsuarioLogado();
		
		List<BigDecimal> arrayReceitasMesAno = new ArrayList<BigDecimal>();
		
		List<String> arrayMesAno = listarMesesGraficos();
		
		BigDecimal totalReceitasMesAno = new BigDecimal(0);
		
		for (String mesAno : arrayMesAno) {
			String mes = mesAno.substring(0, 2);
			String ano = mesAno.substring(3, 7);
			
			totalReceitasMesAno = listarReceitasPorMesAno(id, mes, ano);
			
			if(totalReceitasMesAno != null) {
				arrayReceitasMesAno.add(totalReceitasMesAno);
			} else {
				arrayReceitasMesAno.add(new BigDecimal(0));
			}
			
			
		}
		
		return arrayReceitasMesAno;
	}
	
	public BigDecimal listarReceitasPorMesAno(Long identificadorUsuario, String mes, String ano) {
		return receitaRepository.totalReceitasPorMesAno(identificadorUsuario, mes, ano);
	}
	
	public List<BigDecimal> listarBalancoDespesas() {
		Long id = retornaIdUsuarioLogado();
		
		List<BigDecimal> arrayDespesasMesAno = new ArrayList<BigDecimal>();
		
		List<String> arrayMesAno = listarMesesGraficos();
		
		BigDecimal totalDespsasMesAno = new BigDecimal(0);
		
		for (String mesAno : arrayMesAno) {
			String mes = mesAno.substring(0, 2);
			String ano = mesAno.substring(3, 7);
			
			totalDespsasMesAno = listarDespesasPorMesAno(id, mes, ano);
			
			if(totalDespsasMesAno != null) {
				arrayDespesasMesAno.add(totalDespsasMesAno);
			} else {
				arrayDespesasMesAno.add(new BigDecimal(0));
			}
		}
		
		return arrayDespesasMesAno;
	}
	
	public BigDecimal listarDespesasPorMesAno(Long identificadorUsuario, String mes, String ano) {
		return despesaRepository.totalDespesasPorMesAno(identificadorUsuario, mes, ano);
	}
	
	public List<BigDecimal> listarBalancoPatrimonio() {
		Long id = retornaIdUsuarioLogado();
		
		List<BigDecimal> arrayPatrimonioMesAno = new ArrayList<BigDecimal>();
		
		List<String> arrayMesAno = listarMesesGraficos();
		
		BigDecimal totalPatrimonioMesAno = new BigDecimal(0);
		
		for (String mesAno : arrayMesAno) {
			String mes = mesAno.substring(0, 2);
			String ano = mesAno.substring(3, 7);
			
			totalPatrimonioMesAno = listarHistoricoMensalPatrimonio(id, mes, ano);
			
			if(totalPatrimonioMesAno != null) {
				arrayPatrimonioMesAno.add(totalPatrimonioMesAno);
			} else {
				arrayPatrimonioMesAno.add(new BigDecimal(0));
			}
		}
		
		return arrayPatrimonioMesAno;
	}
	/*	FIM	-		RECUPERAÇÃO DE DADOS DO GRÁFICO DE BALANÇO RECEITA X DESPESA X PATRIMÔNIO	*/

}
