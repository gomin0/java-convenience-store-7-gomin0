package store.domain.receipt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.order.OrderResult;
import store.domain.product.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReceiptTest {

    @DisplayName("할인이 없는 단일 상품 구매 영수증을 생성한다")
    @Test
    void generateReceiptForSingleItemWithoutDiscount() {
        Product product = createProduct("물", 1000);
        OrderResult result = createOrderResult(product, 2, 0, 0, 0);
        Receipt receipt = Receipt.create(List.of(result));

        String expectedReceipt = """
                ==============W 편의점================
                상품명\t\t수량\t금액
                물\t\t2\t2,000
                ====================================
                총구매액\t\t\t2,000
                행사할인\t\t\t-0
                멤버십할인\t\t\t-0
                내실돈\t\t\t2,000
                """;

        assertThat(receipt.generate()).isEqualTo(expectedReceipt);
    }

    @DisplayName("프로모션 할인과 증정 상품이 있는 영수증을 생성한다")
    @Test
    void generateReceiptWithPromotionAndFreeItems() {
        Product product = createProduct("콜라", 1000);
        OrderResult result = createOrderResult(product, 2, 1, 1000, 0);
        Receipt receipt = Receipt.create(List.of(result));

        String expectedReceipt = """
                ==============W 편의점================
                상품명\t\t수량\t금액
                콜라\t\t2\t2,000
                =============증\t정===============
                콜라\t\t1
                ====================================
                총구매액\t\t\t2,000
                행사할인\t\t\t-1,000
                멤버십할인\t\t\t-0
                내실돈\t\t\t1,000
                """;

        assertThat(receipt.generate()).isEqualTo(expectedReceipt);
    }

    @DisplayName("여러 상품과 모든 할인이 적용된 영수증을 생성한다")
    @Test
    void generateReceiptWithMultipleItemsAndAllDiscounts() {
        Product cola = createProduct("콜라", 1000);
        Product water = createProduct("물", 500);
        OrderResult colaResult = createOrderResult(cola, 2, 1, 1000, 300);
        OrderResult waterResult = createOrderResult(water, 2, 0, 0, 300);
        Receipt receipt = Receipt.create(List.of(colaResult, waterResult));

        String expectedReceipt = """
                ==============W 편의점================
                상품명\t\t수량\t금액
                콜라\t\t2\t2,000
                물\t\t2\t1,000
                =============증\t정===============
                콜라\t\t1
                ====================================
                총구매액\t\t\t3,000
                행사할인\t\t\t-1,000
                멤버십할인\t\t\t-600
                내실돈\t\t\t1,400
                """;

        assertThat(receipt.generate()).isEqualTo(expectedReceipt);
    }

    private Product createProduct(String name, int price) {
        return new Product(name, price, 10, 0, null);
    }

    private OrderResult createOrderResult(Product product, int payQuantity, int freeQuantity,
                                          int promotionDiscount, int membershipDiscount) {
        return new OrderResult(product, payQuantity, freeQuantity, promotionDiscount, membershipDiscount);
    }
}