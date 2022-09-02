package com.hiego.dscatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.hiego.dscatalog.components.JjwtTokenEnhacer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	

	@Value("${security.oauth2.client.client-id}") 
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}") 
	private String clientSecret;
	
	@Value("${jwt.duration}") 
	private Integer jwtDuration;
	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;
	
	@Autowired
	private JwtTokenStore tokenSotore;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JjwtTokenEnhacer tokenEnhacer;
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()			 // processo vai ser feito em memoria	
//definir o clientId esperando dizer o idDaAplicação/ //la na app web na hora de acessar o back End vai informar dscatalog o que foi definido aqui
		.withClient(clientId)     
		.secret(passwordEncoder.encode(clientSecret))	//senha	 // la no postman basicAuthorization vc passa usuario e senha
		.scopes("read", "write") // acesso dado pode ser apenas leitura escrita etc// leitura e escrita
		.authorizedGrantTypes("password")
		.accessTokenValiditySeconds(jwtDuration) ; //tempo inspiraçao do token em segundos
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {//quem vai autorizar e qual vai
		//ser o formato do token
		TokenEnhancerChain chain = new TokenEnhancerChain();
		chain.setTokenEnhancers(Arrays.asList(accessTokenConverter,tokenEnhacer));
		endpoints.authenticationManager(authenticationManager) // processar autenticaçao objeto authenticationManager
		.tokenStore(tokenSotore)//quais objetos por processar token
		.accessTokenConverter(accessTokenConverter)
		.tokenEnhancer(chain);

	}
	
	

}
