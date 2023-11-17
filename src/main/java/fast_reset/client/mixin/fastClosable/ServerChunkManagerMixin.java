package fast_reset.client.mixin.fastClosable;

import fast_reset.client.interfaces.FastCloseable;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin implements FastCloseable {

    @Shadow
    @Final
    private ServerLightingProvider lightProvider;
    @Shadow
    @Final
    public ThreadedAnvilChunkStorage threadedAnvilChunkStorage;

    @Override
    public void fast_reset$fastClose() throws IOException {
        this.lightProvider.close();
        ((FastCloseable) this.threadedAnvilChunkStorage).fast_reset$fastClose();
    }
}
