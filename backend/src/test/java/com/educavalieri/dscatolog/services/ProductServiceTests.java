package com.educavalieri.dscatolog.services;

import com.educavalieri.dscatolog.dto.ProductDTO;
import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.entities.Product;
import com.educavalieri.dscatolog.repositories.CategoryRepository;
import com.educavalieri.dscatolog.repositories.ProductRepository;
import com.educavalieri.dscatolog.services.exceptions.DataBaseException;
import com.educavalieri.dscatolog.services.exceptions.ResourceNotFoundException;
import com.educavalieri.dscatolog.services.implement.ProductServiceIMP;
import com.educavalieri.dscatolog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    private Long existId;
    private Long nonExistId;
    private Long dependentId;
    private PageImpl<Product> page;
    Product product = Factory.createProduct();
    ProductDTO dto = new ProductDTO();
    ProductDTO productDTO = new ProductDTO();
    private Category category;

    @BeforeEach
    void setup() throws Exception {
       existId = 1L;
       nonExistId = 1000L;
       dependentId = 4L;
       page = new PageImpl<>(List.of(product));
       Category category = Factory.createCategory();
       productDTO = Factory.createProductDTO();

        //quando o método é void (como o delete) iverter a ordem das ações

       doNothing().when(productRepository).deleteById(existId);
       doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistId);
       doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

       when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
       when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
       when(productRepository.findById(existId)).thenReturn(Optional.of(product));
       when(productRepository.findById(nonExistId)).thenReturn(Optional.empty());
       when(productRepository.getOne(existId)).thenReturn(product);
       when(productRepository.getOne(nonExistId)).thenThrow(EntityNotFoundException.class);
       when(categoryRepository.getOne(existId)).thenReturn(category);
       when(categoryRepository.getOne(nonExistId)).thenThrow(ResourceNotFoundException.class);
    }

    @InjectMocks
    private ProductServiceIMP productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existId);
        });

        verify(productRepository, times(1)).deleteById(existId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           productService.delete(nonExistId);
        });

        verify(productRepository, times(1)).deleteById(nonExistId);
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependentId(){

        Assertions.assertThrows(DataBaseException.class, () -> {
           productService.delete(dependentId);
        });

        verify(productRepository, times(1)).deleteById(dependentId);
    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){

        dto = productService.findById(existId);

        Assertions.assertNotNull(dto);
        Mockito.verify(productRepository, Mockito.times(1)).findById(existId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExists(){


        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            dto = productService.findById(nonExistId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).findById(nonExistId);
    }

    @Test
    public void updateEntityWhenIdExistsShouldReturnProductDTO() {

        dto = productService.update(existId, productDTO);

        Assertions.assertNotNull(dto);
    }

    @Test
    public void updateEntityWhenIdNotExistsShouldThrowResourceNotFoundException() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(nonExistId, productDTO);
        });
    }





}
