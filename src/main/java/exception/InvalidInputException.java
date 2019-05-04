package exception;

/*
* Custom exception to wrap all invalid inputs in the context
* of the POS system.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
