package com.educavalieri.dscatolog.repositories;

import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE "
            + "(:category IS NULL OR :category IN cats) AND "
            + "(LOWER(obj.name) LIKE LOWER(CONCAT('%', :productName, '%' )))")
    Page<Product> findAllWithCategoryId(Pageable pageable, Category category, String productName);
}
