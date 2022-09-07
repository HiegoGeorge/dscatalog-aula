package com.hiego.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	//botao direito source/override/Implements
	
	@Autowired
	private JwtTokenStore jwtTokenStore;
	
	////==== Vetor de String para rotas serem liberadas publicamente ===///////
				// liberar apenas as rotas GET 
	private static final String[] PUBLIC = {"/oauth/token"};
	private static final String[] OPERATOR_OR_ADMIN = {"/products/**", "/categories/**"};
	private static final String[] ADMIN = {"/users/**"};
	
	
	////==== ================================================================ ===///////
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			//aqui configura o tokenStore
		resources.tokenStore(jwtTokenStore);// resoruce serve vai ser capaz de analizar o teken e verificar se é valido(tempo usuriao etc)
	}


	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll()
		.antMatchers(HttpMethod.GET,OPERATOR_OR_ADMIN).permitAll()
		.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")
		.antMatchers(ADMIN).hasAnyRole("ADMIN")
		.anyRequest().authenticated();
	}
	
	
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		// CONFIGURAR QUEM PODE ACESSAR O QUE 
//		http.authorizeRequests()
//		.antMatchers(PUBLIC).permitAll()// quem estiver acessando essa rota vai esta liberado nao vai pedir login
//		.antMatchers(HttpMethod.GET,OPERATOR_OR_ADMIN).permitAll() //liberando os metodos gets apenas para quem for operador ou adm
//		.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")//Possui alguns dos papeis, operador ou adm/Essas rotas podem ser acessadas quem tem uma dessas roles
//		.antMatchers(ADMIN).hasAnyRole("ADMIN")//autorizaçao para admin			
//		.anyRequest().authenticated();// exigir que a pessoa for acessar qualquer outra rota tenha que esta logado	
//		
//	}

		

}
