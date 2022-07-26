package com.hiego.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.hiego.dscatalog.entities.Category;
import com.hiego.dscatalog.entities.Product;

public class ProductDTO implements Serializable{

private static final long serialVersionUID = 1L;

	private long id;
	
	@Size(min = 2, max = 60)
	@NotBlank(message = "Campo Obrigatorio")
	private String name;
	private String description;
	
	@Positive(message = "Preço deve ser um valor positivo")
	private Double price;	
	
	private String img_url;
	
	@PastOrPresent(message = "Data do Produto não pode ser futura")
	private Instant date; 
	
	public List<CategoryDTO> categories = new ArrayList<>();

	
	public ProductDTO() {
		
	}

	public ProductDTO(long id, String name, String description, Double price, String img_url, Instant date) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.img_url = img_url;
		this.date = date;
	}

	public ProductDTO(Product product ) {
		this.id = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.img_url = product.getImg_url();
		this.date = product.getDate();
	}
	
	public ProductDTO(Product product, Set<Category> setCategories) {// instacia dto com os elementos da lista
		this(product);
		setCategories.forEach(categorias -> this.categories.add(new CategoryDTO(categorias)));// para cada x da lista de categories add no dto
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}




	
	
}
