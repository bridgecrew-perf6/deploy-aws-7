package com.educavalieri.dscatolog.resources;

import com.educavalieri.dscatolog.dto.ProductDTO;
import com.educavalieri.dscatolog.services.implement.ProductServiceIMP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductServiceIMP productServiceIMP;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable){
          //  @RequestParam(value = "page", defaultValue = "0") Integer page,
          //  @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
          //  @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
          //  @RequestParam(value = "direction", defaultValue = "DESC") String direction){
          //PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<ProductDTO> list = productServiceIMP.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductDTO> findById(@PathVariable("id") Long id){
        ProductDTO dto = productServiceIMP.findById(id);
        return ResponseEntity.ok().body(dto) ;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO dto){
        dto = productServiceIMP.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @RequestMapping(value = "/put{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProductDTO> update(@PathVariable("id") Long id, @RequestBody ProductDTO dto){
        dto = productServiceIMP.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/delete{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        productServiceIMP.delete(id);
        return ResponseEntity.noContent().build();
    }

}
