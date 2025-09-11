## 단순한 햄버거 주문 시뮬레이션 (CLI)

### 이해를 위한 기본 프로세스 요약:
1. 사용자가 콘솔에 명령어 입력 → Cashier가 주문 처리
2. HeadChef가 주문 확인 → 어떤 요리인지 판단
3. 업무 분배:
- 복잡한 요리(햄버거): HeadChef가 직접 조리
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
- 쉬운 요리 (Fries, Salad):
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


### 메뉴아이템 보완 클래스 다이어그램(Employee쪽은 위와 동일합니다.)
### 변경된 프로세스:
- 이전: HeadChef가 직접 메뉴 이름을 보고 각 메뉴에 맞는 복잡도 판단하는 형태
- 이후: MenuItem이 자신의 속성을 바탕으로 적절한 조리 전략 결정한 후 반환

### 주요 차이점:
- HeadChef의 판단 로직이 각 MenuItem의 전략 객체로 이동
- 각 메뉴가 자신이 누구한테 조리되어야 하는지 스스로 알고 있는 구조
- 복잡한 요리(Burger, Pizza): HeadChef 직접 조리
- 쉬운 요리(Fries, Salad): AssistantChef 위임 조리

### 구현 전 - 예상 CLI 동작 모습
- 아래는 이상적인 접수-조리-완료-접수 모습이고, 실제로는 멀티스레드 특성상 조리 완료 메시지가 사용자 다음 주문(입력)중에 나타날 수도 있을 것 같습니다.

```
=== 햄버거 주문 시뮬레이션 ===
[시스템] 햄버거 주문 시뮬레이션 시작.

customer> order         // 사용자 입력 (메인 스레드)

[Cashier-Thread] 메뉴를 보여드리겠습니다
=================== 메뉴 ===================
  1. Burger - 8500원 (조리시간: 3분)
  2. Pizza - 12000원 (조리시간: 5분)
  3. Fries - 3500원 (조리시간: 2분)
  4. Salad - 6500원 (조리시간: 2분)
==========================================
[Cashier-Thread] 주문하실 번호를 입력해주세요 (1-4):

customer> 1         // 사용자 입력 (메인 스레드)

[Cashier-Thread] Burger 주문 접수됨 (8500원)
[Cashier-Thread] 주문 #1 접수 완료!

[HeadChef-Thread] 복잡한 요리로 판단, HeadChef가 직접 조리 시작
[HeadChef-Thread] 주문 #1 (Burger) 완성!

customer> order         // 사용자 입력 (메인 스레드)

[Cashier-Thread] 메뉴를 보여드리겠습니다(위와 동일해서 생략)
[Cashier-Thread] 주문하실 번호를 입력해주세요 (1-4):

customer> 3         // 사용자 입력 (메인 스레드)

[Cashier-Thread] Fries 주문 접수됨 (3500원)
[Cashier-Thread] 주문 #2 접수 완료!

[HeadChef-Thread] 쉬운 요리로 판단, AssistantChef에게 지시
[AssistantChef-Thread] HeadChef로부터 주문 #2 (Fries) 지시받음, 조리 시작
[AssistantChef-Thread] 주문 #2 (Fries) 완성!

customer> status         // 사용자 입력 (메인 스레드)

============== 시스템 현황 ==============
OrderQueue 대기 중: 0개
HeadChef: 대기 중
AssistantChef: 대기 중
완성된 주문: 2개
--- 완성된 주문 ---
  주문 #1: Burger (COMPLETED)
  주문 #2: Fries (COMPLETED)
총 매출: 12,000원
=======================================

customer> quit         // 사용자 입력 (메인 스레드)
[시스템] 프로그램을 종료합니다. 감사합니다!
```

![20250910_182608](https://github.com/user-attachments/assets/3d3384c3-7ed1-48cc-b9c9-6fbd83690d43)
