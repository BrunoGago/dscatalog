package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

//Anotação para carregar o contexto da aplicação e gerar o teste de Integração
//Transactional: Usada para garantir o RollBack de cada test
@SpringBootTest
@Transactional
public class ProductServiceIT {

    //Injeção de dependência para teste de integração
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    //variáveis universais para os testes

    private long existingId;
    private long nonExistId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists(){

        productService.delete(existingId);

        //Verificação se o total de registros no BD são iguais ao previsto
        Assertions.assertEquals(countTotalProducts-1, productRepository.count());

    }

    @Test
    public void deleteShouldThrowResourceNotFoundWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           productService.delete(nonExistId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPagedWhenPage0Size10(){

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        //retorna verdadeiro se a pagina estiver vazia, o que nao pode acontecer
        Assertions.assertFalse(result.isEmpty());

        //Verifica se é a página 0
        Assertions.assertEquals(0, result.getNumber());

        //Verifica se a página tem 10 itens
        Assertions.assertEquals(10, result.getSize());

        //Verifica se o total de itens é o mesmo do BD
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPagedWhenPageDoesNotExist(){
        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPagedWhenSortedByName(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = productService.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

}
