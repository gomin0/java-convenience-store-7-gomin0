package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String ORDER_FORMAT = "\\[([^-]+-\\d+)](?:,\\[([^-]+-\\d+)])*";

    public String readOrder() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        validateOrderFormat(input);
        return input;
    }

    public boolean readMembershipApplication() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return readYesOrNo();
    }

    public boolean readAdditionalPurchase() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return readYesOrNo();
    }

    public boolean readPromotionAddition() {
        System.out.println("현재 상품은 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        return readYesOrNo();
    }

    public boolean readNormalPriceConfirmation() {
        System.out.println("일부 수량은 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        return readYesOrNo();
    }

    private boolean readYesOrNo() {
        String input = Console.readLine().toUpperCase();
        validateYesOrNo(input);
        return "Y".equals(input);
    }

    private void validateOrderFormat(String input) {
        if (!input.matches(ORDER_FORMAT)) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다.");
        }
    }

    private void validateYesOrNo(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new IllegalArgumentException("[ERROR] Y 또는 N만 입력 가능합니다.");
        }
    }
}