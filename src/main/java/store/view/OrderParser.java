package store.view;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderParser {
    private static final Pattern ORDER_PATTERN = Pattern.compile("\\[([^-]+)-(\\d+)]");

    public List<OrderRequest> parse(String input) {
        List<OrderRequest> orders = new ArrayList<>();
        Matcher matcher = ORDER_PATTERN.matcher(input);

        while (matcher.find()) {
            String productName = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));
            orders.add(new OrderRequest(productName, quantity));
        }

        return orders;
    }
}