package com.hiego.dscatalog.services.validaranotationeregranegocios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.hiego.dscatalog.dto.UserUpdateDTO;
import com.hiego.dscatalog.entities.User;
import com.hiego.dscatalog.repositories.UserRepository;
import com.hiego.dscatalog.resources.exceptions.FieldMessage;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {
	//UserInsertDTO tipo da classe que vai receber essa anotação/ na classe eu coloco essa anotaçao que eu criei
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public void initialize(UserUpdateValid ann) {
	}

	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked") // para tirar os warnners
		var uriVars = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		Long userId = Long.parseLong( uriVars.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
		
		User user = userRepository.findByEmail(dto.getEmail());
		
		if(user != null && userId != user.getId()) { // id do usuario nao for id do que eu quero atualizar. tentando atualizar o mesmo email de um usuario ja existente
			list.add(new FieldMessage("email", "Email existente"));
		}
		
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFildName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
