package com.educavalieri.dscatolog.services.implement;

import com.educavalieri.dscatolog.dto.CategoryDTO;
import com.educavalieri.dscatolog.dto.ProductDTO;
import com.educavalieri.dscatolog.entities.Product;
import com.educavalieri.dscatolog.repositories.ProductRepository;
import com.educavalieri.dscatolog.services.exceptions.DataBaseException;
import com.educavalieri.dscatolog.services.exceptions.ResourceNotFoundException;
import com.educavalieri.dscatolog.services.interfaces.ProductServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());
        productRepository.save(entity);

        return new ProductDTO(entity);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = productRepository.findById(id).get();
            entity.setName(dto.getName());
            entity.setDate(dto.getDate());
            entity.setDescription(dto.getDescription());
            entity.setImgUrl(dto.getImgUrl());
            entity.setPrice(dto.getPrice());
            productRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("ID not found" +id);
        }

        return null;
    }

    @Override
    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("ID not found" +id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }

    }

    @Override
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> entity = productRepository.findAll(pageRequest);
        return entity.map(x -> new ProductDTO(x));
    }
}
