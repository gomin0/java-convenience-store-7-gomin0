package store.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;

import static org.assertj.core.api.Assertions.assertThat;

class OrderResultTest {

    @DisplayName("할인이 없는 주문의 총 금액을 계산한다")
    @Test
    void calculateTotalPriceWithoutDiscount() {
        Product product = createProduct("물", 1000);
        OrderResult result = new OrderResult(product, 2, 0, 0, 0);

        assertThat(result.calculateTotalPrice()).isEqualTo(2000);
    }

    @DisplayName("프로모션 할인이 적용된 주문의 총 금액을 계산한다")
    @Test
    void calculateTotalPriceWithPromotionDiscount() {
        Product product = createProduct("콜라", 1000);
        OrderResult result = new OrderResult(product, 2, 1, 1000, 0);

        assertThat(result.calculateTotalPrice()).isEqualTo(1000);
    }

    @DisplayName("멤버십 할인이 적용된 주문의 총 금액을 계산한다")
    @Test
    void calculateTotalPriceWithMembershipDiscount() {
        Product product = createProduct("물", 10000);
        OrderResult result = new OrderResult(product, 1, 0, 0, 3000);

        assertThat(result.calculateTotalPrice()).isEqualTo(7000);
    }

    @DisplayName("프로모션과 멤버십 할인이 모두 적용된 주문의 총 금액을 계산한다")
    @Test
    void calculateTotalPriceWithAllDiscounts() {
        Product product = createProduct("콜라", 1000);
        OrderResult result = new OrderResult(product, 2, 1, 1000, 300);

        assertThat(result.calculateTotalPrice()).isEqualTo(700);
    }

    @DisplayName("주문 결과의 getter 메서드들이 정상 동작한다")
    @Test
    void getterMethods() {
        Product product = createProduct("콜라", 1000);
        OrderResult result = new OrderResult(product, 2, 1, 1000, 300);

        assertThat(result.getProduct()).isEqualTo(product);
        assertThat(result.getPayQuantity()).isEqualTo(2);
        assertThat(result.getFreeQuantity()).isEqualTo(1);
        assertThat(result.getPromotionDiscount()).isEqualTo(1000);
        assertThat(result.getMembershipDiscount()).isEqualTo(300);
    }

    private Product createProduct(String name, int price) {
        return new Product(name, price, 10, 0, null);
    }
}