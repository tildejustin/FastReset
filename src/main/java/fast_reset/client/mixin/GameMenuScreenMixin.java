package fast_reset.client.mixin;

import fast_reset.client.FastReset;
import fast_reset.client.FastResetConfig;
import fast_reset.client.interfaces.FRMinecraftServer;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @ModifyVariable(method = "initWidgets", at = @At("STORE"), ordinal = 1)
    private ButtonWidget createFastResetButton(ButtonWidget saveButton) {
        assert this.client != null;
        if (!this.client.isInSingleplayer() || !this.shouldFastReset()) {
            return saveButton;
        }

        String menuQuitWorld = I18n.translate("fast_reset.menu.quitWorld");
        int height = 20;
        int width;
        int x;
        int y;
        switch (FastReset.config.buttonLocation) {
            case REPLACE_SQ:
                width = saveButton.getWidth();
                x = saveButton.x;
                y = saveButton.y;

                saveButton.setWidth(this.textRenderer.getStringWidth(saveButton.getMessage()) + 30);
                saveButton.x = this.width - saveButton.getWidth() - 4;
                saveButton.y = this.height - saveButton.getHeight() - 4;
                break;
            case CENTER:
                width = saveButton.getWidth();
                x = saveButton.x;
                y = saveButton.y + 24;
                break;
            case BOTTOM_RIGHT:
            default:
                width = this.textRenderer.getStringWidth(menuQuitWorld) + 30;
                x = this.width - width - 4;
                y = this.height - height - 4;
        }

        AbstractButtonWidget fastResetButton = this.addButton(new ButtonWidget(x, y, width, height, menuQuitWorld, button -> {
            if (this.client != null && this.client.getServer() != null) {
                ((FRMinecraftServer) this.client.getServer()).fastReset$fastReset();
            }
            saveButton.onPress();
        }));

        fastResetButton.visible = FastReset.config.buttonLocation != FastResetConfig.ButtonLocation.HIDE;

        return saveButton;
    }

    @Unique
    private boolean shouldFastReset() {
        if (FastReset.config.alwaysSaveAfter == 0) {
            return true;
        }
        return this.client.getServer() != null && this.client.getServer().getTicks() <= FastReset.config.alwaysSaveAfter * 20;
    }
}