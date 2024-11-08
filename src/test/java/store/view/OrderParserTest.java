package store.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class OrderParserTest {

    @DisplayName("단일 상품 주문을 파싱한다")
    @Test
    void parseSingleOrder() {
        OrderParser parser = new OrderParser();
        List<OrderRequest> orders = parser.parse("[물-1]");

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0))
                .extracting("productName", "quantity")
                .containsExactly("물", 1);
    }

    @DisplayName("여러 상품 주문을 파싱한다")
    @Test
    void parseMultipleOrders() {
        OrderParser parser = new OrderParser();
        List<OrderRequest> orders = parser.parse("[콜라-2],[사이다-1],[물-3]");

        assertThat(orders).hasSize(3);
        assertThat(orders)
                .extracting("productName", "quantity")
                .containsExactly(
                        tuple("콜라", 2),
                        tuple("사이다", 1),
                        tuple("물", 3)
                );
    }

    @DisplayName("잘못된 형식의 입력은 무시한다")
    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "콜라-1",
            "[콜라]",
            "[콜라-]",
            "[-1]"
    })
    void ignoreInvalidFormat(String input) {
        OrderParser parser = new OrderParser();
        List<OrderRequest> orders = parser.parse(input);

        assertThat(orders).isEmpty();
    }
}