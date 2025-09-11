package org.example.hamburger.employee;

/**
 * 모든 직원의 공통 기능을 정의하는 추상 클래스
 * Runnable 구현하여 각 직원이 독립적인 스레드에서 동작할 수 있도록 한다.
 *
 * 설계 의도:
 * - 햄버거 가게의 모든 직원(HeadChef, AssistantChef, Cashier)은 동시에 일해야 한다.
 * - 각자 다른 업루를 하지만 공통된 속성(이름, ID, 작업상태)과 생명주기 관리가 필요하다.
 */
public class Employee{

    // 여기서 name과 id는 식별 및 출력을 하기 위한 용도로 필요했다.
    protected String name;
    protected int id;

    /**
     * 직원의 작업 상태 (true: 일 하는 중, false: 퇴근)
     * 왜 volatile인가?
     * 물론 없어도 동작할 가능성이 높다.
     * 다만, 안전한 시스템 종료를 100% 보장하기 위함 + 쓴다고 크게 성능 변화는 없을 것이라서
     */
    protected volatile boolean working = true;

    public Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    // 작업 안전하게 종료하는 메서드: 위에서 volatile 쓴 이유와 연관
    public void stop() {
        working = false;
    }
}
