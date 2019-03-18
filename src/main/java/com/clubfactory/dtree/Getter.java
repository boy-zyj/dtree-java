package com.clubfactory.dtree;

@FunctionalInterface
public interface Getter<INPUT, OUTPUT> {

    public OUTPUT get(INPUT input);

}
