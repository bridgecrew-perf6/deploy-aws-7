package com.educavalieri.dscatolog.repositories;

import com.educavalieri.dscatolog.entities.Product;
import com.educavalieri.dscatolog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private long existID;
    private long noExistsID;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {

        existID = 1L;
        noExistsID = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdISNull(){

        Product product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts +1, product.getId());
    }


    @Test
    public void deleteShouldWhenIdExists(){

        productRepository.deleteById(existID);

        Optional<Product> result = productRepository.findById(existID);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdNotExists(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
            productRepository.deleteById(noExistsID);
        });
    }

    @Test
    public void findByIdShouldReturnEmptyObjectWhenIdNotExists(){

        Optional<Product> product = productRepository.findById(noExistsID);

        Assertions.assertTrue(product.isEmpty());
    }

    @Test
    public void findByIdShouldReturnEmptyObjectWhenIdExists(){

        Optional<Product> product = productRepository.findById(existID);
        Product obj = productRepository.findById(existID).get();
        long aux = obj.getId();

        Assertions.assertTrue(product.isPresent());
        Assertions.assertTrue(existID == aux);
    }





}
