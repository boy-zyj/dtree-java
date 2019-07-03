package com.clubfactory.dtree;

public class Descriptor {

    protected String description;

    public String getDescription() {
        if (description == null) {
            return getClass().getSimpleName();
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
