package org.example.hamburger.config;

import org.example.hamburger.strategy.CookingStrategy;
import org.example.hamburger.strategy.ComplexCookingStrategy;
import org.example.hamburger.strategy.SimpleCookingStrategy;
import java.util.Map;

/**
 * 조리 전략 설정 관리 클래스
 */
public class CookingStrategyConfig {

    /**
     * 메뉴 이름별 조리 전략 매핑
     * 새로운 메뉴 추가 시 이 Map에만 추가하면 됨
     */
    private static final Map<String, CookingStrategy> MENU_STRATEGIES = Map.of(
            "Burger", new ComplexCookingStrategy(),
            "Pizza", new ComplexCookingStrategy(),
            "Fries", new SimpleCookingStrategy(),
            "Salad", new SimpleCookingStrategy()
            // 새 메뉴 추가 예시:
            // "BigBurger", new ComplexCookingStrategy(),
    );

    public static CookingStrategy getStrategy(String menuName) {
        return MENU_STRATEGIES.getOrDefault(menuName, new SimpleCookingStrategy());
    }
}
