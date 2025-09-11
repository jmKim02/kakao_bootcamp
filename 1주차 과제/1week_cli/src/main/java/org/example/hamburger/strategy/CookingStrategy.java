package org.example.hamburger.strategy;

import org.example.hamburger.employee.Chef;
import org.example.hamburger.employee.HeadChef;
import org.example.hamburger.employee.AssistantChef;

/**
 * 조리 전략 인터페이스 - Strategy 패턴의 핵심
 * 각 메뉴가 어떤 Chef에게 할당될지 결정하는 전략을 정의
 * 향후 다른 Chef가 늘어나거나 해도 확장에 유리하다.
 */
public interface CookingStrategy {
    Chef getAssignedChef(HeadChef headChef, AssistantChef assistantChef);
    String getAssignmentMessage();
}