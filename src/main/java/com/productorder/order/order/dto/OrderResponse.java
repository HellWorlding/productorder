package com.productorder.order.order.dto;

import com.productorder.order.order.domain.Order;
import lombok.Getter;

/**
 * 주문 응답 DTO
 * 과제 핵심 요구사항:
 * - 주문 조회 결과에 productName이 포함되어야 한다.
 * - 상품 이름이 변경되면, 기존 주문 조회에서도 변경된 이름이 보여야 한다.
 * 따라서 productName을 Order에 "복사 저장"하지 않고,
 * Order -> Product 연관관계에서 "조회 시점에" 읽어서 응답에 담는다.
 */
@Getter
public class OrderResponse {

    private final Long orderId;
    private final Long productId;
    private final String productName;
    private final int quantity;

    public OrderResponse(Long orderId, Long productId, String productName, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    /**
     * Entity -> DTO 변환 메서드
     * 포인트:
     * - productName = order.getProduct().getName()
     *   -> Product 엔티티의 현재 name을 읽는다(복사 저장 금지)
     */
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProduct().getId(),
                order.getProduct().getName(),
                order.getQuantity()
        );
    }
}