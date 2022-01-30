package com.educavalieri.dscatolog.services.implement;

import com.educavalieri.dscatolog.dto.CategoryDTO;
import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.repositories.CategoryRepository;
import com.educavalieri.dscatolog.services.exceptions.DataBaseException;
import com.educavalieri.dscatolog.services.exceptions.ResourceNotFoundException;
import com.educavalieri.dscatolog.services.interfaces.CategoryServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryServiceInterface {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {

        List<Category> list = categoryRepository.findAll();
        List<CategoryDTO> listDTO = list.stream()
                .map( x -> new CategoryDTO(x))
                .collect(Collectors.toList());
        return listDTO;
    }

    @Override
    @Transactional
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = categoryRepository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }

    @Override
    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }

    @Override
    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = categoryRepository.findById(id).get();
            entity.setName(dto.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("ID not found" +id);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found" +id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        Page<Category> list = categoryRepository.findAll(pageable);
        return list.map(x -> new CategoryDTO(x));
    }


}
