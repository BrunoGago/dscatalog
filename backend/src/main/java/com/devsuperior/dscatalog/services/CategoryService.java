package com.devsuperior.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

//11 - Service: Anotação JPA que registrar a classe como um componente de injeção de dependência automatizado do Spring
//quem vai gerenciar as instâncias do CategoryService vai ser o spring
@Service
public class CategoryService {

	//12 - injeção de dependência com a interface CategoryRepository
	//13 - Autowired: instância gerenciada pelo Spring
	@Autowired
	private CategoryRepository repository;
	
	public List<Category> findAll(){
		return repository.findAll();
	}
}
