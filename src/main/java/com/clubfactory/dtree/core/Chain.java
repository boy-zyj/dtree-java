package com.clubfactory.dtree.core;

public class Chain extends Runner {

    private Runner[] runners;

    public Chain(Runner... runners) {
        this.runners = runners;
    }

    @Override
    public void run(Center data) throws NoMatchException {
        for (Runner runner:runners) {
            runner.run(data);
        }
    }

    public Runner[] getRunners() {
        return runners;
    }

    @Override
    public String getDescription() {
        String[] descriptions = new String[runners.length];
        for (int i = 0; i < runners.length; i++) {
            descriptions[i] = runners[i].getDescription();
        }
        return String.join("==>", descriptions);
    }

}
