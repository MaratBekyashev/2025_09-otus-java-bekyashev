package hw.exceptions;

public class BanknoteNotFoundException extends RuntimeException {
    public BanknoteNotFoundException(String message) {
        super(message);
    }
}
