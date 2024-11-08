package store.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTest {
    private Cart cart;
    private static final LocalDate ORDER_DATE = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        cart = new Cart(ORDER_DATE);
    }

    @DisplayName("장바구니에 상품을 담을 수 있다")
    @Test
    void addOrder() {
        Order order = createOrder();
        cart.addOrder(order);

        OrderCalculator calculator = cart.createCalculator();
        assertThat(calculator.getOrders()).hasSize(1);
    }

    @DisplayName("장바구니에 null 주문을 담으면 예외가 발생한다")
    @Test
    void validateNullOrder() {
        assertThatThrownBy(() -> cart.addOrder(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 주문이 비어있습니다");
    }

    @DisplayName("멤버십을 적용할 수 있다")
    @Test
    void applyMembership() {
        cart.applyMembership();
        Order order = createOrder();
        cart.addOrder(order);

        OrderCalculator calculator = cart.createCalculator();
        assertThat(calculator.isMembershipApplied()).isTrue();
    }

    @DisplayName("주문 계산기를 생성할 수 있다")
    @Test
    void createCalculator() {
        Order order = createOrder();
        cart.addOrder(order);

        OrderCalculator calculator = cart.createCalculator();
        assertThat(calculator.getOrderDate()).isEqualTo(ORDER_DATE);
        assertThat(calculator.getOrders()).hasSize(1);
        assertThat(calculator.isMembershipApplied()).isFalse();
    }

    private Order createOrder() {
        Product product = new Product("물", 1000, 10, 0, null);
        return new Order(product, 1);
    }
}
