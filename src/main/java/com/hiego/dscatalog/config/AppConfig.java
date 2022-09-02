package com.hiego.dscatalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class AppConfig {
	
	@Value("${jwt.secret}") //externalizado o valor da variavel dentro do applicationProporties
	private String jwtSecret;
	

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {//@Bean em cima do metodo a instancia vai ser um componente gerenciado pelo springBoot
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter(); //instancia objeto
		tokenConverter.setSigningKey("jwtSecret"); // registra a assinatura/ registra chave do token
		return tokenConverter; // retorna
	}

	@Bean
	public JwtTokenStore tokenStore() { 
		return new JwtTokenStore(accessTokenConverter()); // instancia e passa como argumento accessTokenConverter
	}
}
