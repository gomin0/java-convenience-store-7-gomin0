package store.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionResultTest {

    @DisplayName("프로모션이 적용된 결과를 생성한다")
    @ParameterizedTest
    @CsvSource({
            "2, 1, 1000",  // 2+1 프로모션
            "4, 2, 2000",  // 2+1 프로모션 두 번 적용
            "6, 3, 3000"   // 2+1 프로모션 세 번 적용
    })
    void createPromotionResult(int payQuantity, int freeQuantity, int discountAmount) {
        PromotionResult result = new PromotionResult(payQuantity, freeQuantity, discountAmount);

        assertThat(result.getPayQuantity()).isEqualTo(payQuantity);
        assertThat(result.getFreeQuantity()).isEqualTo(freeQuantity);
        assertThat(result.getDiscountAmount()).isEqualTo(discountAmount);
    }

    @DisplayName("프로모션이 적용되지 않은 결과를 생성한다")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void createWithoutPromotion(int quantity) {
        PromotionResult result = PromotionResult.createWithoutPromotion(quantity);

        assertThat(result.getPayQuantity()).isEqualTo(quantity);
        assertThat(result.getFreeQuantity()).isZero();
        assertThat(result.getDiscountAmount()).isZero();
    }

    @DisplayName("프로모션 결과의 모든 필드를 확인할 수 있다")
    @Test
    void getterMethods() {
        PromotionResult result = new PromotionResult(2, 1, 1000);

        assertThat(result)
                .extracting("payQuantity", "freeQuantity", "discountAmount")
                .containsExactly(2, 1, 1000);
    }
}