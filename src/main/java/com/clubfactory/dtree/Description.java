package com.clubfactory.dtree;

public interface Description {

    default String getDescription() {
        return getClass().getSimpleName();
    }

    default void setDescription(String description) {

    }

}
