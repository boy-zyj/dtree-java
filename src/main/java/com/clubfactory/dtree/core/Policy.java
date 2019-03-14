package com.clubfactory.dtree.core;

public abstract class Policy {

    public abstract void runTree(DTree tree, Center data) throws NoMatchException;

}
