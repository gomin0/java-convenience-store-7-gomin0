package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PromotionTest {

    @DisplayName("프로모션 기간내의 날짜인지 확인한다")
    @Test
    void isValidPeriod() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        Promotion promotion = new Promotion("탄산2+1", startDate, endDate);

        assertThat(promotion.isValidPeriod(LocalDate.of(2024, 6, 1))).isTrue();
        assertThat(promotion.isValidPeriod(LocalDate.of(2023, 12, 31))).isFalse();
        assertThat(promotion.isValidPeriod(LocalDate.of(2025, 1, 1))).isFalse();
    }

    @DisplayName("프로모션 시작일이 종료일보다 늦으면 예외가 발생한다")
    @Test
    void validateInvalidDate() {
        LocalDate startDate = LocalDate.of(2024, 12, 31);
        LocalDate endDate = LocalDate.of(2024, 1, 1);

        assertThatThrownBy(() -> new Promotion("탄산2+1", startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 프로모션 기간이 올바르지 않습니다");
    }

    @DisplayName("프로모션 날짜가 null이면 예외가 발생한다")
    @Test
    void validateNullDate() {
        assertThatThrownBy(() -> new Promotion("탄산2+1", null, LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 프로모션 기간이 올바르지 않습니다");

        assertThatThrownBy(() -> new Promotion("탄산2+1", LocalDate.now(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 프로모션 기간이 올바르지 않습니다");
    }

    @DisplayName("유효하지 않은 프로모션 타입이면 예외가 발생한다")
    @Test
    void validateInvalidType() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        assertThatThrownBy(() -> new Promotion(null, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 유효하지 않은 프로모션 타입입니다");

        assertThatThrownBy(() -> new Promotion("invalid", startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 유효하지 않은 프로모션 타입입니다");
    }

    @DisplayName("프로모션의 구매 수량과 증정 수량을 확인한다")
    @Test
    void getBuyAndFreeQuantity() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        Promotion promotion = new Promotion("탄산2+1", startDate, endDate);

        assertThat(promotion.getBuyQuantity()).isEqualTo(2);
        assertThat(promotion.getFreeQuantity()).isEqualTo(1);
    }
}