package org.development;

import com.googlecode.lanterna.TextColor;
import org.development.gui.Display;
import org.development.sensor.SensorSet;
import org.development.simulation.SensorChange;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world!");

        double[] values = {1.2, 2.4, 9.8, -2.5, 6.4, 10.2, 7, 4, 3.8};
        SensorSet sensorSet = new SensorSet(10,20, -10,50);
        SensorChange sensorChange = new SensorChange(sensorSet.getSensors(), 2.3);
        Thread sensorsThread = new Thread(sensorChange);
        sensorsThread.start();

//        Senso




        Display display = new Display(sensorSet);
        display = new Display(sensorSet, TextColor.ANSI.CYAN, TextColor.ANSI.BLUE, TextColor.ANSI.YELLOW);
        display.display();
        sensorChange.terminate();
    }
}