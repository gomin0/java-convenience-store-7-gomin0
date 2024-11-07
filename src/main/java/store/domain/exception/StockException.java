package store.domain.exception;

public class StockException extends IllegalArgumentException {
    public StockException(String message) {
        super("[ERROR] " + message);
    }
}