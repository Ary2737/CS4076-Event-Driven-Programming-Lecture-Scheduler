package ServerController;

public class IncorrectActionException extends RuntimeException {

    public IncorrectActionException(String message) {
        super(message);
    }
}
