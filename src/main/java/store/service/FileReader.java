package store.service;

import store.domain.product.Product;
import store.domain.promotion.Promotion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileReader {
    private static final String PRODUCTS_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_PATH = "src/main/resources/promotions.md";

    public List<Product> readProducts() {
        try {
            List<String> lines = readLines(PRODUCTS_PATH);
            Map<String, Promotion> promotions = readPromotions().stream()
                    .collect(Collectors.toMap(Promotion::getName, Function.identity()));
            return parseProducts(lines, promotions);
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 상품 파일을 읽을 수 없습니다.");
        }
    }

    public List<Promotion> readPromotions() {
        try {
            List<String> lines = readLines(PROMOTIONS_PATH);
            return parsePromotions(lines);
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 프로모션 파일을 읽을 수 없습니다.");
        }
    }

    private List<String> readLines(String path) throws IOException {
        return Files.readAllLines(Path.of(path));
    }

    private List<Product> parseProducts(List<String> lines, Map<String, Promotion> promotions) {
        Map<String, List<ProductInfo>> productGroups = lines.stream()
                .skip(1)
                .map(this::parseProductInfo)
                .collect(Collectors.groupingBy(ProductInfo::name));

        return productGroups.values().stream()
                .map(group -> createProductFromGroup(group, promotions))
                .collect(Collectors.toList());
    }

    private ProductInfo parseProductInfo(String line) {
        String[] parts = line.split(",");
        validateProductParts(parts);

        return new ProductInfo(
                parts[0],
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                "null".equals(parts[3]) ? null : parts[3]
        );
    }

    private Product createProductFromGroup(List<ProductInfo> group, Map<String, Promotion> promotions) {
        ProductInfo first = group.get(0);
        int normalStock = 0;
        int promotionStock = 0;

        for (ProductInfo info : group) {
            if (info.promotionName == null) {
                normalStock = info.quantity;
            } else {
                promotionStock = info.quantity;
            }
        }

        String promotionName = group.stream()
                .map(info -> info.promotionName)
                .filter(name -> name != null)
                .findFirst()
                .orElse(null);

        return new Product(
                first.name,
                first.price,
                normalStock,
                promotionStock,
                promotions.get(promotionName)
        );
    }

    private List<Promotion> parsePromotions(List<String> lines) {
        return lines.stream()
                .skip(1)
                .map(this::createPromotion)
                .collect(Collectors.toList());
    }

    private Promotion createPromotion(String line) {
        String[] parts = line.split(",");
        validatePromotionParts(parts);

        return new Promotion(
                parts[0],
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

    private record ProductInfo(String name, int price, int quantity, String promotionName) {}
}