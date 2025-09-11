package org.example.hamburger.employee;

import org.example.hamburger.order.MenuItem;
import org.example.hamburger.order.Order;
import org.example.hamburger.system.RestaurantService;

/**
 * 계산원 클래스 - 고객의 주문을 접수하고 Order 객체를 생성하는 역할
 * Employee를 상속받아 독립적인 스레드로 동작하지만, 실제로는 이벤트 기반으로 작동
 */

public class Cashier extends Employee{

    private int transactionCount = 0;
    private RestaurantService restaurantService;

    public Cashier(String name, int id, RestaurantService restaurantService) {
        super(name, id);
        this.restaurantService = restaurantService;
    }

    // 사실 처음에 무언가 계속 고유의 업무를 하는걸로 생각했는데 구체적인 구현은 아직 못해봐서 껍데기만 남겨둠
    // 실제 시스템 설정이나 할인 정책 등. 우선은 그냥 간단한 텍스트로 대체.
    @Override
    public void work() {
        System.out.println("[" + Thread.currentThread().getName() + "] " + name + " 근무 시작");
        System.out.println("[" + Thread.currentThread().getName() + "] 계산대 시스템 점검 중...");
        System.out.println("[" + Thread.currentThread().getName() + "] 영수증 프린터 테스트 완료");
        System.out.println("[" + Thread.currentThread().getName() + "] 메뉴판 정리 완료");
        System.out.println("[" + Thread.currentThread().getName() + "] 주문 접수 준비 완료!");
    }

    /**
     * Runnable 인터페이스 구현 - 독립적인 스레드로 실행
     *
     * 특징: "이벤트 대기형" 스레드
     * - Chef들과 달리 능동적으로 작업하지 않음
     * - takeOrder() 호출을 기다리며 대기 상태 유지
     */
    @Override
    public void run() {
        while (working) { // Employee의 volatile boolean working 사용
            try {
                // 100ms마다 깨어나서 working 상태 확인
                // 왜 이렇게 설계했나?
                // 1. takeOrder()는 메인 스레드에서 직접 호출됨
                // 2. Cashier 스레드는 단순히 "살아있음"을 유지하는 역할
                // 3. 실제 업무는 이벤트 기반(takeOrder 호출)으로 처리
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // 프로그램 종료같은 스레드 인터럽트 발생 -> 사실 예외 처리까지 깊게 생각은 못해봤습니다.
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public Order takeOrder(MenuItem menuItem) {
        // 1. 주문 번호 증가
        transactionCount++;

        // 2. 주문 접수 메시지 출력
        System.out.println("[" + Thread.currentThread().getName() + "] " + menuItem.getName() + " 주문 접수됨 (" + menuItem.getPrice() + "원)");
        System.out.println("[" + Thread.currentThread().getName() + "] 주문 #" + transactionCount + " 접수 완료!");

        // 3. Order 객체 생성
        Order order = new Order(transactionCount, menuItem);

        // 4. Restaurant 주문 큐에 추가
        restaurantService.addOrder(order);

        return order;
    }
}
