package com.clubfactory.dtree.core;


public class And extends Condition {

    private Condition[] conditions;

    public And(Condition... conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean validate(Center center) {
        for (Condition condition:conditions) {
            if (!condition.validate(center)) {
                return false;
            }
        }
        return true;
    }

    public Condition[] getConditions() {
        return conditions;
    }

    @Override
    public String getDescription() {
        String[] descriptions = new String[conditions.length];
        for (int i = 0; i < conditions.length ; i++) {
            descriptions[i] = conditions[i].getDescription();
        }
        return "AND(" + String.join(",", descriptions) + ")";
    }
}
