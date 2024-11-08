package store.domain.promotion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionPolicyTest {
    private static final LocalDate VALID_DATE = LocalDate.of(2024, 6, 1);
    private static final LocalDate INVALID_DATE = LocalDate.of(2023, 12, 31);
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        promotion = new Promotion(
                "탄산2+1",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );
    }

    @DisplayName("프로모션이 적용 가능한지 확인한다")
    @Test
    void isApplicable() {
        PromotionPolicy policy = new PromotionPolicy(promotion, 2, VALID_DATE);
        assertThat(policy.isApplicable()).isTrue();

        PromotionPolicy invalidQuantity = new PromotionPolicy(promotion, 1, VALID_DATE);
        assertThat(invalidQuantity.isApplicable()).isFalse();

        PromotionPolicy invalidDate = new PromotionPolicy(promotion, 2, INVALID_DATE);
        assertThat(invalidDate.isApplicable()).isFalse();

        PromotionPolicy noPromotion = new PromotionPolicy(null, 2, VALID_DATE);
        assertThat(noPromotion.isApplicable()).isFalse();
    }

    @DisplayName("추가로 필요한 구매 수량을 계산한다")
    @Test
    void getAdditionalQuantityNeeded() {
        PromotionPolicy needMore = new PromotionPolicy(promotion, 1, VALID_DATE);
        assertThat(needMore.getAdditionalQuantityNeeded()).isEqualTo(1);

        PromotionPolicy enough = new PromotionPolicy(promotion, 2, VALID_DATE);
        assertThat(enough.getAdditionalQuantityNeeded()).isZero();

        PromotionPolicy moreEnough = new PromotionPolicy(promotion, 3, VALID_DATE);
        assertThat(moreEnough.getAdditionalQuantityNeeded()).isZero();

        PromotionPolicy noPromotion = new PromotionPolicy(null, 1, VALID_DATE);
        assertThat(noPromotion.getAdditionalQuantityNeeded()).isZero();
    }

    @DisplayName("프로모션 적용 결과를 계산한다")
    @Test
    void calculatePromotionResult() {
        PromotionPolicy notApplicable = new PromotionPolicy(promotion, 1, VALID_DATE);
        PromotionResult notAppliedResult = notApplicable.calculatePromotionResult(1000);
        assertThat(notAppliedResult.getPayQuantity()).isEqualTo(1);
        assertThat(notAppliedResult.getFreeQuantity()).isZero();
        assertThat(notAppliedResult.getDiscountAmount()).isZero();

        PromotionPolicy applicable = new PromotionPolicy(promotion, 2, VALID_DATE);
        PromotionResult appliedResult = applicable.calculatePromotionResult(1000);
        assertThat(appliedResult.getPayQuantity()).isEqualTo(2);
        assertThat(appliedResult.getFreeQuantity()).isEqualTo(1);
        assertThat(appliedResult.getDiscountAmount()).isEqualTo(1000);
    }
}