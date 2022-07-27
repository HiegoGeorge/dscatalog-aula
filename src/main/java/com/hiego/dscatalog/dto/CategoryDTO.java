package com.hiego.dscatalog.dto;

import java.io.Serializable;
import java.util.Objects;

import com.hiego.dscatalog.entities.Category;

public class CategoryDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	
	
	public CategoryDTO() {

	}
	
	public CategoryDTO(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	//construtor que recebe entidade
	//povoando o DTO com os valores da entidade
	public CategoryDTO(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		CategoryDTO other = (CategoryDTO) obj;
		return id == other.id;
	}
	
	

}
