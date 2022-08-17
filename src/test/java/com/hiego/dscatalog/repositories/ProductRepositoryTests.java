package com.hiego.dscatalog.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.hiego.dscatalog.entities.Product;
import com.hiego.dscatalog.tests.TestsUtils;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository productRepository;
	
	private long existeId = 80L;
	private long naoExisteId = 80L;

	
	@BeforeEach
	void setup() throws Exception{
		 existeId = 1L;
		 naoExisteId = 80L;
	}
	
	@Test
	public void deleteObjetoQuandoIdExistir() {
		productRepository.deleteById(existeId);
		
		Optional<Product> result = productRepository.findById(existeId);
		assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteObjetoQuandoIdNaoExistirLancaException() {
			
		Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
			productRepository.deleteById(naoExisteId);
		});

	}
	
	
	@Test
	public void salvarQuandoNaoExistirId() {
			
		Product product = TestsUtils.creatProduct();
		product.setId(0);
		product = productRepository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(26L, product.getId());
		
	}
	

}
