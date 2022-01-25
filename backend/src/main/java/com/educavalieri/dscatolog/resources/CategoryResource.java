package com.educavalieri.dscatolog.resources;

import com.educavalieri.dscatolog.entities.Category;
import com.educavalieri.dscatolog.services.implement.CategoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryServiceImp categoryServiceImp;

    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        List<Category> list = categoryServiceImp.findAll();
        return ResponseEntity.ok().body(list);

    }

    @GetMapping("*")
    public String test(){
        return "test";
    }
}
