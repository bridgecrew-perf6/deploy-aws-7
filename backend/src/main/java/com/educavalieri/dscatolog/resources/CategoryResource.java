package com.educavalieri.dscatolog.resources;

import com.educavalieri.dscatolog.dto.CategoryDTO;
import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.services.implement.CategoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("*")
    public String test(){
        return "test";
    }
}
