package org.example.hamburger.employee;

import org.example.hamburger.config.ChefConfig;
import org.example.hamburger.order.Order;
import org.example.hamburger.system.RestaurantService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 보조 요리사 클래스 - HeadChef로부터 간단한 요리를 할당받아 조리하는 역할
 * Chef를 상속받아 요리 기능을 가지며, HeadChef와 별도의 큐로 독립적으로 작업
 */
public class AssistantChef extends Chef implements Runnable, Workable{

    /**
     * AssistantChef 전용 주문 큐
     *
     * BlockingQueue<Order> 선택 이유:
     * 1. 스레드 안전성: 멀티스레드 환경에서 안전한 큐 연산
     * 2. 블로킹 연산: take() 호출 시 큐가 비어있으면 자동 대기
     * 3. 생산자-소비자 패턴: HeadChef(생산자) → AssistantChef(소비자)
     */
    private BlockingQueue<Order> assignedOrders;

    public AssistantChef(String name, int id, RestaurantService restaurant) {
        super(name, id, restaurant);
        // 예시를 위해 2가지만 설정
        this.specialties = ChefConfig.getSpecialties("AssistantChef");
        this.assignedOrders = new LinkedBlockingQueue<>();
    }

    // cashier와 같은 이유로 단순화
    @Override
    public void work() {
        System.out.println("[" + Thread.currentThread().getName() + "] 보조 요리사 준비: 조리 도구 정리, 재료 준비 완료");
    }

    /**
     * Runnable 인터페이스 구현 - 독립적인 스레드로 실행
     *
     * 동작 방식: "할당받은 주문 처리형"
     * 1. 자신의 큐(assignedOrders)에서 주문 대기
     * 2. HeadChef가 주문을 할당하면 즉시 처리
     * 3. HeadChef와 독립적으로 동시 작업 가능
     */
    @Override
    public void run() {
        work();

        while (working) { // Employee의 volatile boolean working 사용
            try {

                // take()는 큐가 비어있으면 자동으로 스레드를 대기 상태로 만듦
                // 주문이 들어오면 즉시 깨어나서 처리 시작
                Order order = assignedOrders.take();

                synchronized (System.out) {
                    System.out.println("[" + Thread.currentThread().getName() + "] HeadChef로부터 주문 #" + order.getOrderId() +
                            " (" + order.getMenuItem().getName() + ") 지시받음, 조리 시작");
                }

                // 실제 조리 시뮬레이션 시작
                cookOrder(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void receiveAssignment(Order order) {
        assignedOrders.offer(order);
    }
}
