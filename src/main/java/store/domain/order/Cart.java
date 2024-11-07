package store.domain.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Order> orders;
    private final LocalDate orderDate;

    public Cart(LocalDate orderDate) {
        this.orders = new ArrayList<>();
        this.orderDate = orderDate;
    }

    public void addOrder(Order order) {
        validateOrder(order);
        orders.add(order);
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("[ERROR] 주문이 비어있습니다.");
        }
    }

    public OrderCalculator createCalculator(boolean hasMembership) {
        return new OrderCalculator(orders, orderDate, hasMembership);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}