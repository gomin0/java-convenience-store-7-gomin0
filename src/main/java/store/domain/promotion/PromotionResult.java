package store.domain.promotion;

public class PromotionResult {
    private final int payQuantity;
    private final int freeQuantity;
    private final int discountAmount;

    public PromotionResult(int payQuantity, int freeQuantity, int discountAmount) {
        this.payQuantity = payQuantity;
        this.freeQuantity = freeQuantity;
        this.discountAmount = discountAmount;
    }

    public static PromotionResult createWithoutPromotion(int quantity) {
        return new PromotionResult(quantity, 0, 0);
    }

    public int getPayQuantity() {
        return payQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }
}