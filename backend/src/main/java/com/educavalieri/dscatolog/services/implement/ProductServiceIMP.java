package com.educavalieri.dscatolog.services.implement;

import com.educavalieri.dscatolog.dto.CategoryDTO;
import com.educavalieri.dscatolog.dto.ProductDTO;
import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.entities.Product;
import com.educavalieri.dscatolog.repositories.CategoryRepository;
import com.educavalieri.dscatolog.repositories.ProductRepository;
import com.educavalieri.dscatolog.services.exceptions.DataBaseException;
import com.educavalieri.dscatolog.services.exceptions.ResourceNotFoundException;
import com.educavalieri.dscatolog.services.interfaces.ProductServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceIMP implements ProductServiceInterface {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> list = productRepository.findAll();
        List<ProductDTO> listDTO = list.stream()
                .map(x -> new ProductDTO(x))
                .collect(Collectors.toList());
        return listDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Override
    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = productRepository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("ID not found" +id);
        }

    }

    @Override
    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("ID not found" +id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }

    }

    @Override
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> entity = productRepository.findAll(pageable);
        return entity.map(x -> new ProductDTO(x));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPagedWithCategoryId(Pageable pageable, Long categoryID, String productName) {
        Category category = (categoryID == 0) ? null : categoryRepository.getOne(categoryID);
        Page<Product> entity = productRepository.findAllWithCategoryId(pageable, category, productName);
        Page<ProductDTO> dto = entity.map(x -> new ProductDTO(x));
        return dto;
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity){
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()){
            Category category = categoryRepository.getOne(catDto.getId());
            entity.getCategories().add(category);

//        exemplo com expressão lambda
//        dto.getCategories().forEach( x -> {
//            Category category = categoryRepository.getById(x.getId());
//            entity.getCategories().add(category);
//        });

        }
    }
}
