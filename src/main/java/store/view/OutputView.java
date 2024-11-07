package store.view;

import store.domain.product.Product;

import java.util.List;

public class OutputView {
    public void printWelcome() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
    }

    public void printProducts(List<Product> products) {
        products.forEach(product ->
                System.out.println(product.formatProductInfo())
        );
        System.out.println();
    }

    public void printReceipt(String receipt) {
        System.out.println(receipt);
    }

    public void printError(String message) {
        System.out.println(message);
    }
}