package com.productorder.order.order.domain;

import com.productorder.order.product.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;

@Entity // JPA 관리 영속 엔티티 -> 저장하면 row로 매핑
@Table(name = "orders") // 예약어(order, order by) 회피
@Getter
public class Order {

    //pk 커스텀 주문번호
    @Id
    @Column(name = "order_id")
    private Long id;

    // 상품 이름을 바꿨을때(update) 기존 주문 조회에서도 상품 이름이 바뀌어야
    // Order(N) : Product(1) 상품 당 여러 주문
    // fetch LAZY -> 주문 조회할 때 Product 즉시 같이 조회 안하고 필요할때 DB에서 가져옴(virtual proxy pattern)
    // order.getProduct() 까지는 프록시이고, order.getProduct().getName() 하면 Product Select
    // optional로 관계 필수 설정
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // 외래키 컬럼으로 저장됨 orders에 product_id 생성됨
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // orders 테이블에 quantity 컬럼 생성 (db에 not null 걸음)
    @Column(nullable = false)
    private int quantity;

    // JPA 기본 생성자
    protected Order() {
    }

    public Order(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // INSERT 직전에 자동으로 실행되는 훅. DB에 저장되기 전에 PK 넣는 용도(커스텀)
    // PrePersist는 도메인 이벤트
    // repository에서 save 호출 후 insert 하기 전 메서드 호출

    @PrePersist
    void prePersist() {
        this.id = OrderIdGenerator.nextId();
    }
}