package util.exceptions;

public class ForbiddenException extends Exception {
    private Throwable ex;

    public ForbiddenException() {
        super((Throwable)null);
    }

    public ForbiddenException(String s) {
        super(s, null);
    }

    public ForbiddenException(String s, Throwable ex) {
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
