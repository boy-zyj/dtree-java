package com.clubfactory.dtree;


public class Chain<T> extends AbstractRunner<T> {

    private Runner<T>[] runners;

    @SafeVarargs
    public Chain(Runner<T> ...runners) {
        this.runners = runners;
    }

    @Override
    public void run(T target) throws NoMatchException {
        for (Runner<T> runner:runners) {
            runner.run(target);
        }
    }

    @Override
    protected String getDefaultDescription() {
        String[] descriptions = new String[runners.length];
        for (int i = 0; i < runners.length; i++) {
            descriptions[i] = runners[i].getDescription();
        }
        return String.join("==>", descriptions);
    }

}
