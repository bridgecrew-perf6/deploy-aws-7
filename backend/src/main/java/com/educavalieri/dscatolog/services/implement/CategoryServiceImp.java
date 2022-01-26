package com.educavalieri.dscatolog.services.implement;

import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.repositories.CategoryRepository;
import com.educavalieri.dscatolog.services.interfaces.CategoryServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryServiceInterface {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {

        return categoryRepository.findAll();
    }
}
