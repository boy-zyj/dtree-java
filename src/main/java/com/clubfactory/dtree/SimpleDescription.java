package com.clubfactory.dtree;

public class SimpleDescription implements Description {

    protected String description;

    @Override
    public String getDescription() {
        if (description == null) {
            description = getDefaultDescription();
        }
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    protected String getDefaultDescription() {
        return getClass().getSimpleName();
    }

}
