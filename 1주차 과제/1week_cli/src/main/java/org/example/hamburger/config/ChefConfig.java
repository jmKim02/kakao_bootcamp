package org.example.hamburger.config;

import java.util.List;
import java.util.Map;

/**
 * Chef 전문분야 설정 관리 클래스
 */
public class ChefConfig {

    /**
     * Chef 타입별 전문 요리 목록
     * 새로운 Chef 타입이나 새로운 메뉴 추가 시 여기서 관리
     */
    private static final Map<String, List<String>> CHEF_SPECIALTIES = Map.of(
            "HeadChef", List.of("Burger", "Pizza", "Fries", "Salad"),
            "AssistantChef", List.of("Fries", "Salad")
            // 새 Chef 타입 추가 예시:
            // "PastryChef", List.of("Cake", "Cookies")
    );

    public static List<String> getSpecialties(String chefType) {
        return CHEF_SPECIALTIES.getOrDefault(chefType, List.of());
    }
}
