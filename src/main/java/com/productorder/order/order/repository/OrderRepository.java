package com.productorder.order.order.repository;

import com.productorder.order.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // @EntityGraph
    // - Order 조회 시 product를 함께 fetch
    // - LAZY 설정은 유지하되, 이 메서드에서만 즉시 로딩
    @EntityGraph(attributePaths = "product")
    Page<Order> findAll(Pageable pageable);
}