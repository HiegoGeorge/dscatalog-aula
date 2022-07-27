package com.hiego.dscatalog.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hiego.dscatalog.dto.CategoryDTO;
import com.hiego.dscatalog.entities.Category;
import com.hiego.dscatalog.services.CategoryService;

@RestController
//@RequestMapping(value = "/categories")
public class CategoryResource {
	
	@Autowired
	private CategoryService categoryService;
	
	//chamada via postman 
	@GetMapping("/buscar-categorias")
	public ResponseEntity<List<CategoryDTO>> getAll(){		
		List<CategoryDTO> list = categoryService.Buscar();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/buscar-categorias/{id}")
	public ResponseEntity<CategoryDTO> getFindById(@PathVariable long id){		
		CategoryDTO dto = categoryService.BuscarPorId(id);
		return ResponseEntity.ok().body(dto);
	}
	
	
	@PostMapping("/buscar-categorias")
	public ResponseEntity<CategoryDTO> criarCategoria(@RequestBody CategoryDTO dto){
		dto = categoryService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping("/buscar-categorias/{id}")
	public ResponseEntity<CategoryDTO> criarCategoria(@PathVariable long id, @RequestBody CategoryDTO dto){
		dto = categoryService.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping("/buscar-categorias/{id}")
	public ResponseEntity<Void> deletar (@PathVariable long id){
		categoryService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
