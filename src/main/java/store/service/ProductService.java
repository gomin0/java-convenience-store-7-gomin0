package store.service;

import store.domain.product.Product;

import java.util.List;

public class ProductService {
    private final List<Product> products;

    public ProductService() {
        FileReader fileReader = new FileReader();
        this.products = fileReader.readProducts();
    }

    public List<Product> getAvailableProducts() {
        return products;
    }

    public Product findProduct(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다."));
    }
}