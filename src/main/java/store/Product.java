package store;

public class Product {
    private final String name;
    private final int price;
    private int normalStock;
    private int promotionStock;
    private final Promotion promotion;

    public Product(String name, int price, int normalStock, int promotionStock, Promotion promotion) {
        validate(name, price, normalStock, promotionStock);
        this.name = name;
        this.price = price;
        this.normalStock = normalStock;
        this.promotionStock = promotionStock;
        this.promotion = promotion;
    }

    private void validate(String name, int price, int normalStock, int promotionStock) {
        validateName(name);
        validatePrice(price);
        validateStock(normalStock);
        validateStock(promotionStock);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상품명은 비어있을 수 없습니다.");
        }
    }

    private void validatePrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("[ERROR] 가격은 0 이상이어야 합니다.");
        }
    }

    private void validateStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("[ERROR] 재고는 0 이상이어야 합니다.");
        }
    }

    public String formatProductInfo() {
        if (getTotalStock() == 0) {
            return formatOutOfStock();
        }
        return formatInStock();
    }

    private String formatOutOfStock() {
        return String.format("- %s %,d원 재고 없음%s",
                name, price, getPromotionName());
    }

    private String formatInStock() {
        return String.format("- %s %,d원 %d개%s",
                name, price, getTotalStock(), getPromotionName());
    }

    private String getPromotionName() {
        if (promotion == null) {
            return "";
        }
        return " " + promotion.getName();
    }

    private int getTotalStock() {
        return normalStock + promotionStock;
    }
}