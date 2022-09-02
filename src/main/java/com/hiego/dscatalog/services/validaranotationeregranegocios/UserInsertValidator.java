package com.hiego.dscatalog.services.validaranotationeregranegocios;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.hiego.dscatalog.dto.UserInsertDTO;
import com.hiego.dscatalog.entities.User;
import com.hiego.dscatalog.repositories.UserRepository;
import com.hiego.dscatalog.resources.exceptions.FieldMessage;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	//UserInsertDTO tipo da classe que vai receber essa anotação/ na classe eu coloco essa anotaçao que eu criei
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		User user = userRepository.findByEmail(dto.getEmail());
		
		if(user != null) {
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
