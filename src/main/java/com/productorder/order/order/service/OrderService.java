package com.productorder.order.order.service;

import com.productorder.order.order.domain.Order;
import com.productorder.order.order.dto.OrderCreateRequest;
import com.productorder.order.order.dto.OrderResponse;
import com.productorder.order.order.repository.OrderRepository;
import com.productorder.order.product.domain.Product;
import com.productorder.order.product.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// @Service
// - 이 클래스가 "비즈니스 로직(Service 계층)" 임을 Spring에 알려주는 어노테이션
// - Spring Boot 실행 시 자동으로 객체(Bean)로 생성된다
// - Controller에서 OrderService를 주입(@Autowired / 생성자 주입)할 수 있게 된다
// @RequiredArgsConstructor
// - Lombok 어노테이션
// - final 로 선언된 필드를 파라미터로 받는 생성자를 자동으로 만들어준다
// - 덕분에 Repository를 생성자 주입 방식으로 안전하게 사용할 수 있다
// - new OrderService(...) 를 직접 쓰지 않아도 Spring이 알아서 주입해준다
@Service
@RequiredArgsConstructor
public class OrderService {

    // 주문 저장/조회용 Repository
    private final OrderRepository orderRepository;

    // 주문 생성 시 상품 존재 여부 확인용 Repository
    private final ProductRepository productRepository;

    // @Transactional
    // - 이 메서드를 하나의 "트랜잭션 단위"로 묶어준다
    // - 메서드가 시작되면 트랜잭션 시작
    // - 메서드가 정상 종료되면 commit
    // - 예외(RuntimeException)가 발생하면 rollback
    // - create() 처럼 DB에 INSERT/UPDATE/DELETE 하는 메서드에는 필수
    @Transactional
    public OrderResponse create(OrderCreateRequest request) {

        // productId로 상품 조회
        Product product = productRepository.findById(request.getProductId())
                // 상품이 없으면 주문 생성 자체가 불가능
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // 주문 엔티티 생성
        // product는 연관관계로만 연결 (productName 복사 저장 금지)
        Order order = new Order(product, request.getQuantity());

        // 주문 저장
        // 이 시점에 @PrePersist가 실행되어 orderId가 생성됨
        Order savedOrder = orderRepository.save(order);

        // Entity -> Response DTO 변환
        return OrderResponse.from(savedOrder);
    }

    // @Transactional(Transactional.TxType.SUPPORTS)
    // - "트랜잭션이 있으면 참여하고, 없으면 그냥 실행" 하라는 의미
    // SUPPORTS 는 트랜잭션 전파 방식 중 하나
    // getOne() 트랜잭션 있으면 참여, 없으면 그냥 실행 (새 트랜잭션 안만듦)
    // 조회는 DB 상태를 안바꿈, 트랜잭션 = DB 커넥션 + 락 + 관리 비용 -> 단건 조회마다 트랜잭션 낭비
    // TxType는 메서드를 트랜잭션 환경에서 어떻게 실행할지 옵션
    // - 읽기 전용 조회 메서드(getOne)에 적합
    // - 지금 단계에서는 큰 차이는 없지만,
    //   나중에 성능/락/트랜잭션 전파 개념을 배울 때 중요해짐
//    @Transactional(Transactional.TxType.SUPPORTS)
    // 이게 문제였어!


    @Transactional(readOnly = true) // 조회는 readOnly로: 세션 유지 + 성능상 이점
    public OrderResponse getOne(Long orderId) {

        // 주문 단건 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 주문 응답 DTO로 변환
        // productName은 order -> product -> name 으로 조회됨
        return OrderResponse.from(order);
    }
}