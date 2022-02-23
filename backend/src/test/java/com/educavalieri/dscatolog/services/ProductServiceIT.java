package com.educavalieri.dscatolog.services;

import com.educavalieri.dscatolog.dto.ProductDTO;
import com.educavalieri.dscatolog.repositories.ProductRepository;
import com.educavalieri.dscatolog.services.exceptions.ResourceNotFoundException;
import com.educavalieri.dscatolog.services.implement.ProductServiceIMP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductServiceIMP productServiceIMP;

    @Autowired
    private ProductRepository productRepository;


    private Long existId;
    private Long noExistId;
    private Long countTotalProducts;

    @BeforeEach
    void setup() throws Exception{
        existId = 1L;
        noExistId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {

        productServiceIMP.delete(existId);

        Assertions.assertEquals(countTotalProducts -1, productRepository.count());

    }

    @Test
    public void deleteShouldTrowResourceNotFoundExceptionWhenIdNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productServiceIMP.delete(noExistId);
        } );

    }

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10() {

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> result = productServiceIMP.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());

    }

    @Test
    public void findAllPagedWithCategoryIdShouldReturnPageWhenPage0Size10() {

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> result = productServiceIMP.findAllPagedWithCategoryId(pageRequest, 0L, "");

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());

    }

    @Test
    public void findAllPagedShouldReturnEmptyPAgeWhenPageDoesNotExist() {

        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> result = productServiceIMP.findAllPaged(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllWithCategoryIDPagedShouldReturnEmptyPAgeWhenPageDoesNotExist() {

        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> result = productServiceIMP.findAllPagedWithCategoryId(pageRequest, 0L, "");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPAgeWhenSortByName() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = productServiceIMP.findAllPaged(pageRequest);

        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

    @Test
    public void findAllPagedWithCategoryIdShouldReturnSortedPAgeWhenSortByName() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = productServiceIMP.findAllPagedWithCategoryId(pageRequest, 0L, "");

        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }




}
