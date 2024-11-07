package store.domain.order;

import store.domain.product.Product;

public class OrderResult {
    private final Product product;
    private final int payQuantity;
    private final int freeQuantity;
    private final int promotionDiscount;
    private final int membershipDiscount;

    public OrderResult(Product product, int payQuantity, int freeQuantity,
                       int promotionDiscount, int membershipDiscount) {
        this.product = product;
        this.payQuantity = payQuantity;
        this.freeQuantity = freeQuantity;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
    }

    public int calculateTotalPrice() {
        return (product.getPrice() * payQuantity) - promotionDiscount - membershipDiscount;
    }

    public Product getProduct() {
        return product;
    }

    public int getPayQuantity() {
        return payQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }
}