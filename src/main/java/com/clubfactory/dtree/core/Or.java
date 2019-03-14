package com.clubfactory.dtree.core;


public class Or extends Condition {

    protected Condition[] conditions;

    public Or(Condition... conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean validate(Center data) {
        for (Condition condition:conditions) {
            if (condition.validate(data)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        String[] descriptions = new String[conditions.length];
        for (int i = 0; i < conditions.length ; i++) {
            descriptions[i] = conditions[i].getDescription();
        }
        return "OR(" + String.join(",", descriptions) + ")";
    }

}
