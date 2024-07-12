package org.development;

import org.development.gui.Display;
import org.development.simulation.SensorChange;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world!");

        double[] values = {1.2, 2.4, 9.8, -2.5, 6.4, 10.2, 7, 4, 3.8};
        SensorChange sensorChange = new SensorChange(values);
        Thread sensorsThread = new Thread(sensorChange);
        sensorsThread.start();


//        while (true) {
//            System.out.println(Arrays.toString(values));
//            Thread.sleep(500);
//        }


        Display display = new Display();
        display.show(values);
        sensorsThread.interrupt();
    }
}