package com.educavalieri.dscatolog.resources;

import com.educavalieri.dscatolog.dto.CategoryDTO;
import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.services.implement.CategoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryServiceImp categoryServiceImp;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDTO>> findAll(){
        List<CategoryDTO> list = categoryServiceImp.findAll();
        return ResponseEntity.ok().body(list);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CategoryDTO> findByID(@PathVariable("id") Long id){
        CategoryDTO dto = categoryServiceImp.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> insert (@RequestBody CategoryDTO dto) {
        dto = categoryServiceImp.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);

    }
    @RequestMapping(value = "/put{id}", method = RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto){
        dto = categoryServiceImp.update(id, dto);
        return ResponseEntity.ok().body(dto);

    }


    @DeleteMapping(value = "/delete{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        categoryServiceImp.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("*")
    public String test(){
        return "test";
    }
}
