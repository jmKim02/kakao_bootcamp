package org.example;

import org.example.hamburger.order.MenuItem;
import org.example.hamburger.system.RestaurantService;

import java.util.Scanner;

public class MainCLI {

    private static RestaurantService restaurantService;
    private static Scanner scanner;
    private static boolean waitingForMenuSelection = false;

    public static void main(String[] args) {
        restaurantService = new RestaurantService();
        scanner = new Scanner(System.in);

        restaurantService.start();

        // 시스템 초기화 대기 시간 벌어주는 역할
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        showHelp();
        System.out.print("customer> ");

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.print("customer> ");
                continue;
            }

            if (waitingForMenuSelection) {
                processMenuSelection(input);
            } else {
                processCommand(input);
            }

            System.out.print("customer> ");
        }
    }

    private static void processMenuSelection(String input) {
        try {
            int choice = Integer.parseInt(input);
            MenuItem selectedItem = restaurantService.getMenu().stream()
                    .filter(item -> item.getId() == choice)
                    .findFirst()
                    .orElse(null);

            if (selectedItem != null) {
                restaurantService.getCashier().takeOrder(selectedItem);
                waitingForMenuSelection = false;
            } else {
                System.out.println("[" + Thread.currentThread().getName() + "] 잘못된 메뉴 번호입니다. 1-4 사이로 입력해주세요.");
            }
        } catch (NumberFormatException e) {
            System.out.println("[" + Thread.currentThread().getName() + "] 숫자를 입력해주세요.");
        }
    }

    private static void processCommand(String command) {
        switch (command.toLowerCase()) {
            case "order":
                showMenu();
                break;
            case "status":
                restaurantService.showStatus();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
                System.out.println("[시스템] 프로그램을 종료합니다. 감사합니다!");
                restaurantService.shutdown();
                scanner.close();
                System.exit(0);
                break;
            default:
                System.out.println("'" + command + "' 명령어를 찾을 수 없습니다. 'help'를 입력해보세요.");
        }
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("[" + Thread.currentThread().getName() + "] 메뉴를 보여드리겠습니다");
        System.out.println("=================== 메뉴 ===================");

        for (MenuItem item : restaurantService.getMenu()) {
            System.out.println("  " + item.toString());
        }

        System.out.println("==========================================");
        System.out.println("[" + Thread.currentThread().getName() + "] 주문하실 번호를 입력해주세요 (1-4):");
        waitingForMenuSelection = true;
    }

    private static void showHelp() {
        System.out.println("사용 가능한 명령어:");
        System.out.println("  order  - 음식 주문하기");
        System.out.println("  status - 현재 상태 확인");
        System.out.println("  help   - 도움말 보기");
        System.out.println("  quit   - 프로그램 종료");
        System.out.println();
    }
}