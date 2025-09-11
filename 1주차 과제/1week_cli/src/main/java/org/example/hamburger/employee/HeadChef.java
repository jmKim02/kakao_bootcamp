package org.example.hamburger.employee;

import org.example.hamburger.config.ChefConfig;
import org.example.hamburger.order.Order;
import org.example.hamburger.strategy.CookingStrategy;
import org.example.hamburger.system.RestaurantService;

/**
 * 주방장 클래스 - 주문을 분석하고 업무를 분배하는 관리자 역할
 * Chef를 상속받아 요리 기능을 가지면서, 동시에 AssistantChef를 관리하는 매니저 역할 수행
 *
 * 핵심 책임:
 * 1. Restaurant의 주문 큐 모니터링
 * 2. 각 주문의 복잡도 분석 (Strategy 패턴 활용)
 * 3. 업무 분배 결정 (본인 vs AssistantChef)
 * 4. 복잡한 요리 직접 조리
 */
public class HeadChef extends Chef{

    private AssistantChef assistant;

    public HeadChef(String name, int id, RestaurantService restaurant) {
        super(name, id, restaurant);
        this.specialties = ChefConfig.getSpecialties("HeadChef");
    }

    public void setAssistantChef(AssistantChef assistantChef) {
        this.assistant = assistantChef;
    }

    @Override
    protected String getChefType() {
        return "HeadChef";
    }

    // cashier나 assistantChef와 같은 이유.. 우선 제외
    // 우선은 그냥 간단한 텍스트로 대체.
    @Override
    public void work() {
        System.out.println("[" + Thread.currentThread().getName() + "] " + name + " 근무 시작");
        System.out.println("[" + Thread.currentThread().getName() + "] 주방 장비 점검 중...");
        System.out.println("[" + Thread.currentThread().getName() + "] 오늘의 재료 상태 확인 완료");
        System.out.println("[" + Thread.currentThread().getName() + "] 메뉴별 조리 순서 계획 수립");
        System.out.println("[" + Thread.currentThread().getName() + "] 주방 준비 완료!");
    }

    /**
     * Runnable 인터페이스 구현 - 독립적인 스레드로 실행
     *
     * 동작 방식: "주문 모니터링 및 분배형"
     * 1. Restaurant의 공용 주문 큐를 지속적으로 모니터링
     * 2. 새 주문이 들어오면 즉시 가져와서 분석
     * 3. Strategy 패턴으로 업무 분배 결정
     * 4. 본인 조리 또는 AssistantChef에게 위임
     */
    @Override
    public void run() {
        while (working) { // Employee의 volatile boolean working 사용
            try {

                Order order = restaurantService.getNextOrder();

                // 가져온 주문 분석하고 처리
                processOrder(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Strategy 패턴 활용:
     * 1. MenuItem이 자신의 CookingStrategy 보유
     * 2. Strategy가 적절한 Chef 결정
     * 3. HeadChef는 Strategy의 결정을 따름
     */
    private void processOrder(Order order) {

        // 1. 주문의 메뉴에서 조리 전략 가져오기
        CookingStrategy strategy = order.getMenuItem().getStrategy();

        // 2. 조리 전략에 따라 담당 Chef 결정
        Chef assignedChef = strategy.getAssignedChef(this, assistant);

        // 3. 업무 분배 결정 메시지 출력
        synchronized (System.out) {
            System.out.println();
            System.out.println("[" + Thread.currentThread().getName() + "] " + strategy.getAssignmentMessage());
        }

        // 4. 결정된 담당자에 따라 실제 업무 처리
        if (assignedChef == this) {
            cookOrder(order);
        } else {
            assignToAssistant(order);
        }
    }

    // assistant에게 위임
    private void assignToAssistant(Order order) {
        if (assistant != null) {
            assistant.receiveAssignment(order);
        }
    }
}
