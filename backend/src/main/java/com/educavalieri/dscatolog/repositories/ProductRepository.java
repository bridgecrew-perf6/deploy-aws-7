package com.educavalieri.dscatolog.repositories;

import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE "
            + "(COALESCE(:categories) IS NULL OR cats IN :categories) AND "
            + "(:productName = '' OR LOWER(obj.name) LIKE LOWER(CONCAT('%', :productName, '%' )))")
    Page<Product> findAllWithCategoryId(Pageable pageable, List<Category> categories, String productName);


    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
    List<Product> findProductsWithCategories(List<Product> products);

}
