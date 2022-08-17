package com.hiego.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiego.dscatalog.dto.ProductDTO;
import com.hiego.dscatalog.services.ProductService;
import com.hiego.dscatalog.services.exceptions.DataBaseException;
import com.hiego.dscatalog.services.exceptions.ResourceNotFoundException;
import com.hiego.dscatalog.tests.TestsUtils;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper mapper;

	private long idExist;
	private long idNotExist;
	private long idComChaveEstrangeira;
	private ProductDTO dto;
	private PageImpl<ProductDTO> page;


	@BeforeEach
	void setUp() throws Exception {

		idExist = 1L;
		idNotExist = 1000L;
		idComChaveEstrangeira = 4L;

		dto = TestsUtils.creatProductDTO();
		page = new PageImpl<>(List.of(dto));

		when(productService.BuscarProdutoPorPagina(ArgumentMatchers.any())).thenReturn(page);// simulando comportamento do buscarProdutoPpagina
		when(productService.BuscarPorId(idExist)).thenReturn(dto);
		when(productService.BuscarPorId(idNotExist)).thenThrow(ResourceNotFoundException.class);

		when(productService.updateProduct(eq(idExist), ArgumentMatchers.any())).thenReturn(dto);
		when(productService.updateProduct(eq(idNotExist), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(productService).delete(idExist);
		doThrow(ResourceNotFoundException.class).when(productService).delete(idNotExist);
		doThrow(DataBaseException.class).when(productService).delete(idComChaveEstrangeira);
		
		when(productService.insert(any())).thenReturn(dto);
	}

	@Test
	public void getAllPagesDeveriaRetornarPagina() throws Exception {
		mockMvc.perform(get("/buscar-paginada/product")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void getIdQuandoExisteRetornaProduto() throws Exception {
		mockMvc.perform(get("/buscar-products/{id}", idExist)
				.accept(MediaType.APPLICATION_JSON))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())// $-> Acessa objeto da resposta como se faz acesso do json da resposta inteiro																				
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.description").exists())
				.andExpect(jsonPath("$.price").exists())
				.andExpect(jsonPath("$.img_url").exists())
				.andExpect(jsonPath("$.date").exists());
	}

	@Test
	public void getAllIdNaoExisteRetornaNotFound() throws Exception {
		mockMvc.perform(get("/buscar-products/{id}", idNotExist)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateProdutoQuandoIdExistir() throws Exception {

		String jsonBody = mapper.writeValueAsString(dto); // converte ObjetoDTO em uma string

		mockMvc.perform(put("/buscar-products/{id}", idExist)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.description").exists())
				.andExpect(jsonPath("$.price").exists())
				.andExpect(jsonPath("$.img_url").exists())
				.andExpect(jsonPath("$.date").exists());
	}
	
	
	@Test
	public void deleteProdutoQuandoIdExistir() throws Exception {
				
		mockMvc.perform(delete("/buscar-products/{id}", idExist)
				.accept(MediaType.APPLICATION_JSON))
		
				.andExpect(status().isNoContent());

	}

	@Test
	public void deleteProdutoQuandoIdNaoExistirRetornaNotFound() throws Exception {

		mockMvc.perform(delete("/buscar-products/{id}",idNotExist)
				.accept(MediaType.APPLICATION_JSON))
		
				.andExpect(status().isNotFound());

	}
	
	@Test
	public void insertProdutoQuandoIdExistir() throws Exception {

		String jsonBody = mapper.writeValueAsString(dto); // converte ObjetoDTO em uma string

		mockMvc.perform(post("/buscar-products")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.description").exists())
				.andExpect(jsonPath("$.price").exists())
				.andExpect(jsonPath("$.img_url").exists())
				.andExpect(jsonPath("$.date").exists());
	}
	
	
	
	
	
	
	
}
