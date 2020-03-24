package com.clubfactory.dtree;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;


public class ValueAccessor<IN, OUT> {

    private String desc;

    private Function<IN, OUT> getter;

    public ValueAccessor(String desc, Function<IN, OUT> getter) {
        this.desc = desc;
        this.getter = getter;
    }

    public String getDescription() {
        return desc;
    }

    public OUT getRequiredValue(IN in) {
        OUT out = getter.apply(in);
        return Objects.requireNonNull(out, "Got null when getting value of " + desc);
    }

    public OUT getValue(IN in) {
        return getter.apply(in);
    }

    protected Condition<IN> toCondition(String description, Predicate<IN> predicate) {
        return new ToCondition<>(description, predicate);
    }

    public Condition<IN> eq(Comparable<OUT> other) {
        return toCondition(desc + "=" + other, in -> other.compareTo(getter.apply(in)) == 0);
    }

    public Condition<IN> eq(ValueAccessor<IN, Comparable<OUT>> otherValueAccessor) {
        return toCondition(
                desc + "=" + otherValueAccessor.getDescription(),
                in -> otherValueAccessor.getRequiredValue(in).compareTo(getter.apply(in)) == 0
        );
    }

    public Condition<IN> lt(Comparable<OUT> other) {
        return toCondition(desc + "<" + other, in -> other.compareTo(getter.apply(in)) > 0);
    }

    public Condition<IN> lt(ValueAccessor<IN, Comparable<OUT>> otherValueAccessor) {
        return toCondition(
                desc + "<" + otherValueAccessor.getDescription(),
                in -> otherValueAccessor.getRequiredValue(in).compareTo(getter.apply(in)) > 0
        );
    }

    public Condition<IN> le(Comparable<OUT> other) {
        return toCondition(desc + "<=" + other, in -> other.compareTo(getter.apply(in)) >= 0);
    }

    public Condition<IN> le(ValueAccessor<IN, Comparable<OUT>> otherValueAccessor) {
        return toCondition(
                desc + "<=" + otherValueAccessor.getDescription(),
                in -> otherValueAccessor.getRequiredValue(in).compareTo(getter.apply(in)) >= 0
        );
    }

    public Condition<IN> gt(Comparable<OUT> other) {
        return toCondition(desc + ">" + other, in -> other.compareTo(getter.apply(in)) < 0);
    }

    public Condition<IN> gt(ValueAccessor<IN, Comparable<OUT>> otherValueAccessor) {
        return toCondition(
                desc + ">" + otherValueAccessor.getDescription(),
                in -> otherValueAccessor.getRequiredValue(in).compareTo(getter.apply(in)) < 0
        );
    }

    public Condition<IN> ge(Comparable<OUT> other) {
        return toCondition(desc + ">=" + other, in -> other.compareTo(getter.apply(in)) <= 0);
    }

    public Condition<IN> ge(ValueAccessor<IN, Comparable<OUT>> otherValueAccessor) {
        return toCondition(
                desc + ">=" + otherValueAccessor.getDescription(),
                in -> otherValueAccessor.getRequiredValue(in).compareTo(getter.apply(in)) <= 0
        );
    }

    public Condition<IN> in(Collection<OUT> outs) {
        return toCondition(desc + " in " + outs, in -> outs.contains(getter.apply(in)));
    }

    public Condition<IN> in(ValueAccessor<IN, Collection<OUT>> outsValueAccessor) {
        return toCondition(
                desc + " in " + outsValueAccessor.getDescription(),
                in -> outsValueAccessor.getRequiredValue(in).contains(getter.apply(in))
        );
    }

    public Condition<IN> predicate(String desc, Predicate<IN> predicate) {
        return toCondition(desc, predicate);
    }

    public Condition<IN> isNull() {
        return toCondition(desc + " is null", in -> getter.apply(in) == null);
    }

    public Condition<IN> isNonNull() {
        return toCondition(desc + " is not null", in -> getter.apply(in) != null);
    }

}
