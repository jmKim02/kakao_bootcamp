package org.example.hamburger.system;

import org.example.hamburger.config.MenuConfig;
import org.example.hamburger.employee.AssistantChef;
import org.example.hamburger.employee.Cashier;
import org.example.hamburger.employee.HeadChef;
import org.example.hamburger.order.MenuItem;
import org.example.hamburger.order.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 레스토랑 시스템의 핵심 관리 클래스
 * 모든 직원, 주문, 메뉴를 통합 관리하는 중앙 제어 시스템
 *
 * 설계 의도:
 * - 시스템의 모든 구성요소를 하나로 묶어서 관리
 * - 외부(CLI)에서 레스토랑과 상호작용할 수 있는 단일 인터페이스 제공
 * - 스레드 생명주기 관리 (시작/종료)
 */
public class RestaurantService {

    // 큐 선택 이유 같은 경우는 앞선 엔터티들에 설명되어 있는 부분과 비슷한 이유들입니다.
    private BlockingQueue<Order> orderQueue;
    private List<MenuItem> menu;
    private List<Order> completedOrders;
    private HeadChef headChef;
    private AssistantChef assistantChef;
    private Cashier cashier;
    private Thread headChefThread;
    private Thread assistantChefThread;
    private Thread cashierThread;

    public RestaurantService() {
        // 1. 공유자원 초기화
        this.orderQueue = new LinkedBlockingQueue<>();
        this.completedOrders = Collections.synchronizedList(new ArrayList<>());

        // 2. 메뉴 로드
        this.menu = MenuConfig.getDefaultMenu();

        // 3. 직원 생성
        this.headChef = new HeadChef("Kim", 1, this);
        this.assistantChef = new AssistantChef("Lee", 2, this);
        this.cashier = new Cashier("Park", 3, this);

        // 4. 직원 간 관계 설정 (의존성 주입)
        this.headChef.setAssistantChef(assistantChef);
    }

    /**
     * 시스템 시작 메서드
     * 모든 직원 스레드를 시작하고 사용자에게 준비 완료 알림
     *
     * Thread 생성과 시작을 분리한 이유:
     * - 생성자에서 스레드 시작하면 객체 초기화 완료 전에 스레드 실행 위험
     * - 명시적 start() 호출로 시작 시점 제어
     */
    public void start() {
        System.out.println("=== 햄버거 주문 시뮬레이션 ===");
        System.out.println("[시스템] 햄버거 주문 시뮬레이션 시작.");

        // 스레드 생성 시 명시적 이름 지정
        headChefThread = new Thread(headChef, "HeadChef-Thread");
        assistantChefThread = new Thread(assistantChef, "AssistantChef-Thread");
        cashierThread = new Thread(cashier, "Cashier-Thread");

        headChefThread.start();
        assistantChefThread.start();
        cashierThread.start();

        System.out.println("[시스템] 시스템 준비 완료!");
        System.out.println("명령어: order, status, receipt, help, quit");
    }

    /**
     * 시스템 현황 출력 메서드
     * 사용자가 현재 상태를 한눈에 볼 수 있도록 정보 정리해서 표시
     *
     * 정보 구성:
     * - 대기 중인 주문 수
     * - 각 직원의 상태 (바쁨/대기)
     * - 완성된 주문 목록과 매출
     */
    public void showStatus() {
        System.out.println();
        System.out.println("============== 시스템 현황 ==============");
        System.out.println("OrderQueue 대기 중: " + orderQueue.size() + "개");

        System.out.println("HeadChef: " + (headChef.isBusy() ? "조리 중" : "대기 중"));
        System.out.println("AssistantChef: " + (assistantChef.isBusy() ? "조리 중" : "대기 중"));
        System.out.println("완성된 주문: " + completedOrders.size() + "개");

        if (!orderQueue.isEmpty()) {
            System.out.println("--- 대기 중인 주문 ---");
            orderQueue.forEach(order -> {
                System.out.println("  " + order.toString() + " (HeadChef 검토 대기)");
            });
        }

        System.out.println("=======================================");
        System.out.println();
    }

    public void addOrder(Order order) {
        orderQueue.offer(order);
        cashier.recordSale(order);
    }

    /**
     * 다음 주문 가져오기 (HeadChef가 호출)
     * take() 사용 이유: 블로킹 대기로 새 주문까지 자동 대기
     */
    public Order getNextOrder() throws InterruptedException {
        return orderQueue.take();
    }

    /**
     * 완성된 주문 추가 (Chef들이 호출)
     * synchronized List이므로 add() 호출만으로 스레드 안전
     */
    public void addCompleteOrder(Order order) {
        completedOrders.add(order);
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public Cashier getCashier() {
        return cashier;
    }

    /**
     * 시스템 종료 메서드
     * 1. 모든 직원의 working 플래그를 false로 설정
     * 2. 각 스레드에 인터럽트 신호 전송
     *
     * 이중 안전장치:
     * - stop(): volatile working = false
     * - interrupt(): 블로킹 상태에서도 강제로 깨운다.
     */
    public void shutdown() {
        headChef.stop();
        assistantChef.stop();
        cashier.stop();
        if (headChefThread != null) headChefThread.interrupt();
        if (assistantChefThread != null) assistantChefThread.interrupt();
        if (cashierThread != null) cashierThread.interrupt();
    }
}
