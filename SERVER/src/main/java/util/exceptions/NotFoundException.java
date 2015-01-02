package util.exceptions;

public class NotFoundException extends Exception {
    private Throwable ex;

    public NotFoundException() {
        super((Throwable)null);
    }

    public NotFoundException(String s) {
        super(s, null);
    }

    public NotFoundException(String s, Throwable ex) {
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
