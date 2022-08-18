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

import com.hiego.dscatalog.dto.UserDTO;
import com.hiego.dscatalog.dto.UserInsertDTO;
import com.hiego.dscatalog.services.UserService;

@RestController
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	//chamada via postman 
	@GetMapping("/user")
	public ResponseEntity<List<UserDTO>> getAll(){		
		List<UserDTO> list = userService.Buscar();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<UserDTO> getFindById(@PathVariable long id){		
		UserDTO dto = userService.BuscarPorId(id);
		return ResponseEntity.ok().body(dto);
	}
	
	
	@PostMapping("/user")
	public ResponseEntity<UserDTO> criarCategoria(@Valid @RequestBody UserInsertDTO dto){
		UserDTO newDTO = userService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(newDTO);
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<UserDTO> atualizarCategoria(@PathVariable long id, @RequestBody UserDTO dto){
		dto = userService.updateUser(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping("/user/{id}")
	public ResponseEntity<Void> deletar (@PathVariable long id){
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	//=======Paginacao==============================
	
	@GetMapping("/user-por-page")
	public ResponseEntity<Page<UserDTO>> BuscarPaginada(Pageable pageable){
		 Page<UserDTO> list = userService.BuscarPaginada(pageable);		 
		return ResponseEntity.ok().body(list);
		 		
	}
		
	/////////////////////
	
}
