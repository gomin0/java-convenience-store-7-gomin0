package store.domain.product;

import store.domain.exception.StockException;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionPolicy;

import java.time.LocalDate;

public class Product {
    private final String name;
    private final int price;
    private final Stock stock;
    private final Promotion promotion;

    public Product(String name, int price, int normalStock, int promotionStock, Promotion promotion) {
        validateName(name);
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.stock = new Stock(normalStock, promotionStock);
        this.promotion = promotion;
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

    public void validatePurchase(int quantity) {
        if (!stock.hasEnoughStock(quantity)) {
            throw new StockException("재고 수량을 초과하여 구매할 수 없습니다.");
        }
    }

    public boolean canApplyPromotion(int quantity, LocalDate currentDate) {
        return hasPromotion() &&
                new PromotionPolicy(promotion, quantity, currentDate).isApplicable() &&
                stock.hasPromotionStock();
    }

    public void reducePurchaseStock(int quantity) {
        if (stock.hasPromotionStock()) {
            stock.reducePromotionStock(quantity);
            return;
        }
        stock.reduceNormalStock(quantity);
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String formatProductInfo() {
        if (stock.getTotalStock() == 0) {
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
                name, price, stock.getTotalStock(), getPromotionName());
    }

    private String getPromotionName() {
        if (!hasPromotion()) {
            return "";
        }
        return " " + promotion.getName();
    }
}