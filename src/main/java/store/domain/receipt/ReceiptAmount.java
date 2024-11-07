package store.domain.receipt;

import store.domain.order.OrderResult;

import java.util.List;

public class ReceiptAmount {
    private final int totalAmount;
    private final int promotionDiscount;
    private final int membershipDiscount;

    public ReceiptAmount(List<OrderResult> orderResults) {
        this.totalAmount = calculateTotalAmount(orderResults);
        this.promotionDiscount = calculatePromotionDiscount(orderResults);
        this.membershipDiscount = calculateMembershipDiscount(orderResults);
    }

    private int calculateTotalAmount(List<OrderResult> orderResults) {
        return orderResults.stream()
                .mapToInt(this::calculateOrderAmount)
                .sum();
    }

    private int calculateOrderAmount(OrderResult result) {
        return result.getProduct().getPrice() * result.getPayQuantity();
    }

    private int calculatePromotionDiscount(List<OrderResult> orderResults) {
        return orderResults.stream()
                .mapToInt(OrderResult::getPromotionDiscount)
                .sum();
    }

    private int calculateMembershipDiscount(List<OrderResult> orderResults) {
        return orderResults.stream()
                .mapToInt(OrderResult::getMembershipDiscount)
                .sum();
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalAmount() {
        return totalAmount - promotionDiscount - membershipDiscount;
    }
}