package util;

import java.sql.SQLException;

/**
 * Created by infinitu on 14. 12. 25..
 */

@FunctionalInterface
public interface ConsumerThatThrow<T> {
    void accept(T a) throws SQLException;
}
