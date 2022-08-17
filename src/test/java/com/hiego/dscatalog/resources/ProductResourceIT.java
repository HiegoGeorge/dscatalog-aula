package com.hiego.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiego.dscatalog.dto.ProductDTO;
import com.hiego.dscatalog.repositories.ProductRepository;
import com.hiego.dscatalog.tests.TestsUtils;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {
	
	@Autowired
	private MockMvc mockMvc;
	

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ObjectMapper mapper;
	
	private long idExist;
	private long idNotExist;

	
	@BeforeEach
	void setUp() throws Exception{
		idExist = 1L;
		idNotExist = 1000L;

	}
	
	@Test
	public void getAllPagesDeveriaRetornarPaginaOrdenadaPorNome() throws Exception {
		
		int dataBaseSizeBeforeDelete = productRepository.findAll().size();
		
		mockMvc.perform(get("/buscar-paginada/product?page=0&size=12&sort=name,asc")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.totalElements").value(dataBaseSizeBeforeDelete))																				
		.andExpect(jsonPath("$.content").exists())
		.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"))	
		.andExpect(jsonPath("$.content[1].name").value("PC Gamer"))
		.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));

	}
	
	@Test
	public void updateProdutoQuandoIdExistir() throws Exception {
		
		ProductDTO dto = TestsUtils.creatProductDTO();
		String jsonBody = mapper.writeValueAsString(dto);
		
		String expectedName = dto.getName();
		String expectedDescription = dto.getDescription();
		
		mockMvc.perform(put("/buscar-products/{id}", idExist)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(idExist))
				.andExpect(jsonPath("$.name").value(expectedName))
				.andExpect(jsonPath("$.description").value(expectedDescription));

	}
	
	@Test
	public void updateProdutoQuandoIdNaoExistir() throws Exception {
		
		ProductDTO dto = TestsUtils.creatProductDTO();
		String jsonBody = mapper.writeValueAsString(dto);
		
		mockMvc.perform(put("/buscar-products/{id}", idNotExist)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

}
