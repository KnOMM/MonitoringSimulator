package org.development.gui;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import org.development.sensor.Sensor;
import org.development.sensor.SensorSet;
import org.development.sensor.Status;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Nazar Zhuhan
 * @file Display.java
 * @brief The class is responsible for rendering of data
 */
public class Display {

    SensorSet sensorSet;
    Screen screen;
    boolean isOn;
    TextColor background;
    TextColor mainColour;
    TextColor decoration;

    private int[] posX;
    private String[] labels = {"#", "LOCATION", "TEMPERATURE", "STATUS"};

    /**
     * Sets default colour scheme, initial positions for columns
     *
     * @param sensorSet list of sensors with additional information
     * @param background colour for background
     * @param mainColour main theme colour
     * @param decoration colour for decorative purposes
     */
    public Display(SensorSet sensorSet, TextColor background, TextColor mainColour, TextColor decoration) {
        initialize();

        this.sensorSet = sensorSet;
        isOn = true;
        this.background = background;
        this.mainColour = mainColour;
        this.decoration = decoration;
        posX = new int[4];
        changeX(screen.getTerminalSize());
    }

    /**
     * Sets default colour scheme and positions for columns
     *
     * @param sensorSet list of sensors with additional information
     */
    public Display(SensorSet sensorSet) {
        initialize();

        this.sensorSet = sensorSet;
        isOn = true;
        background = screen.newTextGraphics().getBackgroundColor();
        background = TextColor.ANSI.CYAN;
        decoration = TextColor.ANSI.GREEN;
        mainColour = screen.newTextGraphics().getForegroundColor();
        mainColour = TextColor.ANSI.MAGENTA;
        posX = new int[4];
        changeX(screen.getTerminalSize());
    }

    private void initialize() {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        try {
            screen = terminalFactory.createScreen();
        } catch (IOException e) {
            System.out.println("Error: " + e);
            System.exit(-1);
        }
    }

