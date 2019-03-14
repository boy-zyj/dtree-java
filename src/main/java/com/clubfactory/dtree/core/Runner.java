package com.clubfactory.dtree.core;

public abstract class Runner extends Descriptor {

    public abstract void run(Center data) throws NoMatchException;

    public Chain then(Runner runner) {
        return new Chain(this, runner);
    }

}
