package com.clubfactory.dtree;

import java.util.Objects;
import java.util.function.Consumer;


public class ToAction<T> extends Action<T> {

    private Consumer<T> consumer;

    private Runner<T> runner;

    public ToAction(Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "consumer cannot be null");
        this.consumer = consumer;
    }

    public ToAction(String description, Consumer<T> consumer) {
        this(consumer);
        this.description = description;
    }

    public ToAction(String description, Runner<T> runner) {
        Objects.requireNonNull(runner, "runner cannot be null");
        this.runner = runner;
        this.description = description;
    }

    public ToAction(Runner<T> runner) {
        this(runner.getDescription(), runner);
    }

    @Override
    public void run(T target) {
        if (consumer == null) {
            runner.run(target);
        } else {
            consumer.accept(target);
        }
    }

}
