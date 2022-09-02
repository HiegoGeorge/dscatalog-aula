package com.hiego.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiego.dscatalog.dto.RoleDTO;
import com.hiego.dscatalog.dto.UserDTO;
import com.hiego.dscatalog.dto.UserInsertDTO;
import com.hiego.dscatalog.dto.UserUpdateDTO;
import com.hiego.dscatalog.entities.Role;
import com.hiego.dscatalog.entities.User;
import com.hiego.dscatalog.repositories.RoleRepository;
import com.hiego.dscatalog.repositories.UserRepository;
import com.hiego.dscatalog.services.exceptions.DataBaseException;
import com.hiego.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService{
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;	
	
	@Autowired
	private RoleRepository roleRepository;		
	
	
	@Transactional(readOnly = true)
	public List<UserDTO> Buscar(){		
		 List<User> list = userRepository.findAll();		 
		return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
		 		
	}
	
	@Transactional(readOnly = true)
	public Page<UserDTO> BuscarPaginada(Pageable pageable) {		
		Page<User> list = userRepository.findAll(pageable);
		return list.map(x-> new UserDTO(x));		
	}
	
	
	@Transactional(readOnly = true)
	public UserDTO BuscarPorId(long id){		
		Optional<User> obj = userRepository.findById(id);
		User entidade = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));	
		return new UserDTO(entidade); 
	}

	
	@Transactional(readOnly = true)
	public UserDTO insert (UserInsertDTO dto) {
		
		User userEntidade = new User();		
		copyDtoToEntity(dto,userEntidade);
		userEntidade.setPassword(passwordEncoder.encode(dto.getPassword()));
		userEntidade = userRepository.save(userEntidade);		
		return new UserDTO(userEntidade);
	}


	@Transactional(readOnly = true)
	public UserDTO updateUser(long id, UserUpdateDTO dto) {
		
		try {
		User userEntidade = userRepository.getReferenceById(id);
		copyDtoToEntity(dto,userEntidade);
//		userEntidade.setPassword(passwordEncoder.encode(dto.getPassword()));
		userEntidade = userRepository.saveAndFlush(userEntidade);			
		return new UserDTO(userEntidade);
		
		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id not found" + id + " verificar id valido");
		}
	
	}


	public void delete(long id) {
		try {			
			userRepository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id not found" + id + " verificar id valido");
		}catch(DataIntegrityViolationException e) { //exceção quando tenta apagar objeto associado com outro, chave estrangeira de um fica sem dono
			throw new DataBaseException("Integrity violation");
		}
		
	}


	private void copyDtoToEntity(UserDTO dto, User userEntidade) {
		
		//povoando atributos da entidade
		userEntidade.setFirstName(dto.getFirstName());
		userEntidade.setLastName(dto.getLastName());
		userEntidade.setEmail(dto.getEmail());
		
		//limpar coleção com clear
		userEntidade.getRoles().clear();
		
		//copiar somente as categorias
		for(RoleDTO roleDTO : dto.getRoles()){
			Role role = roleRepository.getReferenceById(roleDTO.getId()); //instancia entidade pelo jpa usando getOne sem ir no banco de dados
			userEntidade.getRoles().add(role);//entidade a ser povoada acessa a lista dela e adiciona as categorias
		}
		
		
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		
		if(username == null) {
			logger.error("Usuario nao encontrado" + username);
			throw new UsernameNotFoundException("Email nao encontrado");
		}
		logger.info("Usuario encontrado: " + username);
		return user;
	}


	
}


