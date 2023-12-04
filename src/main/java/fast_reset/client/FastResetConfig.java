package fast_reset.client;

import org.mcsr.speedrunapi.config.api.SpeedrunConfig;

public class FastResetConfig implements SpeedrunConfig {

    public ButtonLocation buttonLocation = ButtonLocation.BOTTOM_RIGHT;

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
