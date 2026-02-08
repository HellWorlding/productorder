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
    public ProductResponse create(ProductCreateRequest request) {
        // NOTE: 요청 JSON에 stock을 보내더라도, 여기서 엔티티에 stock을 넣지 않으면
        // DB에는 기본값(대부분 0)으로 저장된다.
        // 따라서 ProductCreateRequest에 getStock()이 있어야 하고,
        // Product 엔티티에도 (name, price, stock) 생성자(또는 stock 세팅 로직)가 필요하다.

        Product product = new Product(request.getName(), request.getPrice(), request.getStock());
        Product saved = productRepository.save(product);

        return new ProductResponse(saved.getId(), saved.getName(), saved.getPrice(), saved.getStock());
    }
    @Transactional(readOnly = true)
    public ProductResponse getOne(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + id));
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock());
    }
    @Transactional(readOnly=true)
    public List<ProductResponse> getAll(){
        return productRepository.findAll().stream()
                .map(p -> new ProductResponse(p.getId(),p.getName(),p.getPrice(),p.getStock())).toList();
    }

    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: "+id));
        product.update(request.getName(), request.getPrice(), request.getStock()); //dirty checking
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock());
    }

    @Transactional
    public void delete(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: "+id));
        productRepository.delete(product);
    }

}
