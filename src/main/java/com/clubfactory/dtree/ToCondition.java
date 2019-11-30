package com.clubfactory.dtree;

import java.util.function.Predicate;


public class ToCondition<T> extends AbstractCondtion<T> {

    private Predicate<T> validator;

    public ToCondition(Predicate<T> validator) {
        this.validator = validator;
    }

    public ToCondition(String description, Predicate<T> validator) {
        this(validator);
        this.description = description;
    }

    @Override
    public boolean validate(T target) {
        return validator.test(target);
    }

}
