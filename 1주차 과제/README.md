## 단순한 햄버거 주문 시뮬레이션 (CLI)

### 이해를 위한 기본 프로세스 요약:
1. 사용자가 콘솔에 명령어 입력 → Cashier가 주문 처리
2. HeadChef가 주문 확인 → 어떤 요리인지 판단
3. 업무 분배:
- 어려운 요리(햄버거): HeadChef가 직접 조리
- 쉬운 요리(사이드 음식): AssistantChef에게 지시
4. 조리 완료 → 콘솔에 완료 메시지 출력
<br/>

### 프로세스 상세:
1. 사용자 입력 단계
- 사용자: "order" 입력 → 메뉴 화면 출력 → 번호 선택
- Cashier: 선택된 MenuItem으로 Order 생성 → OrderQueue에 추가
2. HeadChef가 주문 확인
- HeadChef: OrderQueue를 지속적으로 확인 (별도 스레드)
- 새 주문 발견 → Order를 큐에서 가져옴 → 요리 복잡도 판단
3. 업무 분배 결정
- 복잡한 요리 (Burger, Pizza):
  - HeadChef → 직접 조리 → 본인이 처리
- 간단한 요리 (Fries, Salad):
  - HeadChef → AssistantChef에게 지시 → AssistantChef의 개별 큐에 Order 전달
4. 조리 완료
- HeadChef: 주문들을 순차적으로 조리
- AssistantChef: 할당받은 주문들을 별도로 조리 (동시 진행 가능)
- 각각 완료 시 콘솔에 "조리 완성" 메시지 출력
5. 상태 확인
- 사용자: "status" 입력 시 → OrderQueue 대기 상황, 각 셰프 작업 현황, 완료된 주문 목록 출력
<br/>

### OrderStatus(Enum) - 각 조리 중 상태 구분은 ENUM으로
- ORDERED
- COOKING
- COMPLETED

![class_diagram](https://github.com/user-attachments/assets/9282195c-96d1-4413-ae44-583175aaa61d)
