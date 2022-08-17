package com.hiego.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_product")
public class Product implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;
	
	private Double price;
	
	private String img_url;
	
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant date; 
	
	// Quando a um relacionamento muito para muitos é preciso criar um outra tabela com id das tabelas que estao relacionando
	// usando essa associação com anotaçao
	@ManyToMany
	@JoinTable(name = "tb_product_category", 
				joinColumns = @JoinColumn(name = "product_id"), 
				inverseJoinColumns = @JoinColumn(name = "category_id")) 
	Set<Category> categories = new HashSet<>();
	//@JoinTable(name = tb_product_category//tabela faz associação entre as duas entidade
	// @JoinColumn estabelece qual chave estrangeira relacionada a classe que esta referenciada

	public Product() {
	
	}

	public Product(long id, String name, String description, Double price, String img_url, Instant date) {
	
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.img_url = img_url;
		this.date = date;
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	

	public Set<Category> getCategories() {
		return categories;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return id == other.id;
	}
	
		
	
}
