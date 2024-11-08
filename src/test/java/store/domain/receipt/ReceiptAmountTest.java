package store.domain.receipt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.order.OrderResult;
import store.domain.product.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReceiptAmountTest {

    @DisplayName("할인이 없는 주문의 최종 금액을 계산한다")
    @Test
    void calculateAmountWithoutDiscount() {
        Product product = createProduct("물", 1000);
        OrderResult result = createOrderResult(product, 2, 0, 0, 0);
        ReceiptAmount amount = new ReceiptAmount(List.of(result));

        assertThat(amount.getTotalAmount()).isEqualTo(2000);
        assertThat(amount.getPromotionDiscount()).isZero();
        assertThat(amount.getMembershipDiscount()).isZero();
        assertThat(amount.getFinalAmount()).isEqualTo(2000);
    }

    @DisplayName("프로모션 할인이 적용된 주문의 최종 금액을 계산한다")
    @Test
    void calculateAmountWithPromotionDiscount() {
        Product product = createProduct("콜라", 1000);
        OrderResult result = createOrderResult(product, 2, 1, 1000, 0);
        ReceiptAmount amount = new ReceiptAmount(List.of(result));

        assertThat(amount.getTotalAmount()).isEqualTo(2000);
        assertThat(amount.getPromotionDiscount()).isEqualTo(1000);
        assertThat(amount.getMembershipDiscount()).isZero();
        assertThat(amount.getFinalAmount()).isEqualTo(1000);
    }

    @DisplayName("멤버십 할인이 적용된 주문의 최종 금액을 계산한다")
    @Test
    void calculateAmountWithMembershipDiscount() {
        Product product = createProduct("물", 10000);
        OrderResult result = createOrderResult(product, 1, 0, 0, 3000);
        ReceiptAmount amount = new ReceiptAmount(List.of(result));

        assertThat(amount.getTotalAmount()).isEqualTo(10000);
        assertThat(amount.getPromotionDiscount()).isZero();
        assertThat(amount.getMembershipDiscount()).isEqualTo(3000);
        assertThat(amount.getFinalAmount()).isEqualTo(7000);
    }

    @DisplayName("여러 상품의 총 금액과 할인을 계산한다")
    @Test
    void calculateMultipleOrdersAmount() {
        Product cola = createProduct("콜라", 1000);
        Product water = createProduct("물", 500);

        OrderResult colaResult = createOrderResult(cola, 2, 1, 1000, 300);
        OrderResult waterResult = createOrderResult(water, 2, 0, 0, 300);

        ReceiptAmount amount = new ReceiptAmount(List.of(colaResult, waterResult));

        assertThat(amount.getTotalAmount()).isEqualTo(3000); // (1000 * 2) + (500 * 2)
        assertThat(amount.getPromotionDiscount()).isEqualTo(1000);
        assertThat(amount.getMembershipDiscount()).isEqualTo(600);
        assertThat(amount.getFinalAmount()).isEqualTo(1400);
    }

    private Product createProduct(String name, int price) {
        return new Product(name, price, 10, 0, null);
    }

    private OrderResult createOrderResult(Product product, int payQuantity, int freeQuantity,
                                          int promotionDiscount, int membershipDiscount) {
        return new OrderResult(product, payQuantity, freeQuantity, promotionDiscount, membershipDiscount);
    }
}