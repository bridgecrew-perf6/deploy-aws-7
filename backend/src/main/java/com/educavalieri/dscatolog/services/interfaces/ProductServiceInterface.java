package com.educavalieri.dscatolog.services.interfaces;

import com.educavalieri.dscatolog.dto.CategoryDTO;
import com.educavalieri.dscatolog.dto.ProductDTO;
import com.educavalieri.dscatolog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductServiceInterface {

    List<ProductDTO> findAll();

    ProductDTO findById(Long id);

    ProductDTO insert(ProductDTO dto);

    ProductDTO update(Long id, ProductDTO dto);

    void delete(Long id);

    Page<ProductDTO> findAllPaged(Pageable pageable);

    Page<ProductDTO> findAllPagedWithCategoryId(Pageable pageable, Long categoryID, String productName);

    Page<ProductDTO> findProductWithCategory(Pageable pageable, Long categoryID, String productName);
}
