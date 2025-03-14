# kotlin-convenience-store-precourse

## 구현할 기능 목록
- 현재 재고 출력
- 사용자 상품 구매
- 프로모션 적용 여부 확인
- 멤버십 할인 적용 여부 확인
- 영수증 출력
- 추가 구매 여부 확인

### 현재 재고 출력
- 구현에 필요한 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.
- 두 파일 모두 내용의 형식을 유지한다면 값은 수정할 수 있다.

### 사용자 상품 구매
- 구매할 상품과 수량을 입력 받는다. 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.
- 상품과 수량 입력 방식을 지키지 않았을 시 예외 처리한다.
- 존재하지 않는 상품을 입력한 경우 예외 처리한다.
- 구매 수량이 재고 수량을 초과한 경우 예외 처리 한다.

### 프로모션 적용 여부 확인
- 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
- 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.
- 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다.
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.

### 멤버십 할인 적용 여부 확인
- 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
- 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
- 멤버십 할인의 최대 한도는 8,000원이다.
- 멤버십 할인 적용 여부를 입력 받는다.

### 영수증 출력
- 구매 상품 내역: 구매한 상품명, 수량, 가격
- 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록

#### 금액 정보
- 총구매액: 구매한 상품의 총 수량과 총 금액
- 행사할인: 프로모션에 의해 할인된 금액
- 멤버십할인: 멤버십에 의해 추가로 할인된 금액
- 내실돈: 최종 결제 금액

### 추가 구매 여부 확인
- 추가 구매 여부를 확인하기 위해 안내 문구를 출력한다.

### 테스트 코드 작성
- 사용자가 잘못된 값을 입력할 경우 IllegalArgumentException을 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.

