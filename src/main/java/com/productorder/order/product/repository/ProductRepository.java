package com.productorder.order.product.repository;

import com.productorder.order.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// Jpa extend -> basic CRUD (save, findById, findAll, deleteById)
public interface ProductRepository extends JpaRepository<Product, Long> {
}