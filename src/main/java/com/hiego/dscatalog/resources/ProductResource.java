package com.hiego.dscatalog.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hiego.dscatalog.dto.ProductDTO;
import com.hiego.dscatalog.services.ProductService;

@RestController
//@RequestMapping(value = "/categories")
public class ProductResource {
	
	@Autowired
	private ProductService productService;
	
	//chamada via postman 
	@GetMapping("/buscar-products")
	public ResponseEntity<List<ProductDTO>> getAll(){		
		List<ProductDTO> list = productService.Buscar();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/buscar-products/{id}")
	public ResponseEntity<ProductDTO> getFindById(@PathVariable long id){		
		ProductDTO dto = productService.BuscarPorId(id);
		return ResponseEntity.ok().body(dto);
	}
	
	
	@PostMapping("/buscar-products")
	public ResponseEntity<ProductDTO> criarCategoria(@Valid @RequestBody ProductDTO dto){//@Valid pega as validaçoes do DTO
		dto = productService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping("/buscar-products/{id}")
	public ResponseEntity<ProductDTO> criarCategoria(@Valid @PathVariable long id, @RequestBody ProductDTO dto){
		dto = productService.updateProduct(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping("/buscar-products/{id}")
	public ResponseEntity<Void> deletar (@PathVariable long id){
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	//=======Paginacao==============================
	
	@GetMapping("/buscar-paginada/product")
	public ResponseEntity<Page<ProductDTO>> BuscarPaginada(Pageable pageable){
		 Page<ProductDTO> list = productService.BuscarProdutoPorPagina(pageable);		 
		return ResponseEntity.ok().body(list);
		 		
	}
		
	/////////////////////
	
}
