package com.educavalieri.dscatolog.tests;

import com.educavalieri.dscatolog.dto.ProductDTO;
import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-07-13T20:50:07.12345Z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());

    }

    public static Category createCategory(){
        return new Category(1L, "Electronics");
    }
}
