package org.example.hamburger.employee;

import org.example.hamburger.order.MenuItem;
import org.example.hamburger.order.Order;
import org.example.hamburger.system.RestaurantService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 계산원 클래스 - 고객의 주문을 접수하고 Order 객체를 생성하는 역할
 * Employee를 상속받아 독립적인 스레드로 동작하지만, 실제로는 이벤트 기반으로 작동
 */

public class Cashier extends Employee implements Runnable, Workable{

    private int transactionCount = 0;
    private RestaurantService restaurantService;
    private List<Order> salesHistory = Collections.synchronizedList(new ArrayList<>());
    private BlockingQueue<String> taskQueue = new LinkedBlockingQueue<>();

    public Cashier(String name, int id, RestaurantService restaurantService) {
        super(name, id);
        this.restaurantService = restaurantService;
    }

    // 사실 처음에 무언가 계속 고유의 업무를 하는걸로 생각했는데 이게 주기적으로 지금 배운 것들로 스케줄러를
    // 구현하기는 힘들어서 초기 셋팅 각자의 업무를 진행하는 방향으로 했습니다.
    @Override
    public void work() {
        System.out.println("[" + Thread.currentThread().getName() + "] 계산원 준비: 계산대 점검, 결제 시스템 확인 완료");
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
        work();

        while (working) { // Employee의 volatile boolean working 사용
            try {
                taskQueue.take();
                showSalesReport();
            } catch (InterruptedException e) {
                // 프로그램 종료같은 스레드 인터럽트 발생 -> 사실 예외 처리까지 깊게 생각은 못해봤습니다.
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // 매출 전표는 캐셔 쓰레드에서 계산. 메인은 계속 입력받으니까.
    private void showSalesReport() {
        synchronized (System.out) {
            System.out.println();
            System.out.println("=== 매출 전표 ===");

            if (salesHistory.isEmpty()) {
                System.out.println("완성된 주문이 없습니다.");
            } else {
                int totalSales = 0;
                for (Order order : salesHistory) {
                    System.out.println("  " + order.toString() + " ✓");
                    totalSales += order.getMenuItem().getPrice();
                }
                System.out.println("총 주문: " + salesHistory.size() + "건");
                System.out.println("총 매출: " + String.format("%,d", totalSales) + "원");
            }
            System.out.println("================");
            System.out.print("customer> ");
        }
    }

    public void recordSale(Order order) {
        salesHistory.add(order);
    }

    // 메인 스레드에서 호출: 우선 하나의 경우만 한거라 문자열은 크게 상관없음.
    public void requestSalesReport() {
        taskQueue.offer("request");
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
