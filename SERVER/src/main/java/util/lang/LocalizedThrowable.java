package util.lang;

import java.util.ResourceBundle;

public class LocalizedThrowable extends Throwable {
    ResourceBundle labels = ResourceBundle.getBundle("messages.exception");

    public LocalizedThrowable() {
    }

    public LocalizedThrowable(String message) {
        super(message);
    }

    public LocalizedThrowable(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalizedThrowable(Throwable cause) {
        super(cause);
    }

    public LocalizedThrowable(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getLocalizedMessage() {
        return labels.getString(getMessage());
    }

}
