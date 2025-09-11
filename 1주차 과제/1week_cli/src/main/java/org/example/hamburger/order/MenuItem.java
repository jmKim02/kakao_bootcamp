package org.example.hamburger.order;

import org.example.hamburger.config.CookingStrategyConfig;
import org.example.hamburger.strategy.CookingStrategy;

// 메뉴 항목 클래스 - 각 메뉴를 나타내는 데이터 클래스
public class MenuItem {
    
    private int id;
    private String name;
    private int price;
    private int cookTimeMinutes;
    private CookingStrategy strategy;
    
    public MenuItem(int id, String name, int price, int cookTimeMinutes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cookTimeMinutes = cookTimeMinutes;
        this.strategy = determineStrategy();
    }

    private CookingStrategy determineStrategy() {
        return CookingStrategyConfig.getStrategy(name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCookTime() {
        return cookTimeMinutes;
    }

    public CookingStrategy getStrategy() {
        return strategy;
    }

    @Override
    public String toString() {
        return id + ". " + name + " - " + price + "원 (조리시간: " + cookTimeMinutes + "분)";
    }

}
