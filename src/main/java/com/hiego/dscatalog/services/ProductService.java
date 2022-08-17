package com.hiego.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiego.dscatalog.dto.CategoryDTO;
import com.hiego.dscatalog.dto.ProductDTO;
import com.hiego.dscatalog.entities.Category;
import com.hiego.dscatalog.entities.Product;
import com.hiego.dscatalog.repositories.CategoryRepository;
import com.hiego.dscatalog.repositories.ProductRepository;
import com.hiego.dscatalog.services.exceptions.DataBaseException;
import com.hiego.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;	
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Transactional(readOnly = true)
	public List<ProductDTO> Buscar(){
		
		 List<Product> list = productRepository.findAll();
		 
		return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
		 		
	}
	
	
	@Transactional(readOnly = true)
	public ProductDTO BuscarPorId(long id){		
		Optional<Product> obj = productRepository.findById(id);
		Product entidade = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));	
		return new ProductDTO(entidade, entidade.getCategories()); // return new ProductDTO(entidade); retorna sem a categoria, categoria retorna vazia
	}//return new ProductDTO(entidade, entidade.getCategories()); desa maneira chama um construtor dentro do DTO que retorna a categoria junto

	
	@Transactional(readOnly = true)
	public ProductDTO insert (ProductDTO dto) {
		Product categoryEntidade = new Product();
		
		copyDtoToEntity(dto,categoryEntidade);

		categoryEntidade = productRepository.save(categoryEntidade);
		
		return new ProductDTO(categoryEntidade);
	}


	@Transactional(readOnly = true)
	public ProductDTO updateProduct(long id, ProductDTO dto) {
		
		try {
		Product categoryEntidade = productRepository.getReferenceById(id);

		copyDtoToEntity(dto,categoryEntidade);

		categoryEntidade = productRepository.saveAndFlush(categoryEntidade);
			
		return new ProductDTO(categoryEntidade);
		
		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id not found" + id + " verificar id valido");
		}
	
	}


	public void delete(long id) {
		try {
			
			productRepository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id not found" + id + " verificar id valido");
		}catch(DataIntegrityViolationException e) { //excessao quando tenta apagar objeto associado com outro, chave estrangeira de um fica sem dono
			throw new DataBaseException("Integrity violation");
		}
		
	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> BuscarProdutoPorPagina(Pageable pageable) {
		
		Page<Product> list = productRepository.findAll(pageable);
		return list.map(x-> new ProductDTO(x));		
	}


	private void copyDtoToEntity(ProductDTO dto, Product categoryEntidade) {
		
		//povoando atributos da entidade
		categoryEntidade.setName(dto.getName());
		
		categoryEntidade.setDescription(dto.getDescription());
		categoryEntidade.setDate(dto.getDate());
		categoryEntidade.setImg_url(dto.getImg_url());
		categoryEntidade.setPrice(dto.getPrice());
		
		//limpar conjunto de categorias por ventura esteja na entidade
		categoryEntidade.getCategories().clear();
		
		//copiar somente as categorias
		for(CategoryDTO categoryDTO : dto.getCategories()){// percorre todas categorias do dto estao associadas a lista de categorias ProductDTO
			Category category = categoryRepository.getReferenceById(categoryDTO.getId()); //instancia entidade pelo jpa usando getOne sem ir no banco de dados
			categoryEntidade.getCategories().add(category);//entidade a ser povoada acessa a lista dela e adiciona as categorias
		}
		
		
		
	}
	
	
}


