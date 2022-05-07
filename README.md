# React - Spring 커피 주문 관리 개인 프로젝트

<br/>

## 프로젝트 목적

Spring Framework 이해도를 높이기 위한 개인 프로젝트.

<br/>

## 프로젝트 환경

**Front-End**

- React

**Back-End**

- Spring Boot
- Spring Core
- Spring MVC
- Spring JDBC
- Spring Thymeleaf

**DataBase**

- MySQL



## 프로젝트 소개

커피 주문 시스템을 개발했습니다. 

React를 이용해 만든 UI에서 사용자가 커피 주문을 할 수 있습니다. 

Thymeleaf를 이용해 만든 관리자 페이지에서 커피 상품에 대해 CRUD 가능합니다. 또 주문을 조회할 수 있고 주문을 처리할 수 있습니다. (아래 그림 참고)

![비지니스플로우](https://user-images.githubusercontent.com/29492667/167259514-d571bb70-da43-4d84-9c6e-9a38f4f97ce8.png)

<br/>

도메인 설계도

![도메인설계도](https://user-images.githubusercontent.com/29492667/167259964-3ea6f66c-c5a9-47fa-bc66-9f86b9a258b3.png)

<br/>

테이블 설계도

![테이블설계도](https://user-images.githubusercontent.com/29492667/167260021-9a29e976-9d02-40e7-a81d-e84abfe8a6b5.png)

<br/>

프로젝트 설계도

![프로젝트설계도](https://user-images.githubusercontent.com/29492667/167260183-bc7a55cb-b398-413c-a9dd-4a4fd5dba1e3.png)

<br/>

## 구현 기능

### 관리자 페이지 (Response Content-Type: html)

**커피**

- 커피 전체 조회 
  - HTTP Method : GET, URI : http://localhost:8080/coffees
- 커피 상세 조회
  - HTTP Method: GET, URI: http://localhost:8080/coffees/{coffeeId}
- 커피 생성 폼 조회
  - HTTP Method: GET, URI: http://localhost:8080/coffees/new
- 커피 생성 요청
  - HTTP Method: Post, URI: http://localhost:8080/coffees/new
- 커피 삭제 요청
  - HTTP Method: Post, URI: http://localhost:8080/coffees/{coffeeId}/delete

**주문**

- 주문 전체 조회 (주문 생성일 기준 정렬)
  - HTTP Method: GET, URI: http://localhost:8080/orders
- 특정 상태 주문 (주문 생성일 기준 정렬)
  - HTTP Method: GET, URI: http://localhost:8080/orders?orderStatus={ORDERED or FINISHED} 
- 주문 상세 조회
  - HTTP Method: GET, URI: http://localhost:8080/orders/{orderId}
- 주문 상태 변경
  - HTTP Method: POST, URI: http://localhost:8080/orders/{orderId}/change-status
- 주문 삭제
  - HTTP Method: POST, URI: http://localhost:8080/orders/{orderId}/delete

<br/>

### 사용자 페이지 (Response Content-Type: json)

**커피**

- 커피 전체 조회
  - HTTP Method: GET, URI: http://localhost:8080/api/v1/coffees

- 주문 생성
  - HTTP Method: POST, URI: http://localhost:8080/api/v1/orders

