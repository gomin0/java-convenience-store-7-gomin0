package store.domain.exception;

public class StockException extends IllegalArgumentException {
    public StockException(String message) {
        super("[ERROR] " + message + " 다시 입력해 주세요.");
    }
}