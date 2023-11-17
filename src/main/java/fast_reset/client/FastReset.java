package fast_reset.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class FastReset {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final AtomicBoolean saving = new AtomicBoolean();
    public static final Object saveLock = new Object();
    public static FastResetConfig config;
    public static boolean saveOnQuit = true;
}
