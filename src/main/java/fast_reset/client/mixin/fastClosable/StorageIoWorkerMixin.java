package fast_reset.client.mixin.fastClosable;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import fast_reset.client.interfaces.FastCloseable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.thread.TaskExecutor;
import net.minecraft.util.thread.TaskQueue;
import net.minecraft.world.storage.RegionBasedStorage;
import net.minecraft.world.storage.StorageIoWorker;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(StorageIoWorker.class)
public abstract class StorageIoWorkerMixin implements FastCloseable {

    @Shadow
    @Final
    private static Logger LOGGER;
    @Shadow
    @Final
    private TaskExecutor<TaskQueue.PrioritizedTask> field_24468;
    @Shadow
    @Final
    private RegionBasedStorage storage;
    @Shadow
    @Final
    private Map<ChunkPos, ?> results;
    @Shadow
    @Final
    private AtomicBoolean closed;

    @Unique
    private volatile boolean fastClosed;

    // these errors get thrown because we skip ThreadedAnvilChunkGenerator#completeAll, we just ignore them because we don't want anything to be saved anyway
    @WrapWithCondition(method = "write", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false))
    private boolean suppressErrorsWhenFastClosed(Logger logger, String s, Object a, Object b) {
        return !this.fastClosed;
    }

    @Override
    public void fast_reset$fastClose() {
        this.fastClosed = true;

        if (!this.closed.compareAndSet(false, true)) {
            return;
        }

        this.field_24468.close();
        this.results.clear();
        try {
            //noinspection DataFlowIssue - IntelliJ doesn't understand Mixin
            ((FastCloseable) (Object) this.storage).fast_reset$fastClose();
        } catch (Exception e) {
            LOGGER.error("Failed to close storage", e);
        }
    }
}
