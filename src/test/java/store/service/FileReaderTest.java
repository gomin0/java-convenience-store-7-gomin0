package store.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileReaderTest {
    private static final String TEST_PRODUCTS_PATH = "src/main/resources/products.md";
    private static final String TEST_PROMOTIONS_PATH = "src/main/resources/promotions.md";

    @BeforeEach
    void setUp() throws IOException {
        // 테스트용 상품 파일 생성
        Files.writeString(Path.of(TEST_PRODUCTS_PATH), """
                name,price,quantity,promotion
                콜라,1000,10,탄산2+1
                콜라,1000,5,null
                물,500,10,null
                """);

        // 테스트용 프로모션 파일 생성
        Files.writeString(Path.of(TEST_PROMOTIONS_PATH), """
                name,buy,get,start_date,end_date
                탄산2+1,2,1,2024-01-01,2024-12-31
                """);
    }

    @DisplayName("상품 목록을 읽어온다")
    @Test
    void readProducts() {
        FileReader fileReader = new FileReader();
        List<Product> products = fileReader.readProducts();

        assertThat(products).hasSize(3);

        Product promotionCola = products.get(0);
        assertThat(promotionCola.getName()).isEqualTo("콜라");
        assertThat(promotionCola.getPrice()).isEqualTo(1000);
        assertThat(promotionCola.hasPromotion()).isTrue();

        Product normalCola = products.get(1);
        assertThat(normalCola.getName()).isEqualTo("콜라");
        assertThat(normalCola.getPrice()).isEqualTo(1000);
        assertThat(normalCola.hasPromotion()).isFalse();

        Product water = products.get(2);
        assertThat(water.getName()).isEqualTo("물");
        assertThat(water.getPrice()).isEqualTo(500);
        assertThat(water.hasPromotion()).isFalse();
    }

    @DisplayName("프로모션 목록을 읽어온다")
    @Test
    void readPromotions() {
        FileReader fileReader = new FileReader();
        List<Promotion> promotions = fileReader.readPromotions();

        assertThat(promotions).hasSize(1);

        Promotion promotion = promotions.get(0);
        assertThat(promotion.getName()).isEqualTo("탄산2+1");
        assertThat(promotion.isValidPeriod(LocalDate.of(2024, 6, 1))).isTrue();
    }

    @DisplayName("상품 데이터 형식이 올바르지 않으면 예외가 발생한다")
    @Test
    void validateInvalidProductFormat() throws IOException {
        Files.writeString(Path.of(TEST_PRODUCTS_PATH), """
                name,price,quantity
                콜라,1000,10
                """);

        FileReader fileReader = new FileReader();
        assertThatThrownBy(fileReader::readProducts)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("[ERROR] 상품 데이터 형식이 올바르지 않습니다");
    }

    @DisplayName("프로모션 데이터 형식이 올바르지 않으면 예외가 발생한다")
    @Test
    void validateInvalidPromotionFormat() throws IOException {
        Files.writeString(Path.of(TEST_PROMOTIONS_PATH), """
                name,buy,get,start_date
                탄산2+1,2,1,2024-01-01
                """);

        FileReader fileReader = new FileReader();
        assertThatThrownBy(fileReader::readPromotions)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("[ERROR] 프로모션 데이터 형식이 올바르지 않습니다");
    }

    @AfterEach
    void tearDown() throws IOException {
        // 원래 상품 파일 내용으로 복원
        Files.writeString(Path.of(TEST_PRODUCTS_PATH), """
            name,price,quantity,promotion
            콜라,1000,10,탄산2+1
            콜라,1000,10,null
            사이다,1000,8,탄산2+1
            사이다,1000,7,null
            오렌지주스,1800,9,MD추천상품
            오렌지주스,1800,0,null
            탄산수,1200,5,탄산2+1
            탄산수,1200,0,null
            물,500,10,null
            비타민워터,1500,6,null
            감자칩,1500,5,반짝할인
            감자칩,1500,5,null
            초코바,1200,5,MD추천상품
            초코바,1200,5,null
            에너지바,2000,5,null
            정식도시락,6400,8,null
            컵라면,1700,1,MD추천상품
            컵라면,1700,10,null
            """);

        // 원래 프로모션 파일 내용으로 복원
        Files.writeString(Path.of(TEST_PROMOTIONS_PATH), """
            name,buy,get,start_date,end_date
            탄산2+1,2,1,2024-01-01,2024-12-31
            MD추천상품,1,1,2024-01-01,2024-12-31
            반짝할인,1,1,2024-11-01,2024-11-30
            """);
    }
}