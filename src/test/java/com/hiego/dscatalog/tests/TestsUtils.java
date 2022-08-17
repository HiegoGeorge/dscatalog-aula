package com.hiego.dscatalog.tests;

import java.time.Instant;

import com.hiego.dscatalog.dto.ProductDTO;
import com.hiego.dscatalog.entities.Category;
import com.hiego.dscatalog.entities.Product;

public class TestsUtils {
	
	public static Product creatProduct() {// criando um produto de teste
		Product product = new Product(1L, "Phone", "Iphone",800.0, "https://raw.com/img/3-big.jpg", Instant.parse("2020-07-13T20:50:07.12345Z"));
									  
		product.getCategories().add(new Category(2L, "Eletronics"));
		return product;
	}
	
	public static ProductDTO creatProductDTO() {
		Product product = creatProduct();
		return new ProductDTO(product, product.getCategories());
	}

}
