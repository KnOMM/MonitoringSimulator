package org.development.gui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Display {
    Screen screen;
    boolean isOn;


    public void show(double[] values) {

        screen = null;
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();

        try {
            screen = terminalFactory.createScreen();
            screen.startScreen();
            isOn = true;


            drawData(values);
//            screen.newTextGraphics()


            screen.stopScreen();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawData(double[] values) {

        while (isOn) {
            KeyStroke keyStroke;
            try {
                keyStroke = screen.pollInput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (keyStroke != null && (keyStroke.getKeyType() == KeyType.Escape || keyStroke.getKeyType() == KeyType.EOF)) {
                isOn = false;
                continue;
            }
            TerminalSize newSize = screen.doResizeIfNecessary();
            TerminalSize terminalSize = screen.getTerminalSize();
            if (newSize != null) {
                screen.clear();
                terminalSize = newSize;
            }

            int rows = terminalSize.getRows();
            int graphSize = terminalSize.getColumns() / 3;
            int density = (int) (rows * 1.0 / values.length);

            double minTemp = min(values);
            double maxTemp = max(values);

//            System.out.println("/////////////////");
            for (int i = 0; i < values.length; i++) {
                int y = density / 2 + values.length / 3 + i * density;
                BigDecimal temp = BigDecimal.valueOf(values[i]).setScale(1, RoundingMode.HALF_UP);
                int x = 10;

                int fillSize = (int) ((values[i] - minTemp) * graphSize / (maxTemp - minTemp));
//                System.out.println(fillSize);

                screen.newTextGraphics().setForegroundColor(TextColor.ANSI.RED).setBackgroundColor(TextColor.ANSI.GREEN)
                        .putCSIStyledString(x - temp.toString().length(), y, temp.toString());

                screen.newTextGraphics().drawLine(x + 2, y, x + 2 + graphSize, y, '0');
                if (fillSize != 0) screen.newTextGraphics().drawLine(x + 2, y, x + 2 + fillSize, y, '/');
                screen.newTextGraphics().setCharacter(x + 1, y, '[');
                screen.newTextGraphics().setCharacter(x + 3 + graphSize, y, ']');
            }

            try {
                screen.refresh();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Thread.yield();
        }
    }

    private double min(double[] array) {
        double min = array[0];

        for (int i = 0; i < array.length; i++) {
            if (min > array[i]) {
                min = array[i];
            }
        }
        return min;
    }

    private double max(double[] array) {
        double max = array[0];

        for (int i = 0; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }
        return max;
    }
}
