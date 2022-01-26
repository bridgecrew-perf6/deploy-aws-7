package com.educavalieri.dscatolog.services.interfaces;

import com.educavalieri.dscatolog.dto.CategoryDTO;
import com.educavalieri.dscatolog.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CategoryServiceInterface {

    List<CategoryDTO> findAll();

    CategoryDTO findById(Long id);

    CategoryDTO insert(CategoryDTO dto);

    CategoryDTO update(Long id, CategoryDTO dto);

    void delete(Long id);

    Page<CategoryDTO> findAllPaged(PageRequest pageRequest);
}
