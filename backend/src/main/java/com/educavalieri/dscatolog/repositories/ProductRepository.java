package com.educavalieri.dscatolog.repositories;

import com.educavalieri.dscatolog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
