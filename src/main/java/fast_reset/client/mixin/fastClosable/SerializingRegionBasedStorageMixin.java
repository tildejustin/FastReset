package fast_reset.client.mixin.fastClosable;

import fast_reset.client.interfaces.FastCloseable;
import net.minecraft.world.storage.SerializingRegionBasedStorage;
import net.minecraft.world.storage.StorageIoWorker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(SerializingRegionBasedStorage.class)
public abstract class SerializingRegionBasedStorageMixin implements FastCloseable {

    @Shadow
    @Final
    private StorageIoWorker worker;

    @Override
    public void fast_reset$fastClose() throws IOException {
        ((FastCloseable) this.worker).fast_reset$fastClose();
    }
}
