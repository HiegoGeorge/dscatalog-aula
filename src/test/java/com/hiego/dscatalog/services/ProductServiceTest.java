package com.hiego.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hiego.dscatalog.dto.ProductDTO;
import com.hiego.dscatalog.entities.Product;
import com.hiego.dscatalog.repositories.ProductRepository;
import com.hiego.dscatalog.services.exceptions.DataBaseException;
import com.hiego.dscatalog.services.exceptions.ResourceNotFoundException;
import com.hiego.dscatalog.tests.TestsUtils;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
//teste de service é um teste de unidade
	
	@InjectMocks
	private ProductService productService;
	
	// nao pode injetar tem que simular comportamento. Em um objeto mockado.
	
	@Mock
	private ProductRepository productRepository;
	
	private long idExist;
	private long idNotExist;
	private long idComChaveEstrangeira;
	private PageImpl<Product> page;
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception{
		idExist = 1L;
		idNotExist = 1000L;
		idComChaveEstrangeira = 4L;
		product = TestsUtils.creatProduct();
		page = new PageImpl<>(List.of(product));
		
		//Mockito.when(null)//usar quando metodo retorna algo// quando eu chamar o repository.find all
		when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);// ArgumentMatchers.any()qualquer valor
		when(productRepository.save(ArgumentMatchers.any())).thenReturn(page);
		
		when(productRepository.findById(idExist)).thenReturn(Optional.of(product));
		when(productRepository.findById(idNotExist)).thenReturn(Optional.empty());
		
		doNothing().when(productRepository).deleteById(idExist); // doNothing( para metodo que nao retorna void
		doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(idNotExist);//para retornar excessao 
		doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(idComChaveEstrangeira);
	}
	
	@Test
	public void deletDeveFazerNadaQuandIdExistir() {		
		assertDoesNotThrow(() -> {
			productService.delete(idExist);
		});
		
		verify(productRepository,Mockito.times(1)).deleteById(idExist);// Mockito.times(1) foi chamado apenas 1 vez
	}
	
	
	@Test
	public void deletDeveFazerNadaQuandIdNaoExistir() {		
		assertThrows(ResourceNotFoundException.class, () -> {
			productService.delete(idNotExist);
		});
		
		verify(productRepository,Mockito.times(1)).deleteById(idNotExist);// Mockito.times(1) foi chamado apenas 1 vez
	}
	
	@Test
	public void deletDeveFazerNadaQuandIdExistirTemDependencia() {		
		assertThrows(DataBaseException.class, () -> {
			productService.delete(idComChaveEstrangeira);
		});
		
		verify(productRepository,Mockito.times(1)).deleteById(idComChaveEstrangeira);// Mockito.times(1) foi chamado apenas 1 vez
	}
	
	
	
	@Test
	public void getAllPagesQuandoRetornarPages() {		

		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = productService.BuscarProdutoPorPagina(pageable);
		
		assertNotNull(result);
		verify(productRepository).findAll(pageable);
		
	}
}
