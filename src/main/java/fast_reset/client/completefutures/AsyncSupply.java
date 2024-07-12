package fast_reset.client.completefutures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public final class AsyncSupply<T> implements Runnable {
    private CompletableFuture<T> future;
    private Supplier<? extends T> supplier;

    private AsyncSupply(CompletableFuture<T> future, Supplier<? extends T> supplier) {
        this.future = future;
        this.supplier = supplier;
    }

    @Override
    public void run() {
        CompletableFuture<T> f;
        Supplier<? extends T> s;
        if ((f = future) != null && (s = supplier) != null) {
            future = null;
            supplier = null;
            if (!f.isDone()) {
                try {
                    f.complete(s.get());
                } catch (Throwable ex) {
                    f.completeExceptionally(ex);
                }
            }
        }
    }

    public void cancel() {
        this.future.complete(null);
        this.future = null;
    }

    /**
     * Copy of {@link CompletableFuture#supplyAsync}, using our own cancellable version of AsyncSupply.
     */
    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        CompletableFuture<U> future = new CompletableFuture<>();
        executor.execute(new AsyncSupply<>(future, supplier));
        return future;
    }
}