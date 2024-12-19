package fast_reset.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import fast_reset.client.FastReset;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.storage.RegionBasedStorage;
import net.minecraft.world.storage.StorageIoWorker;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StorageIoWorker.class)
public abstract class StorageIoWorkerMixin {

    @Unique
    private volatile boolean fastClosed;

    @WrapWithCondition(method = "write", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false))
    private boolean suppressErrorsWhenFastClosed(Logger logger, String s, Object a, Object b) {
        return !this.fastClosed;
    }

    @WrapWithCondition(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/RegionBasedStorage;write(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/nbt/CompoundTag;)V"))
    private boolean doNotWriteToStorage(RegionBasedStorage storage, ChunkPos pos, CompoundTag tag) {
        return !this.fastClosed;
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void setFastClosed(CallbackInfo ci) {
        this.fastClosed = FastReset.shouldFastClose();
    }
}
