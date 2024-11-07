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
        return promotion.getBuyQuantity() - purchaseQuantity;
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public boolean needsAdditionalItems() {
        return hasPromotion() && getAdditionalQuantityNeeded() > 0;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}