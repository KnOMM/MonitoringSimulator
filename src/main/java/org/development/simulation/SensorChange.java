package org.development.simulation;

import org.development.sensor.Sensor;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


/**
 * @author Nazar Zhuhan
 * @file SensorChange.java
 * @brief The class is responsible for simulation of data taken from sensors
 */
public class SensorChange implements Runnable {

    boolean isOn = true;

    private List<Sensor> sensors;
    private double shift;

    /**
     *
     * @param sensors - sensors to update (for simulation purposes)
     * @param shift - the simulation difference
     */
    public SensorChange(List<Sensor> sensors, double shift) {
        this.sensors = sensors;
        this.shift = shift;
    }

    @Override
    public void run() {

        while (isOn) {
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
        Sensor sensor;
        for (int i = 0; i < sensors.size(); i++) {
            change = ThreadLocalRandom.current().nextBoolean();
            sensor = sensors.get(i);
            sensor.setTemperature(sensor.getTemperature() + (change ?
                    shift * ThreadLocalRandom.current().nextFloat(2) :
                    -1 * shift * ThreadLocalRandom.current().nextFloat(2)));
            sensors.set(i, sensor);
        }
    }

    public void terminate() {
        isOn = false;
    }
}
