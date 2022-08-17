package com.hiego.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {//@Bean em cima do metodo a instancia vai ser um componente gerenciado pelo springBoot
		return new BCryptPasswordEncoder();
	}
}
