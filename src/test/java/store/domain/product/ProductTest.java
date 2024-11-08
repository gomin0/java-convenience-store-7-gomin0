package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.exception.StockException;
import store.domain.promotion.Promotion;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    private static final LocalDate VALID_DATE = LocalDate.of(2024, 1, 1);

    @Nested
    @DisplayName("상품 생성")
    class CreateProduct {

        @DisplayName("정상적인 상품을 생성한다")
        @Test
        void createValidProduct() {
            Product product = new Product("물", 1000, 10, 0, null);

            assertThat(product.getName()).isEqualTo("물");
            assertThat(product.getPrice()).isEqualTo(1000);
            assertThat(product.hasPromotion()).isFalse();
        }

        @DisplayName("상품명이 null이거나 비어있으면 예외가 발생한다")
        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  "})
        void validateEmptyName(String name) {
            assertThatThrownBy(() -> new Product(name, 1000, 10, 0, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 상품명은 비어있을 수 없습니다");

            assertThatThrownBy(() -> new Product(null, 1000, 10, 0, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 상품명은 비어있을 수 없습니다");
        }

        @DisplayName("가격이 0 미만이면 예외가 발생한다")
        @Test
        void validateNegativePrice() {
            assertThatThrownBy(() -> new Product("물", -1000, 10, 0, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 가격은 0 이상이어야 합니다");
        }
    }

    @Nested
    @DisplayName("재고 관리")
    class StockManagement {

        @DisplayName("구매 수량이 재고보다 많으면 예외가 발생한다")
        @Test
        void validateExceedingPurchase() {
            Product product = new Product("물", 1000, 10, 0, null);

            assertThatThrownBy(() -> product.validatePurchase(11))
                    .isInstanceOf(StockException.class)
                    .hasMessageContaining("재고 수량을 초과하여 구매할 수 없습니다");
        }

        @DisplayName("프로모션 재고가 있으면 프로모션 재고가 차감된다")
        @Test
        void reducePromotionStock() {
            Product product = new Product("물", 1000, 10, 5, createPromotion());
            product.reducePurchaseStock(3);

            assertThat(product.formatProductInfo()).contains("12개"); // 일반 10개 + 남은 프로모션 2개
        }

        @DisplayName("프로모션 재고가 없으면 일반 재고가 차감된다")
        @Test
        void reduceNormalStock() {
            Product product = new Product("물", 1000, 10, 0, null);
            product.reducePurchaseStock(3);

            assertThat(product.formatProductInfo()).contains("7개");
        }
    }

    @Nested
    @DisplayName("프로모션 적용")
    class PromotionApplication {

        @DisplayName("프로모션 적용 가능 여부를 확인한다")
        @Test
        void checkPromotionApplicable() {
            Product product = new Product("콜라", 1000, 10, 5, createPromotion());

            assertThat(product.canApplyPromotion(2, VALID_DATE)).isTrue();
            assertThat(product.canApplyPromotion(1, VALID_DATE)).isFalse(); // 수량 부족
            assertThat(product.canApplyPromotion(2, LocalDate.of(2023, 1, 1))).isFalse(); // 날짜 무효
        }
    }

    @Nested
    @DisplayName("상품 정보 출력")
    class ProductInfo {

        @DisplayName("재고가 있는 상품 정보를 출력한다")
        @Test
        void formatInStockProduct() {
            Product productWithPromotion = new Product("콜라", 1000, 10, 5, createPromotion());
            Product productWithoutPromotion = new Product("물", 1000, 10, 0, null);

            assertThat(productWithPromotion.formatProductInfo()).isEqualTo("- 콜라 1,000원 15개 탄산2+1");
            assertThat(productWithoutPromotion.formatProductInfo()).isEqualTo("- 물 1,000원 10개");
        }

        @DisplayName("재고가 없는 상품 정보를 출력한다")
        @Test
        void formatOutOfStockProduct() {
            Product product = new Product("물", 1000, 0, 0, null);

            assertThat(product.formatProductInfo()).isEqualTo("- 물 1,000원 재고 없음");
        }
    }

    private Promotion createPromotion() {
        return new Promotion("탄산2+1",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));
    }
}