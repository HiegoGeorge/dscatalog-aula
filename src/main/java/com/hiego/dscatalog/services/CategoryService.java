package com.hiego.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiego.dscatalog.dto.CategoryDTO;
import com.hiego.dscatalog.entities.Category;
import com.hiego.dscatalog.repositories.CategoryRepository;
import com.hiego.dscatalog.services.exceptions.DataBaseException;
import com.hiego.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;	
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> Buscar(){
		
		 List<Category> list = categoryRepository.findAll();
		 
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		 		
	}//map= transforma cada elemento original em outra coisa/ faz aplicando uma funçao a cada elemento da lista
	// collect(Collectors.toList()); transforma uma stream para lista
	
	
	@Transactional(readOnly = true)
	public CategoryDTO BuscarPorId(long id){		
		Optional<Category> obj = categoryRepository.findById(id);
		Category entidade = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CategoryDTO(entidade);
	}
	
	//orElseThrow) posso retornar uma exceção caso nao tenha algo na entidade
	//Criar exceção personalizado para nao ficar estourando erro 500
	
	@Transactional(readOnly = true)
	public CategoryDTO insert (CategoryDTO dto) {
		Category categoryEntidade = new Category();
		
		categoryEntidade.setName(dto.getName());
		categoryEntidade = categoryRepository.save(categoryEntidade);
		
		return new CategoryDTO(categoryEntidade);
	}

	@Transactional(readOnly = true)
	public CategoryDTO update(long id, CategoryDTO dto) {
		
		try {
		Category categoryEntidade = categoryRepository.getReferenceById(id);
		
		// getReferenceById-> ele instania objeto provisorio com os dados e id no objeto, nao faz consulta direto no banco
		// so vai no banco para salvar e nao para buscar
		//findById ele vai no banco para verificar
		
		categoryEntidade.setName(dto.getName());
		categoryEntidade = categoryRepository.save(categoryEntidade);
			
		return new CategoryDTO(categoryEntidade);
		
		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id not found" + id + " verificar id valido");
		}
	
	}


	public void delete(long id) {
		try {
			
			categoryRepository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id not found" + id + " verificar id valido");
		}catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
		
	}


//public Category findById(String id) {
//	Optional<Category> optional = categoryRepository.findById(id);
//	return optional.orElseThrow( ()-> new ObjectNotFoundException("Objeto não encontrado! id: " + id + ", Tipo: " + Category.class.getName(), id));
//}
}


//converter DTO para entidade
//pecorre lista para cada elemento da lista passo na construtor do dto adicionando na lista de dto
//List<CategoryDTO> listDto = new ArrayList<>();
//for(Category cat : list) {
//	listDto.add(new CategoryDTO(cat));
//}