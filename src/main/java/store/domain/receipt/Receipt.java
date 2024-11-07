package store.domain.receipt;

import store.domain.order.OrderResult;

import java.util.List;

public class Receipt {
    private static final String HEADER = "==============W 편의점================";
    private static final String FREE_ITEMS_HEADER = "=============증\t정===============";
    private static final String FOOTER = "====================================";

    private final List<OrderResult> orderResults;
    private final ReceiptAmount receiptAmount;

    private Receipt(List<OrderResult> orderResults) {
        this.orderResults = orderResults;
        this.receiptAmount = new ReceiptAmount(orderResults);
    }

    public static Receipt create(List<OrderResult> orderResults) {
        return new Receipt(orderResults);
    }

    public String generate() {
        StringBuilder receipt = new StringBuilder();
        appendHeader(receipt);
        appendPurchaseDetails(receipt);
        appendFreeItems(receipt);
        appendAmountDetails(receipt);
        return receipt.toString();
    }

    private void appendHeader(StringBuilder receipt) {
        receipt.append(HEADER).append("\n");
        receipt.append("상품명\t\t수량\t금액\n");
    }

    private void appendPurchaseDetails(StringBuilder receipt) {
        orderResults.forEach(result ->
                receipt.append(String.format("%s\t\t%d\t%,d\n",
                        result.getProduct().getName(),
                        result.getPayQuantity(),
                        result.getProduct().getPrice() * result.getPayQuantity()
                ))
        );
    }

    private void appendFreeItems(StringBuilder receipt) {
        if (!hasFreeItems()) {
            return;
        }

        receipt.append(FREE_ITEMS_HEADER).append("\n");
        appendFreeItemDetails(receipt);
    }

    private boolean hasFreeItems() {
        return orderResults.stream()
                .anyMatch(result -> result.getFreeQuantity() > 0);
    }

    private void appendFreeItemDetails(StringBuilder receipt) {
        orderResults.stream()
                .filter(result -> result.getFreeQuantity() > 0)
                .forEach(result ->
                        receipt.append(String.format("%s\t\t%d\n",
                                result.getProduct().getName(),
                                result.getFreeQuantity()
                        ))
                );
    }

    private void appendAmountDetails(StringBuilder receipt) {
        receipt.append(FOOTER).append("\n");
        receipt.append(String.format("총구매액\t\t\t%,d\n", receiptAmount.getTotalAmount()));
        receipt.append(String.format("행사할인\t\t\t-%,d\n", receiptAmount.getPromotionDiscount()));
        receipt.append(String.format("멤버십할인\t\t\t-%,d\n", receiptAmount.getMembershipDiscount()));
        receipt.append(String.format("내실돈\t\t\t%,d\n", receiptAmount.getFinalAmount()));
    }
}