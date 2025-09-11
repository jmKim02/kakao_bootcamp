package org.example.hamburger.order;

// 주문 클래스 - 고객의 개별 주문을 나타내는 데이터 클래스
public class Order {

    private int orderId;
    private MenuItem menuItem;
    private OrderStatus status;

    public Order(int orderId, MenuItem menuItem) {
        this.orderId = orderId;
        this.menuItem = menuItem;
        this.status = OrderStatus.ORDERED;
    }

    public int getOrderId() {
        return orderId;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "주문 #" + orderId + ": " + menuItem.getName() + " (" + status + ")";
    }
}
