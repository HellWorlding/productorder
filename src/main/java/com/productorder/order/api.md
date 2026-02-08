## 파일/폴더 구조

```text
src/
└─ main/
   ├─ java/
   │  └─ com/productorder/order/
   │     ├─ OrderApplication.java
   │     ├─ db-schema.md
   │     ├─ product/
   │     │  ├─ controller/
   │     │  │  └─ ProductController.java
   │     │  ├─ service/
   │     │  │  └─ ProductService.java
   │     │  ├─ repository/
   │     │  │  └─ ProductRepository.java
   │     │  ├─ domain/
   │     │  │  └─ Product.java
   │     │  └─ dto/
   │     │     ├─ ProductCreateRequest.java
   │     │     ├─ ProductUpdateRequest.java
   │     │     └─ ProductResponse.java
   │     └─ order/
   │        ├─ api-order.md
   │        ├─ controller/
   │        │  └─ OrderController.java
   │        ├─ service/
   │        │  └─ OrderService.java
   │        ├─ repository/
   │        │  └─ OrderRepository.java
   │        ├─ domain/
   │        │  └─ Order.java
   │        └─ dto/
   │           ├─ OrderCreateRequest.java
   │           └─ OrderResponse.java
   └─ resources/
      └─ application-*.properties
```

---

# 도메인 관계 (과제 기준 v1)

## 과제 조건 요약
- 주문 생성 시 **상품은 1개만 선택**한다.
- 주문 조회 응답에 `productName` 이 포함되어야 한다.
- 상품 이름을 변경하면, 기존 주문 조회 결과의 `productName` 도 변경된 이름으로 보여야 한다.
  - 즉, 주문이 상품 이름을 **복사 저장하지 않고**, `Product` 를 **참조(연관관계)** 해서 조회해야 한다.

## Cardinality (관계 수)
- **Product (1) : Order (N)**
  - 하나의 상품은 여러 주문에서 참조될 수 있다.
- **Order (N) : Product (1)**
  - 하나의 주문은 정확히 하나의 상품만 가진다(참조한다).

## DB 스키마 (v1)

### product
- `id` (PK, BIGINT, auto_increment)
- `name` (VARCHAR, not null)
- `price` (INT, not null)
- (도전) `stock` (INT, not null)

### orders
- `id` (PK, BIGINT, custom: `[yymmdd][13-digit random]`)
- `product_id` (FK -> product.id, not null)
- `quantity` (INT, not null, >= 1)

## ERD (text)

```text
Product(1)  ───────<  Order(N)
                 FK: orders.product_id -> product.id
```

## JPA 매핑 (v1)
- `Order` 쪽에서만 연관관계를 갖는 **단방향 매핑**으로 충분

```java
// Order.java
// 여러 주문이 하나의 상품 참조, 주문 조회 시점에서 상품 즉시 로딩 안함, 주문은 반드시 상태 가짐
@ManyToOne(fetch = FetchType.LAZY, optional = false)
// 외래키로 orders.product_id
@JoinColumn(name = "product_id", nullable = false)
// Product 엔티티에 Order 관련 필드 안둠
private Product product;
```

---

# 확장 설계 (과제 이상: 주문당 여러 상품)

## 언제 바꾸나?
- 주문이 상품을 여러 개 담는 구조(장바구니/주문서)가 필요해지면
- **Order ↔ Product** 관계가 사실상 **N:M** 이 되므로, 중간 엔티티(라인 아이템)를 둔다.

## v2 권장 구조: OrderItem(주문항목) 도입

### Cardinality (v2)
- **Order (1) : OrderItem (N)**
- **Product (1) : OrderItem (N)**

### DB 스키마 (v2)

#### orders
- `id` (PK)
- ...

#### product
- `id` (PK)
- ...

#### order_item
- `id` (PK)  // 또는 (order_id, product_id) 복합키
- `order_id` (FK -> orders.id)
- `product_id` (FK -> product.id)
- `quantity` (INT, not null, >= 1)
- (선택) `order_price` (주문 시점 가격 스냅샷)

### ERD (text)

```text
Order(1)  ───────<  OrderItem(N)  >──────  Product(1)
              FK: order_item.order_id     FK: order_item.product_id
```

### JPA 매핑 (v2, 정석)

```java
// Order.java
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
private List<OrderItem> orderItems = new ArrayList<>();

// OrderItem.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "order_id")
private Order order;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "product_id")
private Product product;

@Column(nullable = false)
private int quantity;
```

## v2에서 상품명 변경 반영 요구사항은?
- 주문 조회 시 `orderItem.getProduct().getName()` 으로 읽으면
- 상품명 변경이 주문 조회 결과에 자동 반영된다.

---

# 도전 과제 힌트 (N+1 회피)

- 주문 목록 조회에서 `Order -> Product` 를 함께 조회하지 않으면 N+1이 발생할 수 있다.
- 해결 방향(둘 중 하나 선택)
  - `join fetch` 사용
  - 또는 `@EntityGraph(attributePaths = "product")` 사용

> 과제 v1(단건 조회)에서는 N+1이 크게 체감되지 않지만, 목록 조회부터는 반드시 의식한다.