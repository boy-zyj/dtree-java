package com.clubfactory.dtree;

import java.util.function.BiConsumer;


public class Capture<T> extends AbstractRunner<T> {

    private Runner<T> previous;
    private Runner<T> onRejectedRunner;
    private BiConsumer<T, RuntimeException> onRejectedErrorHandler;

    @Override
    public void run(T target) throws NoMatchException {
        try {
            previous.run(target);
        } catch (RuntimeException ex) {
            try {
                if (onRejectedRunner != null) {
                    onRejectedRunner.run(target);
                }
            } catch (RuntimeException ex1) {
                ex.addSuppressed(ex1);
            }
            if (onRejectedErrorHandler == null) {
                throw ex;
            } else {
                onRejectedErrorHandler.accept(target, ex);
            }
        }
    }

    public Capture(Runner<T> previous, Runner<T> onRejectedRunner) {
        this.previous = previous;
        this.onRejectedRunner = onRejectedRunner;
    }

    public Capture(Runner<T> previous, BiConsumer<T, RuntimeException> onRejectedErrorHandler) {
        this.previous = previous;
        this.onRejectedErrorHandler = onRejectedErrorHandler;
    }

    public Capture(Runner<T> previous, Runner<T> onRejectedRunner, BiConsumer<T, RuntimeException> onRejectedErrorHandler) {
        this.previous = previous;
        this.onRejectedRunner = onRejectedRunner;
        this.onRejectedErrorHandler = onRejectedErrorHandler;
    }

    @Override
    protected String getDefaultDescription() {
        StringBuilder sb = new StringBuilder("CAPTURE");
        if (onRejectedRunner != null) {
            sb.append("->");
            sb.append(onRejectedRunner.getDescription());
        }
        if (onRejectedErrorHandler == null) {
            sb.append("->");
            sb.append("THROW");
        } else {
            sb.append("->");
            sb.append("HANDLE");
        }
        return sb.toString();
    }

}
