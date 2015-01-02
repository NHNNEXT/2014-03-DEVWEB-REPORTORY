package util.exceptions;

public class InternalServerErrorException extends Exception {
    private Throwable ex;

    public InternalServerErrorException() {
        super((Throwable)null);
    }

    public InternalServerErrorException(String s) {
        super(s, null);
    }

    public InternalServerErrorException(String s, Throwable ex) {
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
