package org.example.hamburger.employee;

import org.example.hamburger.order.Order;
import org.example.hamburger.order.OrderStatus;
import org.example.hamburger.system.RestaurantService;

import java.util.List;

/**
 * 모든 요리사(HeadChef, AssistantChef)의 공통 기능을 정의하는 추상 클래스
 * Employee를 상속받아 요리사만의 특별한 기능들을 추가
 */
public abstract class Chef extends Employee{

    // 하위 클래스(요리사들)에서 직접 접근 필요해서 protected
    protected RestaurantService restaurantService;
    protected List<String> specialties;

    /**
     * 현재 조리 중인지 상태 표시
     * 기본적으로 employee의 work와 마찬가지로 volatile 안써도 가능은 하지만 비슷한 이유이기는 한 것 같다.
     * 조금 다른건, 여기서는 서로 다른 스레드가 읽고 쓰는 변수라서 있어야 하는게 더 좋은 것 같다.
     */
    protected volatile boolean busy = false;

    public Chef(String name, int id, RestaurantService restaurantService) {
        super(name, id);
        this.restaurantService = restaurantService;
    }

    // 현재 조리 중인지 확인하는 메서드
    public boolean isBusy() {
        return busy;
    }

    protected void cookOrder(Order order) {
        try {
            // 1. 조리 시작: busy 상태 변경
            busy = true;
            order.setStatus(OrderStatus.COOKING);

            // 2. 조리 시간만큼 대기 (이거는 음 대략? 실제 상황 시뮬레이션 위해 임의로 정한 시간)
            Thread.sleep(order.getMenuItem().getCookTime() * 1000);

            // 3. 조리 완료 처리
            order.setStatus(OrderStatus.COMPLETED);
            restaurantService.addCompleteOrder(order); // 완성품을 추가해서 관리 (db 따로 없으니까)

            // 4. 완료 메시지 출력
            // 연습 겸 콘솔 로그 출력 좀 너무 뒤죽박죽 말고 깔끔하게 보려고 표준 출력을 락 객체로 사용했습니다.
            synchronized (System.out) {
                System.out.println();
                System.out.println("================================");
                System.out.println("[" + Thread.currentThread().getName() + "] 주문 #" + order.getOrderId() +
                        " (" + order.getMenuItem().getName() + ") 완성! ✓");
                System.out.println("================================");
                System.out.println();
                System.out.print("customer> ");
            }

            // 5. 조리 완료
            busy = false;
        } catch (InterruptedException e) {
            // 스레드 인터럽트 발생 시
            Thread.currentThread().interrupt();
            busy = false;
        }
    }

    protected abstract String getChefType();
}
