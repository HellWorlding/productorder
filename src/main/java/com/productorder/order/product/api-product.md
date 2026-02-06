# Product API (v1)

Base URL: `/api`

## URL 규칙
- POST   `/api/product`
- GET    `/api/product/{id}`
- GET    `/api/product`
- PUT    `/api/product/{id}`
- DELETE `/api/product/{id}`

---

## Product Data Model
- id: Long (PK, auto increment)
- name: String (필수)
- price: int (0 이상)

---

## 1) 상품 등록

### POST `/api/product`

#### Request Body
```json
{
  "name": "콜드브루",
  "price": 4500
}
```

#### Response (201 Created)
```json
{
  "id": 1,
  "name": "콜드브루",
  "price": 4500
}
```

#### Validation
- name: NotBlank
- price: Min(0)

---

## 2) 상품 단건 조회

### GET `/api/product/{id}`

#### Response (200 OK)
```json
{
  "id": 1,
  "name": "콜드브루",
  "price": 4500
}
```

---

## 3) 상품 목록 조회

### GET `/api/product`

#### Response (200 OK)
```json
[
  {
    "id": 1,
    "name": "콜드브루",
    "price": 4500
  },
  {
    "id": 2,
    "name": "라떼",
    "price": 5000
  }
]
```

---

## 4) 상품 수정

### PUT `/api/product/{id}`

#### Request Body
```json
{
  "name": "콜드브루(L)",
  "price": 5000
}
```

#### Response (200 OK)
```json
{
  "id": 1,
  "name": "콜드브루(L)",
  "price": 5000
}
```

---

## 5) 상품 삭제

### DELETE `/api/product/{id}`

#### Response (204 No Content)
- Body 없음
