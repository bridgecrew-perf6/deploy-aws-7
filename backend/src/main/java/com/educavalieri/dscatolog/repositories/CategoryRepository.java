package com.educavalieri.dscatolog.repositories;

import com.educavalieri.dscatolog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long>{
}
