package store.domain.membership;

public class Membership {
    private static final double DISCOUNT_RATE = 0.3;
    private static final int MAX_DISCOUNT = 8000;

    private final boolean isActivated;

    private Membership(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public static Membership from(boolean isActivated) {
        return new Membership(isActivated);
    }

    public int calculateDiscount(int amount) {
        if (!isActivated) {
            return 0;
        }
        return Math.min((int)(amount * DISCOUNT_RATE), MAX_DISCOUNT);
    }

    public boolean isActivated() {
        return isActivated;
    }
}