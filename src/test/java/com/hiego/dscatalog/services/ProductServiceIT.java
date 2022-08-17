package com.hiego.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.hiego.dscatalog.dto.ProductDTO;
import com.hiego.dscatalog.repositories.ProductRepository;
import com.hiego.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {//teste de integraça, faz acesso direto ao banco H2
	
	
	@Autowired
	private ProductService productService;	

	@Autowired
	private ProductRepository productRepository;
	
	private long idExist;
	private long idNotExist;

	
	@BeforeEach
	void setUp() throws Exception{
		idExist = 1L;
		idNotExist = 1000L;

	}
	
	@Test
	public void deleteDeveDeletarQuandoIdExistir() {
		int dataBaseSizeBeforeDelete = productRepository.findAll().size();
		
		productService.delete(idExist);
		
		Assertions.assertEquals(dataBaseSizeBeforeDelete -1, productRepository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		assertThrows(ResourceNotFoundException.class, () -> {
			productService.delete(idNotExist);
		});

	}
	
	@Test
	public void buscarPorPaginaQuandoRetornarUmaPagina0Size10() {

		int dataBaseSizeBeforeDelete = productRepository.findAll().size();
		
		PageRequest page = PageRequest.of(0, 10);
		Page<ProductDTO> result = productService.BuscarProdutoPorPagina(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(dataBaseSizeBeforeDelete, result.getTotalElements());
	}
	
	@Test
	public void buscarPorPaginaQuandoRetornarUmaPaginaVaziaQuandoNaoExisgtir() {
		
		PageRequest page = PageRequest.of(50, 10);
		Page<ProductDTO> result = productService.BuscarProdutoPorPagina(page);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void buscarPorPaginaRetornarPaginaOrdenadaPorNome() {
		
		PageRequest page = PageRequest.of(0, 10, Sort.by("name"));
		Page<ProductDTO> result = productService.BuscarProdutoPorPagina(page);
		
		Assertions.assertFalse(result.isEmpty());	
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}
	

}
