//3 - Resource implementa o Controlador REST (sendo todo o back end uma API para o Front End).
//4- Sempre que o recurso estiver linkado com uma entidade, devemos declarar o nome da entidade + resource

package com.devsuperior.dscatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.entities.Category;

//5 - Para implementação da classe controller, devemos usar uma anotação Spring (RestController)
//Observação: Anotation é uma forma enxuta de configurar algo no código, ao invés de programar ela do zero
//6- A anotação RequestMapping é usada para setarmos a rota do recurso, usando sempre o nome em questão no plural
@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	//End-point: É a primeira rota possível que irá responder algo (salvar, deletar, etc - dentro da rota que setamos em RequestMapping)
	//ResponseEntity: É um objeto do spring que vai encapsular uma resposta HTTP
	//GetMapping: é uma anotação composta que funciona como um atalho para @RequestMapping(method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<List<Category>> findAll(){
		//Obs: List é uma interface, e para instanciação, devemos usar uma classe que implementa a interface (ArrayList)
		List<Category> list = new ArrayList<>();
		//o L na frente do número refere-se a LONG
		list.add(new Category(1L, "Books"));
		list.add(new Category(2L, "Electronics"));
		//ResponseEntity.ok: permite uma resposta HTTP 200 (requisição com sucesso); O body()é o valor que será retornado, nesse caso, a lista
		return ResponseEntity.ok().body(list);
		
	}
}
