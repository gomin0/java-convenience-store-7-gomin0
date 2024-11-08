package store.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.exception.StockException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @DisplayName("재고가 충분한지 확인한다")
    @Test
    void hasEnoughStock() {
        Stock stock = new Stock(5, 5);

        assertThat(stock.hasEnoughStock(3)).isTrue();
        assertThat(stock.hasEnoughStock(11)).isFalse();
    }

    @DisplayName("프로모션 재고가 있는지 확인한다")
    @Test
    void hasPromotionStock() {
        Stock stock = new Stock(5, 0);
        assertThat(stock.hasPromotionStock()).isFalse();

        Stock stockWithPromotion = new Stock(5, 5);
        assertThat(stockWithPromotion.hasPromotionStock()).isTrue();
    }

    @DisplayName("프로모션 재고를 차감한다")
    @Test
    void reducePromotionStock() {
        Stock stock = new Stock(5, 5);
        stock.reducePromotionStock(3);

        assertThat(stock.getTotalStock()).isEqualTo(7);
    }

    @DisplayName("일반 재고를 차감한다")
    @Test
    void reduceNormalStock() {
        Stock stock = new Stock(5, 5);
        stock.reduceNormalStock(3);

        assertThat(stock.getTotalStock()).isEqualTo(7);
    }

    @DisplayName("재고보다 많은 수량을 차감하려고 하면 예외가 발생한다")
    @Test
    void validateReduceQuantity() {
        Stock stock = new Stock(5, 5);

        assertThatThrownBy(() -> stock.reduceNormalStock(6))
                .isInstanceOf(StockException.class)
                .hasMessageContaining("재고 수량을 초과하여 구매할 수 없습니다.");
    }

    @DisplayName("음수 재고로 생성하려고 하면 예외가 발생한다")
    @Test
    void validateNegativeStock() {
        assertThatThrownBy(() -> new Stock(-1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("재고는 0보다 작을 수 없습니다");
    }
}