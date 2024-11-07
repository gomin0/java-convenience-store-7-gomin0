package store.service;

import store.domain.product.Product;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductService {
    private final Map<String, Product> products;

    public ProductService() {
        FileReader fileReader = new FileReader();
        List<Product> productList = fileReader.readProducts(fileReader.readPromotions());
        this.products = productList.stream()
                .collect(Collectors.toMap(Product::getName, Function.identity()));
    }

    public List<Product> getAvailableProducts() {
        return List.copyOf(products.values());
    }

    public Product findProduct(String name) {
        if (!products.containsKey(name)) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다.");
        }
        return products.get(name);
    }
}