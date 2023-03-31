package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

//DataJpaTest: Usado para receber somente as informações do repositório JPA para realizar os testes

@DataJpaTest
public class ProductRepositoryTests {

    //Devemos fazer a injeção de dependência
    @Autowired
    private ProductRepository productRepository;

    //criação das variáveis universais para os testes das demais classes
    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        //inicialização das variáveis
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        Product product = Factory.creatProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        productRepository.deleteById(existingId);

        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExcWhenIdDoesNotExist(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(nonExistingId);
        });
    }

    @Test
    public void findByIdShouldReturnProductWhenIdIsNotNull(){
        //Arrange dos dados e Action do método
        Optional<Product> product = productRepository.findById(existingId);
        //Assertion de verificação
        Assertions.assertTrue(product.isPresent());
    }

    @Test
    public void findByIdShouldReturnNotProductWhenIdIsNull(){
        //Arrange dos dados e Action do método
        Optional<Product> product = productRepository.findById(nonExistingId);
        //Assertion de verificação
        Assertions.assertFalse(product.isPresent());
    }

}
