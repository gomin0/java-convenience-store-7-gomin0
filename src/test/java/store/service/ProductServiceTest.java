package store.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductServiceTest {

    @DisplayName("상품 목록을 조회한다")
    @Test
    void getAvailableProducts() {
        ProductService productService = new ProductService();

        assertThat(productService.getAvailableProducts())
                .hasSize(18)
                .extracting("name")
                .contains("콜라", "물", "사이다");
    }

    @DisplayName("이름으로 상품을 찾는다")
    @Test
    void findProduct() {
        ProductService productService = new ProductService();

        assertThat(productService.findProduct("콜라"))
                .extracting("name", "price")
                .containsExactly("콜라", 1000);
    }

    @DisplayName("존재하지 않는 상품을 찾으면 예외가 발생한다")
    @Test
    void findNonExistentProduct() {
        ProductService productService = new ProductService();

        assertThatThrownBy(() -> productService.findProduct("없는상품"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 존재하지 않는 상품입니다");
    }
}