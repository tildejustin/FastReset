package fast_reset.client.mixin;

import fast_reset.client.FastReset;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @ModifyConstant(method = "shutdown", constant = @Constant(intValue = 0, ordinal = 0))
    private int disableWorldSaving(int savingDisabled) {
        return FastReset.saveOnQuit ? savingDisabled : 1;
    }

    @Redirect(method = "shutdown", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;saveAllPlayerData()V"))
    private void disablePlayerSaving(PlayerManager playerManager) {
        if (FastReset.saveOnQuit) {
            playerManager.saveAllPlayerData();
        }
    }
}