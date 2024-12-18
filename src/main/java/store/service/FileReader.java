package store.service;

import store.domain.product.Product;
import store.domain.promotion.Promotion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
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
        List<ProductInfo> productInfos = parseProductInfos(lines);
        Map<String, List<ProductInfo>> productGroups = groupProductsByName(productInfos);
        return createProducts(productGroups, promotions);
    }

    private List<ProductInfo> parseProductInfos(List<String> lines) {
        return lines.stream()
                .skip(1)
                .map(this::parseProductInfo)
                .collect(Collectors.toList());
    }

    private Map<String, List<ProductInfo>> groupProductsByName(List<ProductInfo> productInfos) {
        return productInfos.stream()
                .collect(Collectors.groupingBy(ProductInfo::name));
    }

    private List<Product> createProducts(Map<String, List<ProductInfo>> productGroups, Map<String, Promotion> promotions) {
        List<Product> products = new ArrayList<>();
        productGroups.forEach((name, group) -> addProductsFromGroup(products, group, promotions));
        return products;
    }

    private void addProductsFromGroup(List<Product> products, List<ProductInfo> group, Map<String, Promotion> promotions) {
        if (needsOutOfStockProduct(group)) {
            addOutOfStockProduct(products, group.get(0));
        }
        addExistingProducts(products, group, promotions);
    }

    private boolean needsOutOfStockProduct(List<ProductInfo> group) {
        return group.stream().noneMatch(info -> info.promotionName == null);
    }

    private void addOutOfStockProduct(List<Product> products, ProductInfo info) {
        products.add(new Product(info.name, info.price, 0, 0, null));
    }

    private void addExistingProducts(List<Product> products, List<ProductInfo> group, Map<String, Promotion> promotions) {
        group.forEach(info -> products.add(createProduct(info, promotions)));
    }

    private Product createProduct(ProductInfo info, Map<String, Promotion> promotions) {
        return new Product(
                info.name,
                info.price,
                info.promotionName == null ? info.quantity : 0,
                info.promotionName == null ? 0 : info.quantity,
                promotions.get(info.promotionName)
        );
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