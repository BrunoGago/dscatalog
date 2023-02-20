package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

//11 - Service: Anotação JPA que registrar a classe como um componente de injeção de dependência automatizado do Spring
//quem vai gerenciar as instâncias do CategoryService vai ser o spring
@Service
public class CategoryService {

	//12 - injeção de dependência com a interface CategoryRepository
	//13 - Autowired: instância gerenciada pelo Spring
	@Autowired
	private CategoryRepository repository;
	
	//Transactional: Anotação spring, o framework garante que a transação será feita no BD.
	//readOnly: Ao setarmos como true, evita que seja feito o looking no banco de dados so para leitura, melhorando a performance.
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
		
		Page<Category> list = repository.findAll(pageRequest);
		
		//Utilizei a função lambda para transformar os dados da lista de Category para um dado de CategoryDTO
		return list.map(x -> new CategoryDTO(x));
	}

	//Optional: Utilizado para evitar trabalharmos com valor nulo (desde o Java 8)
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));
		return new CategoryDTO(entity);
	}

	//Método criado para salvar o objeto no repository, transformando o Category para CategoryDTO
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	//getReferenceById: Garante que o objeto seja instanciado em memória antes de acionar o BD
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try{
			Category entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID (" + id + ") NOT FOUND!");
		}
	}
	
	//Não colocamos @Transaction, pois vamos capturar uma exceção do BD
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("ID (" + id + ") NOT FOUND!");
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation, please ensure to delete a valid item!");
		}
	}
}
