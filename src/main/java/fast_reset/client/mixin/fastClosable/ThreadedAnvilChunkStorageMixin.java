package fast_reset.client.mixin.fastClosable;

import fast_reset.client.interfaces.FastCloseable;
import net.minecraft.server.world.ChunkTaskPrioritySystem;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin extends VersionedChunkStorageMixin {

    @Shadow
    @Final
    private ChunkTaskPrioritySystem chunkTaskPrioritySystem;
    @Shadow
    @Final
    private PointOfInterestStorage pointOfInterestStorage;

    @Override
    public void fast_reset$fastClose() throws IOException {
        try {
            this.chunkTaskPrioritySystem.close();
            ((FastCloseable) this.pointOfInterestStorage).fast_reset$fastClose();
        } finally {
            super.fast_reset$fastClose();
        }
    }
}