    public void display() {
        try {

            screen.startScreen();
            drawData();
            screen.stopScreen();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawData() throws IOException {
//        drawHeader("kjdf", "kjdlfkjd");
        while (isOn) {
            KeyStroke keyStroke;
            keyStroke = screen.pollInput();

            // Checking for Escape keystroke
            if (keyStroke != null && (keyStroke.getKeyType() == KeyType.Escape || keyStroke.getKeyType() == KeyType.EOF)) {
                isOn = false;
                continue;
            }

            // do resize logic if necessary
            TerminalSize newSize = screen.doResizeIfNecessary();
            TerminalSize terminalSize = screen.getTerminalSize();
            if (newSize != null) {
                screen.clear();
                drawHeader("MONITORING", "ESC: QUIT");
                terminalSize = newSize;
                changeX(terminalSize);
            }

            // draw rows and columns
            int rows = terminalSize.getRows();
            int graphSize = terminalSize.getColumns() - (50);
            if (graphSize < terminalSize.getColumns() / 3) {
                graphSize = terminalSize.getColumns() / 3;
            }
            int margin;

            int density = (int) Math.round((rows - 7) * 1.0 / (sensorSet.getSensors().size() + 1));
            margin = (rows - 7) - (density * (sensorSet.getSensors().size() - 1) + 4) + 4;


            double minTemp = sensorSet.getMin();
            double maxTemp = sensorSet.getMax();

            List<Sensor> sensors = sensorSet.getSensors();

            // rendering of rows
            for (int i = 0; i < sensors.size(); i++) {
                int y = 4 + i * density + margin / 2;
                drawEntry(sensors.get(i), graphSize, y, minTemp, maxTemp, i);
            }
            screen.refresh();

            // notification for CPU
            Thread.yield();
        }
    }


    private void drawHeader(String header, String footer) throws IOException {
        // Disabling cursor visibility
        screen.setCursorPosition(null);

        // Adjusting dimension values for zero indexes.
        int width = screen.getTerminalSize().getColumns() - 1;
        int height = screen.getTerminalSize().getRows() - 1;

        // Clearing the screen, creating header & footer with inverted colors.
        screen.clear();
        screen.newTextGraphics().drawLine(0, 0, width, 0, new TextCharacter(' ')
                .withBackgroundColor(mainColour).withForegroundColor(background));
        screen.newTextGraphics().drawLine(0, height, width, height, new TextCharacter(' ')
                .withBackgroundColor(mainColour).withForegroundColor(background));
        screen.newTextGraphics().setBackgroundColor(mainColour).setForegroundColor(background)
                .putCSIStyledString(0, 0, header);
        screen.newTextGraphics().setBackgroundColor(mainColour).setForegroundColor(background)
                .putCSIStyledString(0, height, "\u001b[1m" +footer + "\u001b[0m");

        // Using a key-value data structure for mapping data descriptions to screen.
        Map<Integer, String> colSetup = new HashMap<>();
        for (int i = 0; i < posX.length; i++) {
            colSetup.put(posX[i], labels[i]);
        }

        int row = 2;  // Row value is common

        // Iterating over HashMap and casting Lanterna Spell;
        colSetup.forEach((k, v) -> screen.newTextGraphics().putString(k, row, v));

        // Drawing the double line box
        screen.newTextGraphics().setForegroundColor(decoration).setCharacter(0, 1, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
        screen.newTextGraphics().drawLine(1, 1, width - 1, 1, new TextCharacter(Symbols.DOUBLE_LINE_HORIZONTAL)
                .withForegroundColor(decoration));
        screen.newTextGraphics().setForegroundColor(decoration).setCharacter(width, 1, Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
        screen.newTextGraphics().setForegroundColor(decoration).setCharacter(0, 2, Symbols.DOUBLE_LINE_VERTICAL);
        screen.newTextGraphics().setForegroundColor(decoration).setCharacter(width, 2, Symbols.DOUBLE_LINE_VERTICAL);


        screen.newTextGraphics().setForegroundColor(decoration).setCharacter(0, 3, Symbols.DOUBLE_LINE_T_RIGHT);
        screen.newTextGraphics().drawLine(1, 3, width - 1, 3, new TextCharacter(Symbols.DOUBLE_LINE_HORIZONTAL)
                .withForegroundColor(decoration));
        screen.newTextGraphics().setForegroundColor(decoration).setCharacter(width, 3, Symbols.DOUBLE_LINE_T_LEFT);

        screen.newTextGraphics().drawLine(0, 4, 0, height - 3, new TextCharacter(Symbols.DOUBLE_LINE_VERTICAL)
                .withForegroundColor(decoration));
        screen.newTextGraphics().drawLine(width, 4, width, height - 3, new TextCharacter(Symbols.DOUBLE_LINE_VERTICAL)
                .withForegroundColor(decoration));

        //DOUBLE_LINE_BOTTOM_LEFT_CORNER
        screen.newTextGraphics().setForegroundColor(decoration).setCharacter(0, height - 2, Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
        screen.newTextGraphics().drawLine(1, height - 2, width - 1, height - 2, new TextCharacter(Symbols.DOUBLE_LINE_HORIZONTAL)
                .withForegroundColor(decoration));
        screen.newTextGraphics().setForegroundColor(decoration).setCharacter(width, height - 2, Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);

        // Highest temperature statistic
        screen.newTextGraphics().putString(posX[0], height - 1, "Highest Temperature Record: ");

        // Finally, refreshing the screen to make our changes visible.
        screen.refresh();
    }

    private void drawEntry(Sensor sensor, int graphSize, int y, double min, double max, int count) {

        if (y < screen.getTerminalSize().getRows() - 3) {

            screen.newTextGraphics().setForegroundColor(mainColour).putCSIStyledString(posX[0], y, "" + (count + 1));
            screen.newTextGraphics().setForegroundColor(TextColor.ANSI.WHITE).putString(posX[1], y, sensor.getLocation());
            double temp = Math.round(sensor.getTemperature() * 10) / 10.0;
            // right aligned x value
            int x = posX[2] + 6 - ("" + temp).length();
            screen.newTextGraphics().setBackgroundColor(TextColor.ANSI.DEFAULT).putString(posX[2], y, "      ");
            screen.newTextGraphics().setForegroundColor(decoration).putString(x, y, temp + "");
            screen.newTextGraphics().setCharacter(1 + x + ("" + temp).length(), y, '[');
            screen.newTextGraphics().drawLine(2 + x + ("" + temp).length(), y, 2 + x + ("" + temp).length() + graphSize, y, '-');

            // length of the graph bar
            int fillSize = (int) ((sensor.getTemperature() - min) * graphSize / (max - min));
            if (fillSize > graphSize) {
                fillSize = graphSize;
            } else if (fillSize < min) {
                fillSize = 0;
            }

            if (fillSize > 0)
                screen.newTextGraphics().setBackgroundColor(mainColour).drawLine(2 + x + ("" + temp).length(), y, x + ("" + temp).length() + 3 + fillSize, y, ' ');
            screen.newTextGraphics().setCharacter(2 + x + ("" + temp).length() + graphSize + 1, y, ']');

            // setting colour for the state column
            Status statusColour ;
            if (temp < min) {
statusColour = Status.COLD;
            }
            else if (temp > max) {
                statusColour = Status.HOT;
            }
            else{

                int tempPer = (int) (6 * temp / Math.abs(max - min));
                switch (tempPer) {
                    case 0:
                        statusColour = Status.COLD;
                        break;
                    case 1:
                        statusColour = Status.COOL;
                        break;
                    case 2:
                        statusColour = Status.NORMAL;
                        break;
                    case 3:
                        statusColour = Status.WARM;
                        break;
                    case 4:
                        statusColour = Status.HOT;
                        break;
                    default:
                        statusColour = Status.COLD;
                        break;
                }
            }

            screen.newTextGraphics().setForegroundColor(statusColour.getColor()).putString(posX[3], y,"      ");
            screen.newTextGraphics().setForegroundColor(statusColour.getColor()).putString(posX[3], y, statusColour.toString());

        }

    }

    /**
     *
     * @param terminalSize - passed in size of terminal
     */
    private void changeX(TerminalSize terminalSize) {
        int graphLength = terminalSize.getColumns() - (35);
        posX[0] = 3;
        posX[1] = 8;
        posX[2] = 25;
        if (graphLength + 25 < 70) {
            posX[3] = 70;
        } else {
            posX[3] = 25 + graphLength;
        }
    }
}
