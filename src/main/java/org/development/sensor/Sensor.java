package org.development.sensor;

import java.math.BigDecimal;


/**
 * @author Nazar Zhuhan
 * @file Sensor.java
 * @brief The class is a blueprint of sensor instance.
 */
public class Sensor {
    String location;
    double temperature;

    public Sensor(String location, double temperature) {
        this.location = location;
        this.temperature = temperature;
    }

    public String getLocation() {
        return location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
