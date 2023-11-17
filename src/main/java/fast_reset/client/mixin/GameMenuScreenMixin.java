package fast_reset.client.mixin;

import fast_reset.client.FastReset;
import fast_reset.client.interfaces.FRMinecraftServer;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "initWidgets", at = @At("STORE"), ordinal = 1)
    private ButtonWidget createFastResetButton(ButtonWidget saveButton) {
        assert this.client != null;
        if (!this.client.isInSingleplayer()) {
            return saveButton;
        }

        Text menuQuitWorld = new TranslatableText("fast_reset.menu.quitWorld");
        int height = 20;
        int width;
        int x;
        int y;
        switch (FastReset.config.buttonLocation) {
            case BOTTOM_RIGHT:
                width = this.textRenderer.getWidth(menuQuitWorld) + 30;
                x = this.width - width - 4;
                y = this.height - height - 4;
                break;
            case CENTER:
                width = saveButton.getWidth();
                x = this.width / 2 - width / 2;
                y = this.height / 4 + 148 - height;
                break;
            case REPLACE_SQ:
            default:
                width = saveButton.getWidth();
                x = saveButton.x;
                y = saveButton.y;

                saveButton.setWidth(this.textRenderer.getWidth(saveButton.getMessage()) + 30);
                saveButton.x = this.width - saveButton.getWidth() - 4;
                saveButton.y = this.height - saveButton.getHeight() - 4;
                break;
        }

        this.addButton(new ButtonWidget(x, y, width, height, menuQuitWorld, button -> {
            if (this.client != null && this.client.getServer() != null) {
                ((FRMinecraftServer) this.client.getServer()).fastReset$fastReset();
            }
            saveButton.onPress();
        }));
        return saveButton;
    }
}