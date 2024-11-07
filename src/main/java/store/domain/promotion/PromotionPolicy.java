package store.domain.promotion;


import java.time.LocalDate;

public class PromotionPolicy {
    private final Promotion promotion;
    private final int purchaseQuantity;
    private final LocalDate currentDate;

    public PromotionPolicy(Promotion promotion, int purchaseQuantity, LocalDate currentDate) {
        this.promotion = promotion;
        this.purchaseQuantity = purchaseQuantity;
        this.currentDate = currentDate;
    }

    public boolean isApplicable() {
        return promotion != null &&
                promotion.isValidPeriod(currentDate) &&
                isQuantityQualified();
    }

    private boolean isQuantityQualified() {
        return purchaseQuantity >= promotion.getBuyQuantity();
    }

    public int getAdditionalQuantityNeeded() {
        if (!hasPromotion()) {
            return 0;
        }
        return Math.max(0, promotion.getBuyQuantity() - purchaseQuantity);
    }

    public PromotionResult calculatePromotionResult(int price) {
        if (!isApplicable()) {
            return PromotionResult.createWithoutPromotion(purchaseQuantity);
        }
        return calculatePromotionDiscount(price);
    }

    private PromotionResult calculatePromotionDiscount(int price) {
        int sets = purchaseQuantity / (promotion.getBuyQuantity() + promotion.getFreeQuantity());
        int freeItems = sets * promotion.getFreeQuantity();
        int payingItems = purchaseQuantity - freeItems;

        return new PromotionResult(payingItems, freeItems, price * freeItems);
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}