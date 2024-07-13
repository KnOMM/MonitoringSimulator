package org.development.sensor;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Nazar Zhuhan
 * @file SensorSet.java
 * @brief The class creates set of sensors data to be displayed by {@link org.development.gui.Display}.
 * @details There are 2 constructors with defined data and randomly generated data;
 */
public class SensorSet {
    private List<Sensor> sensors;
    private double shift;
    private int min;
    private int max;


    /**
     * Sets the character at the current position to the specified value
     *
     * @param sensors prepared sensors list of {@link Sensor}s
     * @param shift   shows deviation value
     * @param min     shows minimum value for bar graph when being displayed
     * @param max     shows maximum value for bar graph when being displayed
     */
    public SensorSet(List<Sensor> sensors, double shift, int min, int max) {
        this.sensors = sensors;
        this.shift = shift;
        this.min = min;
        this.max = max;
    }


    /**
     * Sets the character at the current position to the specified value
     *
     * @param size  size of randomly generated set of {@link Sensor}s
     * @param avg   average value for temperature
     * @param min   shows minimum value for bar graph when being displayed
     * @param max   shows maximum value for bar graph when being displayed
     */
    public SensorSet(int size, int avg,int min, int max) {
        sensors = new ArrayList<>();
        Sensor sensor;
        Random random = new Random();
        String location;
        BigDecimal temp;
        int multiplier;
        for (int i = 0; i < size; i++) {
            location = Location.values()[random.nextInt(Location.values().length)].getName();
            multiplier = random.nextInt(5);
            temp = new BigDecimal(avg + (random.nextBoolean() ? multiplier * shift : -1 * shift * multiplier));
            sensor = new Sensor(location, temp.doubleValue());
            sensors.add(sensor);
        }
        this.min = min;
        this.max = max;

    }


    public List<Sensor> getSensors() {
        return sensors;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
