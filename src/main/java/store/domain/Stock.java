package store.domain;


public class Stock {
    private int normalStock;
    private int promotionStock;

    public Stock(int normalStock, int promotionStock) {
        validate(normalStock, promotionStock);
        this.normalStock = normalStock;
        this.promotionStock = promotionStock;
    }

    private void validate(int normalStock, int promotionStock) {
        if (normalStock < 0 || promotionStock < 0) {
            throw new IllegalArgumentException("[ERROR] 재고는 0보다 작을 수 없습니다.");
        }
    }

    public boolean hasEnoughStock(int quantity) {
        return getTotalStock() >= quantity;
    }

    public int getTotalStock() {
        return normalStock + promotionStock;
    }

    public boolean hasPromotionStock() {
        return promotionStock > 0;
    }

    public void reducePromotionStock(int quantity) {
        validateReduceQuantity(quantity);
        promotionStock -= quantity;
    }

    public void reduceNormalStock(int quantity) {
        validateReduceQuantity(quantity);
        normalStock -= quantity;
    }

    private void validateReduceQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("[ERROR] 차감할 수량은 0보다 작을 수 없습니다.");
        }
    }
}