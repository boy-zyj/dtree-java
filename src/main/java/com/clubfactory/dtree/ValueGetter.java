package com.clubfactory.dtree;

import java.util.function.Function;
import java.util.function.Predicate;

public class ValueGetter<IN, OUT> {

    private String desc;

    private Function<IN, OUT> getter;

    public ValueGetter(String desc, Function<IN, OUT> getter) {
        this.desc = desc;
        this.getter = getter;
    }

    public String getDescription() {
        return desc;
    }

    public OUT getOutput(IN in) {
        return getter.apply(in);
    }

    protected Condition<IN> toCondition(String description, Predicate<IN> predicate) {
        return new ToCondition<>(description, predicate);
    }

    public Condition<IN> eq(Comparable<OUT> other) {
        return toCondition(desc + "=" + other, in -> other.compareTo(getter.apply(in)) == 0);
    }

    public Condition<IN> eq(ValueGetter<IN, Comparable<OUT>> otherVallueGetter) {
        return toCondition(
                desc + "=" + otherVallueGetter.getDescription(),
                in -> otherVallueGetter.getOutput(in).compareTo(getter.apply(in)) == 0
        );
    }

    public Condition<IN> lt(Comparable<OUT> other) {
        return toCondition(desc + "<" + other, in -> other.compareTo(getter.apply(in)) > 0);
    }

    public Condition<IN> lt(ValueGetter<IN, Comparable<OUT>> otherValueGetter) {
        return toCondition(
                desc + "<" + otherValueGetter.getDescription(),
                in -> otherValueGetter.getOutput(in).compareTo(getter.apply(in)) > 0
        );
    }

    public Condition<IN> le(Comparable<OUT> other) {
        return toCondition(desc + "<=" + other, in -> other.compareTo(getter.apply(in)) >= 0);
    }

    public Condition<IN> le(ValueGetter<IN, Comparable<OUT>> otherValueGetter) {
        return toCondition(
                desc + "<=" + otherValueGetter.getDescription(),
                in -> otherValueGetter.getOutput(in).compareTo(getter.apply(in)) >= 0
        );
    }

    public Condition<IN> gt(Comparable<OUT> other) {
        return toCondition(desc + ">" + other, in -> other.compareTo(getter.apply(in)) < 0);
    }

    public Condition<IN> gt(ValueGetter<IN, Comparable<OUT>> otherValueGetter) {
        return toCondition(
                desc + ">" + otherValueGetter.getDescription(),
                in -> otherValueGetter.getOutput(in).compareTo(getter.apply(in)) < 0
        );
    }

    public Condition<IN> ge(Comparable<OUT> other) {
        return toCondition(desc + ">=" + other, in -> other.compareTo(getter.apply(in)) <= 0);
    }

    public Condition<IN> ge(ValueGetter<IN, Comparable<OUT>> otherValueGetter) {
        return toCondition(
                desc + ">=" + otherValueGetter.getDescription(),
                in -> otherValueGetter.getOutput(in).compareTo(getter.apply(in)) <= 0
        );
    }

    public Condition<IN> test(String desc, Predicate<IN> predicate) {
        return toCondition(desc, predicate);
    }

}
