package com.productorder.order.product.service;

import com.productorder.order.product.domain.Product;
import com.productorder.order.product.dto.ProductCreateRequest;
import com.productorder.order.product.dto.ProductResponse;
import com.productorder.order.product.dto.ProductUpdateRequest;
import com.productorder.order.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    @Transactional
    public ProductResponse create(ProductCreateRequest request){
        Product product = new Product(request.getName(), request.getPrice());
        Product saved = productRepository.save(product);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getPrice());
    }
    @Transactional(readOnly = true)
    public ProductResponse getOne(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + id));
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
    @Transactional(readOnly=true)
    public List<ProductResponse> getAll(){
        return productRepository.findAll().stream()
                .map(p -> new ProductResponse(p.getId(),p.getName(),p.getPrice())).toList();
    }

    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: "+id));
        product.update(request.getName(), request.getPrice()); //dirty checking
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    @Transactional
    public void delete(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: "+id));
        productRepository.delete(product);
    }

}
