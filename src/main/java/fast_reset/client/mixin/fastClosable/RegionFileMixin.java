package fast_reset.client.mixin.fastClosable;

import fast_reset.client.interfaces.FastCloseable;
import net.minecraft.world.storage.RegionFile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.nio.channels.FileChannel;

@Mixin(RegionFile.class)
public abstract class RegionFileMixin implements FastCloseable {

    @Shadow
    @Final
    private FileChannel channel;

    @Override
    public void fast_reset$fastClose() throws IOException {
        this.channel.close();
    }
}
