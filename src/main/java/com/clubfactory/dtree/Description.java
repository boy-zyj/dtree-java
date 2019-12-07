package com.clubfactory.dtree;

public interface Description {

    String getDescription();

    default void setDescription(String description) {
        throw new UnsupportedOperationException("setDescription not supported");
    }

}
