package org.development.simulation;

import java.util.concurrent.ThreadLocalRandom;

public class SensorChange implements Runnable {

    private double[] values;

    public SensorChange(double[] values) {
        this.values = values;
    }

    @Override
    public void run() {

        while (true) {
            simulate();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void simulate() {

        boolean change;
        for (int i = 0; i < values.length; i++) {
            change = ThreadLocalRandom.current().nextBoolean();
            values[i] += change ? 1 : -1;
        }
    }
}
