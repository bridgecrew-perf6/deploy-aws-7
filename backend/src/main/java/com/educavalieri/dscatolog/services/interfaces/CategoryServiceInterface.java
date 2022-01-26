package com.educavalieri.dscatolog.services.interfaces;

import com.educavalieri.dscatolog.dto.CategoryDTO;
import com.educavalieri.dscatolog.entities.Category;

import java.util.List;

public interface CategoryServiceInterface {

    List<CategoryDTO> findAll();

    CategoryDTO findById(Long id);

    CategoryDTO insert(CategoryDTO dto);

}
