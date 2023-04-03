package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//A anotação abaixo é utilizada para teste unitário na camada Controller, mockando a camada service
@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    //Para chamar o end point, usamos o MockMvc
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //Usado quando a classe de teste carrega o contexto da aplicação e precisa mockar um bean do sistemas (end points por ex)
    //Anotações que se usa: @WebMvcTest e @SpringBootTest
    @MockBean
    private ProductService productService;

    //variáveis de teste

    //PageImpl: usado para criar um tipo page concetro, usando o método new
    private PageImpl<ProductDTO> page;

    private ProductDTO productDTO;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() throws Exception{
        productDTO = Factory.createProductDto();
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;


        //simulação do comportamento do findAllPaged da camada service
        //Quando o FindAllPaged for chamado com qualquer argumento, será retornado um objeto page do tipo PageImpl de productDto
        when(productService.findAllPaged(any())).thenReturn(page);

        //simulação do comportamento do findById da camada service
        when(productService.findById(existingId)).thenReturn(productDTO);

        //mockando o lançamento da exceção
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //EQ utilizado para que o any() seja aceito
        when(productService.update(eq(existingId), any())).thenReturn(productDTO);
        when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        //Mockando a camada service com delete
        doNothing().when(productService).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
        doThrow(DatabaseException.class).when(productService).delete(dependentId);

        //Mockando a camada service com insert
        when(productService.insert(any())).thenReturn(productDTO);

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        //Acessa a requisição, aceita o tipo JSON e verifica se o statis está ok (HTTP 200)
        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        //Fazendo o mesmo que acima, mas de uma forma mais legível
        //ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
        //result.andExpect(status().isOk());
    }


    @Test
    public void findByIdShouldReturnProductWhenIdExist() throws Exception {
        mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
        //o $ acessa o objeto Json e o .id seleciona o que no JSON queremos analisar
    }

    @Test
    public void findByIdShouldReturnNotFoundExceptionWhenIdDoesNotExist() throws Exception{
        mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExist() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoeNotExist() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(post("/products")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception{

        mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{

        mockMvc.perform(delete("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    }
}
