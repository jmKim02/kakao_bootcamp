package org.example.hamburger.strategy;

import org.example.hamburger.employee.Chef;
import org.example.hamburger.employee.HeadChef;
import org.example.hamburger.employee.AssistantChef;

/**
 * 복잡한 요리 전략 - Burger, Pizza 같은 복잡한 요리용
 * HeadChef가 직접 조리해야 하는 메뉴들의 전략
 */
public class ComplexCookingStrategy implements CookingStrategy {

    @Override
    public Chef getAssignedChef(HeadChef headChef, AssistantChef assistantChef) {
        return headChef;
    }

    @Override
    public String getAssignmentMessage() {
        return "복잡한 요리로 판단, HeadChef가 직접 조리";
    }
}