package org.example.hamburger.config;

import org.example.hamburger.order.MenuItem;

import java.util.List;

// 다른 Config와 마찬가지로 db가 없어서 예시 하드코딩 부분만 뽑았습니다.
public class MenuConfig {
    private static final List<MenuItem> DEFAULT_MENU = List.of(
            new MenuItem(1, "Burger", 8500, 3),
            new MenuItem(2, "Pizza", 12000, 5),
            new MenuItem(3, "Fries", 3500, 2),
            new MenuItem(4, "Salad", 6500, 2)
    );

    public static List<MenuItem> getDefaultMenu() {
        return DEFAULT_MENU;
    }
}