package fast_reset.client;

import org.mcsr.speedrunapi.config.api.SpeedrunConfig;
import org.mcsr.speedrunapi.config.api.annotations.Config;

public class FastResetConfig implements SpeedrunConfig {

    public ButtonLocation buttonLocation = ButtonLocation.BOTTOM_RIGHT;

    @Config.Numbers.Whole.Bounds(max = 60 * 5, enforce = Config.Numbers.EnforceBounds.MIN_ONLY)
    public int alwaysSaveAfter = 60 * 2 + 30;

    {
        FastReset.config = this;
    }

    @Override
    public String modID() {
        return "fast_reset";
    }

    public enum ButtonLocation {
        BOTTOM_RIGHT,
        CENTER,
        REPLACE_SQ,
        HIDE
    }
}
