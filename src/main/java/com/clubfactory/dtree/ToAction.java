package com.clubfactory.dtree;

import java.util.function.Consumer;


public class ToAction<T> extends AbstractRunner<T> {

    private Consumer<T> consumer;

    public ToAction(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public ToAction(String description, Consumer<T> consumer) {
        this(consumer);
        this.description = description;
    }

    @Override
    public void run(T data) {
        consumer.accept(data);
    }

}
