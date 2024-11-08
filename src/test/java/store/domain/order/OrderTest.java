package store.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.product.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문을 생성한다")
    @Test
    void createOrder() {
        Product product = createProduct("물", 1000, 10);
        Order order = new Order(product, 5);

        assertThat(order.getProduct()).isEqualTo(product);
        assertThat(order.getQuantity()).isEqualTo(5);
    }

    @DisplayName("상품이 null이면 예외가 발생한다")
    @Test
    void validateNullProduct() {
        assertThatThrownBy(() -> new Order(null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 상품이 선택되지 않았습니다");
    }

    @DisplayName("구매 수량이 0 이하면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    void validateNegativeQuantity(int quantity) {
        Product product = createProduct("물", 1000, 10);

        assertThatThrownBy(() -> new Order(product, quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 구매 수량은 0보다 커야 합니다");
    }

    @DisplayName("재고보다 많은 수량을 주문하면 예외가 발생한다")
    @Test
    void validateExceedingStock() {
        Product product = createProduct("물", 1000, 10);

        assertThatThrownBy(() -> new Order(product, 11))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다");
    }

    private Product createProduct(String name, int price, int stock) {
        return new Product(name, price, stock, 0, null);
    }
}