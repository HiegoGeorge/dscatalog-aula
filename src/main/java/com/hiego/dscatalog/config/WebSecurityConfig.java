package com.hiego.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { //Configuração provisória para liberar todos endpoints

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsService detailsService;
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**"); // biblioteca spring cloud oauth usa para passar as requisiçoes
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detailsService).passwordEncoder(passwordEncoder);//SpringSecurity na hora de fazer autentificação ele vai saber como vai buscar usuario por email e analisar senha criptografada
		super.configure(auth);//configurar o algoritimo usado para criptografar senha e quem é userDetailsService
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {		
		return super.authenticationManager();
	}
	
	//@Bean para que o componente fique disponivel no sistema
	//AuthenticationManager | configure faz parte do WebSecurityConfigurerAdapter botao direito source ouverrid/Implements
	
}
