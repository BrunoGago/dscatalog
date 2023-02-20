//3 - Resource implementa o Controlador REST (sendo todo o back end uma API para o Front End).
//4- Sempre que o recurso estiver linkado com uma entidade, devemos declarar o nome da entidade + resource

package com.devsuperior.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

//4 - Para implementação da classe controller, devemos usar uma anotação Spring (RestController)
//Observação: Anotation é uma forma enxuta de configurar algo no código, ao invés de programar ela do zero
//5 - A anotação RequestMapping é usada para setarmos a rota do recurso, usando sempre o nome em questão no plural
@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService service;
	
	//End-point: É a primeira rota possível que irá responder algo (salvar, deletar, etc - dentro da rota que setamos em RequestMapping)
	//ResponseEntity: É um objeto do spring que vai encapsular uma resposta HTTP
	//GetMapping: é uma anotação composta que funciona como um atalho para @RequestMapping(method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		
		//15 - Variável criada para trazer todos os dados de service, que por sua vez traz os dados do Banco de Dados
		List<CategoryDTO> list = service.findAll();
		
		//ResponseEntity.ok: permite uma resposta HTTP 200 (requisição com sucesso); O body()é o valor que será retornado, nesse caso, a lista
		return ResponseEntity.ok().body(list);	
	}
	
	//anotação acrescentada com o tipo id, para acionar o método GET por id
	//@PathVariable: Anotação para um pré processando antes da compilação, para configurar o WEBSERVICE, "casando" com a variável GETMAPPING
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
			CategoryDTO dto = service.findById(id);
			return ResponseEntity.ok().body(dto);
	}
	
	//@RequestBody: Para que o end point reconheça o objeto da requisição
	//@PostMapping: No padrão REST, o método Post insere um novo recurso
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
}
