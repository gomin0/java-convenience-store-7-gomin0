package store.domain.order;

import store.domain.membership.Membership;
import store.domain.promotion.PromotionPolicy;
import store.domain.promotion.PromotionResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderCalculator {
    private final List<Order> orders;
    private final LocalDate orderDate;
    private final Membership membership;
    private final List<OrderResult> results;

    public OrderCalculator(List<Order> orders, LocalDate orderDate, boolean hasMembership) {
        this.orders = new ArrayList<>(orders);
        this.orderDate = orderDate;
        this.membership = Membership.from(hasMembership);
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
        order.getProduct().reducePurchaseStock(order.getQuantity());
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
        int originalPrice = order.getProduct().getPrice() * promotionResult.getPayQuantity();
        int membershipDiscount = membership.calculateDiscount(originalPrice);

        return new OrderResult(
                order.getProduct(),
                promotionResult.getPayQuantity(),
                promotionResult.getFreeQuantity(),
                promotionResult.getDiscountAmount(),
                membershipDiscount
        );
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public boolean isMembershipApplied() {
        return membership.isActive();
    }
}