package fast_reset.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FastReset {
    public static final Logger LOGGER = LogManager.getLogger();
    private static final ThreadLocal<Boolean> FAST_CLOSE = new ThreadLocal<>();
    public static FastResetConfig config;

    public static boolean shouldFastClose() {
        return Boolean.TRUE.equals(FAST_CLOSE.get());
    }

    public static void enableFastClose() {
        FAST_CLOSE.set(true);
    }
}
