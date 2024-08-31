package fun.club.common.util;

import java.util.Optional;

public class OptionalUtil {
    public static <T> T getOrElseThrow(Optional<T> optional, String message) {
        return optional.orElseThrow(() -> new RuntimeException(message));
    }
}
