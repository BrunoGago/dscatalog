package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//Teste unitário com mockito
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

    //Usado quando a classe de teste carrega o CONTEXTO e precisa MOCKAR um BEAN (@WebMvcTest e @SprinBootTest)
    @MockBean
    private ProductRepository productRepository2;
}
