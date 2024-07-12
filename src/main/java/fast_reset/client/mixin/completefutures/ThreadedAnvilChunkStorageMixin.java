package fast_reset.client.mixin.completefutures;

import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.thread.ThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {

    @Redirect(
            method = "loadChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/CompletableFuture;supplyAsync(Ljava/util/function/Supplier;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private <T> CompletableFuture<T> redirectCompletableFuture(Supplier<T> supplier, Executor executor) {
        return ((ThreadExecutor<?>) executor).submit(supplier);
    }
}
