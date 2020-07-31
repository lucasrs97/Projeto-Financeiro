package com.apirest.Financeiro.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementsUserDetailsService userDetailsService;
	
	//	CONFIGURAÇÃO PARA INFORMAR:
	//	1 - QUAL A FORMA DE AUTENTICAÇÃO
	//	2 - QUAIS PÁGINAS PRECISAM E NÃO PRECISAM DE AUTENTICAÇÃO
	//	3 -	QUAL A POLÍTICA DE CONFIGURAÇÃO DO CORS
	//	4 - DESABILITAÇÃO DO CSRF (FALHA DE SEGURANÇA)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.httpBasic();
    	
    	http.authorizeRequests().antMatchers("/login").permitAll();
    	http.authorizeRequests().antMatchers("/usuario").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        
    	http.cors().configurationSource(corsConfigurationSource());
    	http.csrf().disable();
    }
    
    //	CONFIGURAÇÃO PARA INFORMAR AO SPRING SECURITY QUAIS USUÁRIOS PODEM SER PASSADOS NO CORPO DA REQUISIÇÃO
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//    	auth.inMemoryAuthentication()
//    		.withUser("lucasramalho").password("{noop}123").roles("USER")
//    		.and()
//    		.withUser("teste22").password("{noop}123").roles("USER","ADMIN");
//    }
    
//    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth
    		.userDetailsService(userDetailsService)
    		.passwordEncoder(new BCryptPasswordEncoder());
    }
    
    
    //	CONFIGURAÇÃO PARA ACEITAR SOLICITAÇÕES DE ORIGENS CRUZADAS. (EX: HTTP://LOCALHOST:8081)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","DELETE"));
        configuration.setAllowCredentials(true);
        
        // As três linhas abaixo adicionarão os cabeçalhos de resposta relevantes do CORS
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

}
