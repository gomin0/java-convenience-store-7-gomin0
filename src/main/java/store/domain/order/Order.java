package store.domain.order;

import store.domain.product.Product;

public class Order {
    private final Product product;
    private final int quantity;

    public Order(Product product, int quantity) {
        validateOrder(product, quantity);
        this.product = product;
        this.quantity = quantity;
    }

    private void validateOrder(Product product, int quantity) {
        validateProduct(product);
        validateQuantity(quantity);
        product.validatePurchase(quantity);
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("[ERROR] 상품이 선택되지 않았습니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 구매 수량은 0보다 커야 합니다.");
        }
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}