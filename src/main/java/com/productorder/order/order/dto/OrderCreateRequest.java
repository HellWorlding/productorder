package com.productorder.order.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 생성 요청 DTO
 * - Controller에서 @Valid로 검증하기 위한 "입력 전용" 객체
 * - Entity(Order)를 직접 받지 않는 이유:
 *   1) Entity는 DB 구조/영속성 책임이 있고,
 *   2) 요청 검증/입력 스펙은 API 계약(Contract)이기 때문
 * 필드:
 * - productId: 주문할 상품 ID (필수)
 * - quantity : 주문 수량 (최소 1)
 */
@Getter
@NoArgsConstructor // JSON 역직렬화(Jackson)를 위해 기본 생성자 필요
public class OrderCreateRequest {

    // 주문할 상품의 PK
    // 클라이언트가 Product의 id를 보내줘야 주문 생성 가능
    @NotNull(message = "productId는 필수입니다.")
    private Long productId;

    /**
     * 주문 수량
     * - int는 null이 될 수 없지만, 최소값 검증은 필요
     */
    @Min(value = 1, message = "quantity는 1 이상이어야 합니다.")
    private int quantity;
}