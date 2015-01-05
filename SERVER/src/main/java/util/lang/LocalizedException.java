package util.lang;

public class LocalizedException extends LocalizedThrowable {
    public LocalizedException() {
    }

    public LocalizedException(String message) {
        super(message);
    }

    public LocalizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalizedException(Throwable cause) {
        super(cause);
    }

    public LocalizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
