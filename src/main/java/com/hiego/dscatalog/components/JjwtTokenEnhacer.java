package com.hiego.dscatalog.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.hiego.dscatalog.entities.User;
import com.hiego.dscatalog.repositories.UserRepository;

@Component
public class JjwtTokenEnhacer implements TokenEnhancer{
	
	@Autowired 
	private UserRepository userRepository;
	

	//recebe os dois parametros(objetos)/ implementado entra no cliclo de vida do token e acrescenta o que passar.
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
			
		User user = userRepository.findByEmail(authentication.getName());//busca e retorna por email
		//para acrescentar o objeto ao token precisa colocar em um map
		
		Map<String, Object> map = new HashMap<>();
		map.put("userFirstName", user.getFirstName());
		map.put("userId", user.getId());
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		
		token.setAdditionalInformation(map); // token esta com as informaçoes adicionais
		
		return accessToken;
	}
	

}
