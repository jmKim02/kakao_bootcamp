package org.example.hamburger.strategy;

import org.example.hamburger.employee.Chef;
import org.example.hamburger.employee.HeadChef;
import org.example.hamburger.employee.AssistantChef;

/**
 * 쉬운 요리 전략 - Fries, Salad 같은 간단한 요리용
 * AssistantChef에게 위임 가능한 메뉴들의 전략
 */
public class SimpleCookingStrategy implements CookingStrategy {

    @Override
    public Chef getAssignedChef(HeadChef headChef, AssistantChef assistantChef) {
        return assistantChef;
    }

    @Override
    public String getAssignmentMessage() {
        return "쉬운 요리로 판단, AssistantChef에게 지시";
    }
}