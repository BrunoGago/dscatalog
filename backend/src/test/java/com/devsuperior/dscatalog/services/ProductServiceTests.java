package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

//Teste unitário com mockito para a camada de repositório
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    //Não podemos usar o Autowired pois seria um teste de integração
    //O InjectMocks
    @InjectMocks
    private ProductService productService;

    //Essa anotação gera um repository de "mentira" para termos um teste sem carregar o contexto da aplicação
    //Usado para NAO CARREGAR O CONTEXTO (@ExtendWith)
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    //Usado quando a classe de teste carrega o CONTEXTO e precisa MOCKAR um BEAN (@WebMvcTest e @SprinBootTest)
    //@MockBean
    //private ProductRepository productRepository2;

    private long existingId;
    private long nonExistId;
    private long dependentId;

    //Tipo concreto que representa uma página de dados
    private PageImpl<Product> page;

    private Product product;

    private Category category;

    private ProductDTO productDTO;

    //BeforeEach é usado para configuração, inicialização de uma variável universal nos testes, etc
    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistId = 1000L;
        dependentId = 4L;
        product = Factory.creatProduct();
        page = new PageImpl<>(List.of(product));
        category = Factory.createCategory();
        productDTO = Factory.createProductDto();

        //configuração para findAll que retorna uma Page
        //como o método findAll retorna vários tipos de de dados, temos que fazer um casting de ArgumentMatchers para Pageable
        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        //Configuração de Save para teste mock
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        //teste com retorno de um product com id existente
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));

        //teste de retorno nulo para Id não existente
        Mockito.when(productRepository.findById(nonExistId)).thenReturn(Optional.empty());

        //Configuração do objeto mockado, quando chamar o DeleteById com o ID existente, não será feito nada (VOID) e nem deverá ter
        //exceção lançada
        Mockito.doNothing().when(productRepository).deleteById(existingId);

        //Configuração para lançar exceção quando o ID nao existir
        Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistId);

        //Configuração de comportamento simulado para lançar exceção quando o delete do banco tiver integridade violada (deletar PK sem deletar FK)
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

        //Configuração de comportamento simulado do GetOne quando o ID existir, retornando o product
        Mockito.when(productRepository.getOne(existingId)).thenReturn(product);

        //Configuração de comportamento simulado do GetOne quando o ID não existir, retornando uma exceção
        Mockito.when(productRepository.getOne(nonExistId)).thenThrow(EntityNotFoundException.class);

        //Configuração de comportamento simulado do GetOne quando o ID existir, retornando o product
        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);

        //Configuração de comportamento simulado do GetOne quando o ID não existir, retornando uma exceção
        Mockito.when(categoryRepository.getOne(nonExistId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = productService.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
    }
    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependetId(){
        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        //Vai verificar se o método "deleteById" foi chamado pelo Mock de productoRepository
        //Podemos usar Mockito. para acessar diversos métodos da infertace
        Mockito.verify(productRepository).deleteById(existingId);
    }


    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists(){
        ProductDTO result = productService.findById(existingId);

        Assertions.assertTrue(existingId == result.getId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(nonExistId);
        });

        //Vai verificar se o método "findById" foi chamado pelo Mock de productoRepository
        Mockito.verify(productRepository, Mockito.times(1)).findById(nonExistId);
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExist(){
        //Update pede um Long Id e um ProductDto
        ProductDTO result = productService.update(existingId, productDTO);

        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(nonExistId, productDTO);
        });
    }

}
