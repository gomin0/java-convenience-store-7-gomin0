package store.domain.promotion;

public enum PromotionType {
    TWO_PLUS_ONE("탄산2+1", 2, 1),
    ONE_PLUS_ONE_MD("MD추천상품", 1, 1),
    ONE_PLUS_ONE_FLASH("반짝할인", 1, 1);

    private final String displayName;
    private final int buyQuantity;
    private final int freeQuantity;

    PromotionType(String displayName, int buyQuantity, int freeQuantity) {
        this.displayName = displayName;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
    }

    public static PromotionType fromName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        for (PromotionType type : values()) {
            if (type.displayName.equals(name)) {
                return type;
            }
        }
        return null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }
}