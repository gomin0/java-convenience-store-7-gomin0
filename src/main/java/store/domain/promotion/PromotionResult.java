package store.domain.promotion;

public class PromotionResult {
    private final int payQuantity;
    private final int freeQuantity;
    private final int discountAmount;

    private PromotionResult(int payQuantity, int freeQuantity, int price) {
        this.payQuantity = payQuantity;
        this.freeQuantity = freeQuantity;
        this.discountAmount = calculateDiscountAmount(price);
    }

    private int calculateDiscountAmount(int price) {
        return freeQuantity * price;
    }

    public static PromotionResult createWithPromotion(int quantity, int buyQuantity,
                                                      int freeQuantity, int price) {
        int sets = quantity / (buyQuantity + freeQuantity);
        int payQuantity = quantity - (sets * freeQuantity);
        return new PromotionResult(payQuantity, sets * freeQuantity, price);
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