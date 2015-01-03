package util.exceptions;

import util.lang.LocalizedException;

public class ForbiddenException extends LocalizedException {
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
