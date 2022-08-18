package com.hiego.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hiego.dscatalog.services.exceptions.DataBaseException;
import com.hiego.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	//recebe dois parametros uma exceção e um http request
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e , HttpServletRequest request){
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.NOT_FOUND.value());
		err.setError("Erro!! ID não encontrado ");
		err.setMessage(e.getMessage()); //pega a mensagem que esta definida no service
		err.setPath(request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	
	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardError> dataBase(DataBaseException e , HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Data Base Exception");
		err.setMessage(e.getMessage()); //pega a mensagem que esta definida no service
		err.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(err);
	}
	
	
	//trataExceção dos beasn validator @valid @Email @NotBlank @Empty
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e , HttpServletRequest request){
//		HttpStatus status = HttpStatus.BAD_REQUEST;
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		
		ValidationError err = new ValidationError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Validation Exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());		
		for(FieldError f: e.getBindingResult().getFieldErrors()) {//para cada fildErro na lista faz tratamento
			//e.getBindingResult().getFieldErrors() pega os erros especificos da validação.
			err.addError(f.getField(), f.getDefaultMessage());
		}
		
		return ResponseEntity.status(status).body(err);
	}
	
}
