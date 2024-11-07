package store.view;

public class OrderRequest {
    private final String productName;
    private final int quantity;

    public OrderRequest(String productName, int quantity) {
        validate(productName, quantity);
        this.productName = productName;
        this.quantity = quantity;
    }

    private void validate(String productName, int quantity) {
        validateProductName(productName);
        validateQuantity(quantity);
    }

    private void validateProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상품명이 비어있습니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 수량은 0보다 커야 합니다.");
        }
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}