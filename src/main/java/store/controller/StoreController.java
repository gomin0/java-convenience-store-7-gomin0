package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.exception.StockException;
import store.domain.order.Cart;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.promotion.PromotionPolicy;
import store.domain.receipt.Receipt;
import store.service.ProductService;
import store.view.InputView;
import store.view.OrderParser;
import store.view.OrderRequest;
import store.view.OutputView;

import java.util.List;

public class StoreController {
    private final ProductService productService;
    private final InputView inputView;
    private final OutputView outputView;
    private final OrderParser orderParser;

    public StoreController() {
        this.productService = new ProductService();
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.orderParser = new OrderParser();
    }

    public void run() {
        outputView.printWelcome();
        Cart cart = new Cart(DateTimes.now().toLocalDate());

        do {
            showProducts();
            processOrder(cart);
        } while (continueShopping());

        printReceipt(cart);
    }

    private void showProducts() {
        List<Product> products = productService.getAvailableProducts();
        outputView.printProducts(products);
    }

    private void processOrder(Cart cart) {
        try {
            List<OrderRequest> orderRequests = receiveOrder();
            processOrderRequests(cart, orderRequests);
            processMembership(cart);
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
            processOrder(cart);
        }
    }

    private List<OrderRequest> receiveOrder() {
        String orderInput = inputView.readOrder();
        return orderParser.parse(orderInput);
    }

    private void processOrderRequests(Cart cart, List<OrderRequest> orderRequests) {
        for (OrderRequest request : orderRequests) {
            Product product = productService.findProduct(request.getProductName());
            processPromotionOffer(product, request.getQuantity());
            Order order = new Order(product, request.getQuantity());
            cart.addOrder(order);
        }
    }

    private void processPromotionOffer(Product product, int quantity) {
        if (!canOfferPromotion(product, quantity)) {
            return;
        }

        PromotionPolicy policy = new PromotionPolicy(
                product.getPromotion(),
                quantity,
                DateTimes.now().toLocalDate()
        );

        int additionalNeeded = policy.getAdditionalQuantityNeeded();
        if (additionalNeeded > 0 && inputView.readPromotionAddition()) {
            validatePromotionStock(product, quantity + additionalNeeded);
        }
    }

    private boolean canOfferPromotion(Product product, int quantity) {
        return product.canApplyPromotion(
                quantity,
                DateTimes.now().toLocalDate()
        );
    }

    private void validatePromotionStock(Product product, int quantity) {
        try {
            product.validatePurchase(quantity);
        } catch (StockException e) {
            if (inputView.readNormalPriceConfirmation()) {
                return;
            }
            throw new IllegalArgumentException("[ERROR] 주문이 취소되었습니다.");
        }
    }

    private void processMembership(Cart cart) {
        if (inputView.readMembershipApplication()) {
            cart.applyMembership();
        }
    }

    private boolean continueShopping() {
        return inputView.readAdditionalPurchase();
    }

    private void printReceipt(Cart cart) {
        Receipt receipt = Receipt.create(cart.createCalculator().calculate());
        outputView.printReceipt(receipt.generate());
    }
}