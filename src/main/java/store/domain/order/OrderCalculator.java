package store.domain.order;

import store.domain.promotion.PromotionPolicy;
import store.domain.promotion.PromotionResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderCalculator {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    private final List<Order> orders;
    private final LocalDate orderDate;
    private final boolean hasMembership;
    private final List<OrderResult> results;

    public OrderCalculator(List<Order> orders, LocalDate orderDate, boolean hasMembership) {
        this.orders = new ArrayList<>(orders);
        this.orderDate = orderDate;
        this.hasMembership = hasMembership;
        this.results = new ArrayList<>();
    }

    public List<OrderResult> calculate() {
        processOrders();
        return new ArrayList<>(results);
    }

    private void processOrders() {
        orders.forEach(this::processOrder);
    }

    private void processOrder(Order order) {
        PromotionResult promotionResult = calculatePromotion(order);
        OrderResult result = createOrderResult(order, promotionResult);
        results.add(result);
    }

    private PromotionResult calculatePromotion(Order order) {
        PromotionPolicy policy = new PromotionPolicy(
                order.getProduct().getPromotion(),
                order.getQuantity(),
                orderDate
        );
        return policy.calculatePromotionResult(order.getProduct().getPrice());
    }

    private OrderResult createOrderResult(Order order, PromotionResult promotionResult) {
        int membershipDiscount = calculateMembershipDiscount(
                order.getProduct().getPrice() * promotionResult.getPayQuantity()
        );

        return new OrderResult(
                order.getProduct(),
                promotionResult.getPayQuantity(),
                promotionResult.getFreeQuantity(),
                promotionResult.getDiscountAmount(),
                membershipDiscount
        );
    }

    private int calculateMembershipDiscount(int amount) {
        if (!hasMembership) {
            return 0;
        }
        return Math.min(
                (int)(amount * MEMBERSHIP_DISCOUNT_RATE),
                MAX_MEMBERSHIP_DISCOUNT
        );
    }
}