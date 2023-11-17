package fast_reset.client.interfaces;

import java.io.IOException;

public interface FastCloseable {

    void fast_reset$fastClose() throws IOException;
}
