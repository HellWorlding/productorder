
---

# Order API (v1)

Base URL: `/api/order`

## URL 규칙
- POST   `/api/order`
- GET    `/api/order/{id}`

---


- 참고: md 파일을 `src/main/java` 아래에 두는 방식은 과제/학습용으로 허용. (실무에선 보통 `/docs` 또는 `src/main/resources/docs` 등으로 분리)

---


- id: Long (PK, custom: [yymmdd][13-digit random])
- product (1) : order (N)
- productId: Long (FK)
- quantity: int (1 이상)
    - ※ 재고(stock) 도전 과제 대비용으로 포함 권장

---

## 1) 주문 생성

### POST `/api/order`

#### Request Body
```json
{
  "productId": 1,
  "quantity": 2
}
```

#### Response (201 Created)
```json
{
  "orderId": 2602060000000123456,
  "productId": 1,
  "productName": "콜드브루",
  "quantity": 2
}
```

#### Validation
- productId: NotNull
- quantity: Min(1)

---

## 2) 주문 단건 조회

### GET `/api/order/{id}`

#### Response (200 OK)
```json
{
  "orderId": 2602060000000123456,
  "productId": 1,
  "productName": "콜드브루",
  "quantity": 2
}
```

---

## 요구사항 체크 (중요)
- 주문 조회 응답에 `productName` 이 포함되어야 합니다.
- 상품 이름이 변경되면, 기존 주문 조회 결과의 `productName` 도 변경된 이름으로 보여야 합니다.
    - 즉, 주문이 `productName` 을 복사 저장하지 않고 `Product` 와 연관관계로 조회되도록 구현합니다.
