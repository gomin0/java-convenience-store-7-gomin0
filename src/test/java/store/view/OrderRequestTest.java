package store.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderRequestTest {

    @DisplayName("주문 요청을 생성한다")
    @Test
    void createOrderRequest() {
        OrderRequest request = new OrderRequest("물", 1);

        assertThat(request.getProductName()).isEqualTo("물");
        assertThat(request.getQuantity()).isEqualTo(1);
    }

    @DisplayName("상품명이 비어있으면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void validateEmptyProductName(String productName) {
        assertThatThrownBy(() -> new OrderRequest(productName, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 상품명이 비어있습니다");
    }

    @DisplayName("상품명이 null이면 예외가 발생한다")
    @Test
    void validateNullProductName() {
        assertThatThrownBy(() -> new OrderRequest(null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 상품명이 비어있습니다");
    }

    @DisplayName("수량이 0 이하면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    void validateNegativeQuantity(int quantity) {
        assertThatThrownBy(() -> new OrderRequest("물", quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 수량은 0보다 커야 합니다");
    }
}