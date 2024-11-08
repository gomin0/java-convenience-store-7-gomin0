package store.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderCalculatorTest {
    private static final LocalDate ORDER_DATE = LocalDate.of(2024, 1, 1);
    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart(ORDER_DATE);
    }

    @DisplayName("프로모션이 없는 상품의 주문을 계산한다")
    @Test
    void calculateOrderWithoutPromotion() {
        Product product = createProduct("물", 1000, 10, 0, null);
        cart.addOrder(new Order(product, 2));

        List<OrderResult> results = cart.createCalculator().calculate();
        OrderResult result = results.get(0);

        assertThat(result.getProduct()).isEqualTo(product);
        assertThat(result.getPayQuantity()).isEqualTo(2);
        assertThat(result.getFreeQuantity()).isZero();
        assertThat(result.getPromotionDiscount()).isZero();
        assertThat(result.getMembershipDiscount()).isZero();
    }

    @DisplayName("프로모션이 있는 상품의 주문을 계산한다")
    @Test
    void calculateOrderWithPromotion() {
        Promotion promotion = createPromotion("탄산2+1");
        Product product = createProduct("콜라", 1000, 0, 10, promotion);
        cart.addOrder(new Order(product, 2));

        List<OrderResult> results = cart.createCalculator().calculate();
        OrderResult result = results.get(0);

        assertThat(result.getProduct()).isEqualTo(product);
        assertThat(result.getPayQuantity()).isEqualTo(2);
        assertThat(result.getFreeQuantity()).isEqualTo(1);
        assertThat(result.getPromotionDiscount()).isEqualTo(1000);
        assertThat(result.getMembershipDiscount()).isZero();
    }

    @DisplayName("멤버십이 적용된 주문을 계산한다")
    @Test
    void calculateOrderWithMembership() {
        Product product = createProduct("물", 10000, 10, 0, null);
        cart.addOrder(new Order(product, 1));
        cart.applyMembership();

        List<OrderResult> results = cart.createCalculator().calculate();
        OrderResult result = results.get(0);

        assertThat(result.getMembershipDiscount()).isEqualTo(3000);
    }

    @DisplayName("여러 상품의 주문을 계산한다")
    @Test
    void calculateMultipleOrders() {
        Promotion promotion = createPromotion("탄산2+1");
        Product cola = createProduct("콜라", 1000, 0, 10, promotion);
        Product water = createProduct("물", 500, 10, 0, null);

        cart.addOrder(new Order(cola, 2));
        cart.addOrder(new Order(water, 2));
        cart.applyMembership();

        List<OrderResult> results = cart.createCalculator().calculate();

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getPromotionDiscount()).isEqualTo(1000);
        assertThat(results.get(1).getMembershipDiscount()).isEqualTo(300);
    }

    private Product createProduct(String name, int price, int normalStock, int promotionStock, Promotion promotion) {
        return new Product(name, price, normalStock, promotionStock, promotion);
    }

    private Promotion createPromotion(String name) {
        return new Promotion(
                name,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );
    }
}