package store.domain;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buyQuantity;
    private final int freeQuantity;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int buyQuantity, int freeQuantity,
                     LocalDate startDate, LocalDate endDate) {
        validateQuantities(buyQuantity, freeQuantity);
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validateQuantities(int buyQuantity, int freeQuantity) {
        if (buyQuantity <= 0 || freeQuantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 구매 수량과 증정 수량은 0보다 커야 합니다.");
        }
    }

    public boolean isValidPeriod(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean isApplicable(int quantity) {
        return quantity >= buyQuantity;
    }

    public int calculateFreeItems(int quantity) {
        return (quantity / buyQuantity) * freeQuantity;
    }

    public String getName() {
        return name;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }
}