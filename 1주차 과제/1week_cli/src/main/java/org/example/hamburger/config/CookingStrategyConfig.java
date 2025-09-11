package org.example.hamburger.config;

import org.example.hamburger.strategy.CookingStrategy;
import org.example.hamburger.strategy.ComplexCookingStrategy;
import org.example.hamburger.strategy.SimpleCookingStrategy;
import java.util.Map;

/**
 * 조리 전략 설정 관리 클래스
 */
public class CookingStrategyConfig {

    // 싱글톤 방식을 쓴 이유는
    // 여기서는 구현체들이 모두 stateless한 단순한 구조이기 때문이다.
    // 단순한, 입력에 의존하는 구조라 여러번 중복을 줄이고 공유해도 문제 없을 것이라 판단.
    private static final CookingStrategy COMPLEX_STRATEGY = new ComplexCookingStrategy();
    private static final CookingStrategy SIMPLE_STRATEGY = new SimpleCookingStrategy();

    /**
     * 메뉴 이름별 조리 전략 매핑
     * 새로운 메뉴 추가 시 이 Map에만 추가하면 됨
     */
    private static final Map<String, CookingStrategy> MENU_STRATEGIES = Map.of(
            "Burger", COMPLEX_STRATEGY,
            "Pizza", COMPLEX_STRATEGY,
            "Fries", SIMPLE_STRATEGY,
            "Salad", SIMPLE_STRATEGY
            // 새 메뉴 추가 예시:
            // "BigBurger", new ComplexCookingStrategy(),
    );

    public static CookingStrategy getStrategy(String menuName) {
        return MENU_STRATEGIES.getOrDefault(menuName, SIMPLE_STRATEGY);
    }
}
