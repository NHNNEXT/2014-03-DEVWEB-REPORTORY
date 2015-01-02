package util.exceptions;

public class BadRequestException extends Exception {
    private Throwable ex;

    public BadRequestException() {
        super((Throwable)null);
    }

    public BadRequestException(String s) {
        super(s, null);
    }

    public BadRequestException(String s, Throwable ex) {
        super(s, null);
        this.ex = ex;
    }

    public Throwable getException() {
        return ex;
    }

    public Throwable getCause() {
        return ex;
    }
}
