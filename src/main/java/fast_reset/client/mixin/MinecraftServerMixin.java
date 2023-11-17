package fast_reset.client.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fast_reset.client.interfaces.FRMinecraftServer;
import fast_reset.client.interfaces.FastCloseable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements FRMinecraftServer {

    @Shadow
    private volatile boolean loading;

    @Unique
    private volatile boolean fastReset;

    @WrapWithCondition(method = "shutdown", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;saveAllPlayerData()V"))
    private boolean disablePlayerSaving(PlayerManager playerManager) {
        return this.shouldSave();
    }

    @WrapWithCondition(method = "shutdown", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;save(ZZZ)Z"))
    private boolean disableSaving(MinecraftServer server, boolean bl, boolean bl2, boolean bl3) {
        return this.shouldSave();
    }

    @WrapOperation(method = "shutdown", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;close()V"))
    private void fastClose(ServerWorld serverWorld, Operation<Void> original) throws IOException {
        if (!this.shouldSave()) {
            ((FastCloseable) serverWorld.getChunkManager()).fast_reset$fastClose();
        } else {
            original.call(serverWorld);
        }
    }

    @Unique
    private boolean shouldSave() {
        return !this.fastReset && this.loading;
    }

    @Override
    public void fastReset$fastReset() {
        this.fastReset = true;
    }
}