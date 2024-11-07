package store.service;

import store.domain.product.Product;
import store.domain.promotion.Promotion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileReader {
    private static final String PRODUCTS_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_PATH = "src/main/resources/promotions.md";

    public List<Product> readProducts(Map<String, Promotion> promotions) {
        List<String> lines = readLines(PRODUCTS_PATH);
        return parseProducts(lines, promotions);
    }

    public Map<String, Promotion> readPromotions() {
        List<String> lines = readLines(PROMOTIONS_PATH);
        return parsePromotions(lines);
    }

    private List<String> readLines(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 파일을 읽을 수 없습니다.");
        }
    }

    private List<Product> parseProducts(List<String> lines, Map<String, Promotion> promotions) {
        return lines.stream()
                .skip(1)
                .map(line -> createProduct(line, promotions))
                .collect(Collectors.toList());
    }

    private Product createProduct(String line, Map<String, Promotion> promotions) {
        String[] parts = line.split(",");
        validateProductParts(parts);

        String name = parts[0];
        int price = Integer.parseInt(parts[1]);
        int quantity = Integer.parseInt(parts[2]);
        String promotionName = "null".equals(parts[3]) ? null : parts[3];

        return new Product(name, price,
                quantity,
                0, // 프로모션 재고는 나중에 설정
                promotions.get(promotionName));
    }

    private Map<String, Promotion> parsePromotions(List<String> lines) {
        return lines.stream()
                .skip(1)
                .map(this::createPromotion)
                .collect(Collectors.toMap(Promotion::getName, promotion -> promotion));
    }

    private Promotion createPromotion(String line) {
        String[] parts = line.split(",");
        validatePromotionParts(parts);

        return new Promotion(
                parts[0],
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                LocalDate.parse(parts[3]),
                LocalDate.parse(parts[4])
        );
    }

    private void validateProductParts(String[] parts) {
        if (parts.length != 4) {
            throw new IllegalStateException("[ERROR] 상품 데이터 형식이 올바르지 않습니다.");
        }
    }

    private void validatePromotionParts(String[] parts) {
        if (parts.length != 5) {
            throw new IllegalStateException("[ERROR] 프로모션 데이터 형식이 올바르지 않습니다.");
        }
    }
}