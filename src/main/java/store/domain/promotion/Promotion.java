package store.domain.promotion;

import java.time.LocalDate;


public class Promotion {
    private final PromotionType type;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String typeName, LocalDate startDate, LocalDate endDate) {
        this.type = validateAndGetType(typeName);
        validateDates(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private PromotionType validateAndGetType(String typeName) {
        PromotionType type = PromotionType.fromName(typeName);
        if (type == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 프로모션 타입입니다.");
        }
        return type;
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("[ERROR] 프로모션 기간이 올바르지 않습니다.");
        }
    }

    public boolean isValidPeriod(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public String getName() {
        return type.getDisplayName();
    }

    public int getBuyQuantity() {
        return type.getBuyQuantity();
    }

    public int getFreeQuantity() {
        return type.getFreeQuantity();
    }
}