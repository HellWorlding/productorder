package com.productorder.order.order.controller;

import com.productorder.order.order.dto.OrderCreateRequest;
import com.productorder.order.order.dto.OrderResponse;
import com.productorder.order.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    // Controller는 비즈니스 로직을 직접 처리하지 않는다
    // 주문 관련 로직은 OrderService가 담당한다
    private final OrderService orderService;

    // 주문 생성
    // POST /api/order
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201 Created
    public OrderResponse create(@Valid @RequestBody OrderCreateRequest request) {

        // 실제 생성 로직은 Service로 위임
        return orderService.create(request);
    }

    // 주문 단건 조회
    // GET /api/order/{id}
    @GetMapping("/{id}")
    public OrderResponse getOne(@PathVariable("id") Long id) {

        // 조회 로직은 Service로 위임
        return orderService.getOne(id);
    }
}