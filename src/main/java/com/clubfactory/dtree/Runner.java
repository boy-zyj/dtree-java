package com.clubfactory.dtree;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Runner<T> extends Description {

    void run(T target) throws NoMatchException;

    default Chain<T> then(Runner<T> runner) {
        return new Chain<>(this, runner);
    }

    default Capture<T> capture(Runner<T> onRejectedRunner) {
        return new Capture<>(this, onRejectedRunner);
    }

    default Capture<T> capture(BiConsumer<T, RuntimeException> onRejectedErrorHandler) {
        return new Capture<>(this, onRejectedErrorHandler);
    }

    default Capture<T> capture(Consumer<RuntimeException> onRejectedErrorHandler) {
        return new Capture<>(this, (obj, e) -> onRejectedErrorHandler.accept(e));
    }

    default Capture<T> capture(Runner<T> onRejectedRunner, BiConsumer<T, RuntimeException> onRejectedErrorHandler) {
        return new Capture<>(this, onRejectedRunner, onRejectedErrorHandler);
    }

    default Capture<T> capture(Runner<T> onRejectedRunner, Consumer<RuntimeException> onRejectedErrorHandler) {
        return new Capture<>(this, onRejectedRunner, (obj, e) -> onRejectedErrorHandler.accept(e));
    }

}
