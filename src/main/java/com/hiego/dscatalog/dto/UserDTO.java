package com.hiego.dscatalog.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.hiego.dscatalog.entities.User;

public class UserDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotBlank(message = "Campo Obrigatorio")
	private String firstName;
	private String lastName;
	
	@Email(message = "Favor colocar um e-mail valido")
	private String email;

	
	private Set<RoleDTO>roles = new HashSet<>();
	

	public UserDTO() {
	}
	
	
	public UserDTO(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
//-------------------converter Entidade para DTO---------------------------//
	public UserDTO(User entidade) {
		id = entidade.getId();
		firstName = entidade.getFirstName();
		lastName = entidade.getLastName();
		email = entidade.getEmail();
		entidade.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
/*Pegando a lista de Roles que ja veio junto com usuario(FetchType.EAGER) 
* percorre para cada um instancia um role dto é inserir na lista de roles (private Set<RoleDTO>roles = new HashSet<>())*/
	}
//----------------------------------------------//
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	


	public Set<RoleDTO> getRoles() {
		return roles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		return Objects.equals(id, other.id);
	}
	
	





}
