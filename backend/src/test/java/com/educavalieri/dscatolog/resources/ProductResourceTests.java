package com.educavalieri.dscatolog.resources;

import com.educavalieri.dscatolog.dto.ProductDTO;
import com.educavalieri.dscatolog.services.exceptions.DataBaseException;
import com.educavalieri.dscatolog.services.exceptions.ResourceNotFoundException;
import com.educavalieri.dscatolog.services.implement.ProductServiceIMP;
import com.educavalieri.dscatolog.tests.Factory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import java.awt.*;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceIMP productServiceIMP;

    @Autowired
    private ObjectMapper objectMapper;


    public ProductResourceTests() {
    }

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existId;
    private Long noExistId;
    private Long dependentID;

    @BeforeEach
    void setup() throws Exception{

        existId = 1L;
        noExistId = 1000L;
        dependentID = 3L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        when(productServiceIMP.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
        when(productServiceIMP.findById(existId)).thenReturn(productDTO);
        when(productServiceIMP.findById(noExistId)).thenThrow(ResourceNotFoundException.class);
        when(productServiceIMP.update(eq(existId), any())).thenReturn(productDTO);
        when(productServiceIMP.update(eq(noExistId), any())).thenThrow(ResourceNotFoundException.class);
        when(productServiceIMP.insert(any())).thenReturn(productDTO);

        //quando o método é void (como o delete) iverter a ordem das ações

        doNothing().when(productServiceIMP).delete(existId);
        doThrow(ResourceNotFoundException.class).when(productServiceIMP).delete(noExistId);
        doThrow(DataBaseException.class).when(productServiceIMP).delete(dependentID);

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products")).andExpect(status().isOk());

//        Pode Ser assim:
//        ResultActions result =
//                mockMvc.perform(get("/products")
//                        .accept(MediaType.APPLICATION_JSON));


//        Pode ser realizado desta forma:
//        ResultActions result = mockMvc.perform(get("/products"));
//        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception{

        ResultActions result = mockMvc.perform(get("/products/{id}", existId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

 @Test
    public void findByIdShouldReturnNotFoundWhenIdNotExists() throws Exception{

        ResultActions result = mockMvc.perform(get("/products/{id}", noExistId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/put{id}", existId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void insertShouldReturnProductDTOWhenIdExists() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products/save")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }


    @Test
    public void updateShouldReturnNotFoundWhenIdExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/put{id}", noExistId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception{

        ResultActions result = mockMvc.perform(delete("/products/delete{id}", existId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdNotExists() throws Exception{

        ResultActions result = mockMvc.perform(delete("/products/delete{id}", noExistId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }



}
