package fast_reset.client.mixin.fastClosable;

import fast_reset.client.interfaces.FastCloseable;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.util.ThrowableDeliverer;
import net.minecraft.world.storage.RegionBasedStorage;
import net.minecraft.world.storage.RegionFile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(RegionBasedStorage.class)
public abstract class RegionBasedStorageMixin implements FastCloseable {

    @Shadow
    @Final
    private Long2ObjectLinkedOpenHashMap<RegionFile> cachedRegionFiles;

    @Override
    public void fast_reset$fastClose() throws IOException {
        ThrowableDeliverer<IOException> throwableDeliverer = new ThrowableDeliverer<>();
        for (RegionFile regionFile : this.cachedRegionFiles.values()) {
            try {
                ((FastCloseable) regionFile).fast_reset$fastClose();
            } catch (IOException iOException) {
                throwableDeliverer.add(iOException);
            }
        }
        throwableDeliverer.deliver();
    }
}
