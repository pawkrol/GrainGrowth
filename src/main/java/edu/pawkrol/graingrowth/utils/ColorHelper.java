package edu.pawkrol.graingrowth.utils;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class ColorHelper {

    private static Map<Integer, Color> colorMap = new HashMap<>();
    private static Map<Integer, Color> energyColorMap = new HashMap<>();

    private static boolean colorsInverted = false;

    private static float redFactor = 0f;
    private static float greenFactor = 0.1f;
    private static float blueFactor = 1f;

    static {
        colorMap.put(-2, Color.color(1, 0, 1));
        colorMap.put(-1, Color.BLACK);
        colorMap.put(0, Color.WHITE);

        energyColorMap.put(0, Color.color(0, 0, 0.2));
        energyColorMap.put(1, Color.color(0, 0, 0.4));
        energyColorMap.put(2, Color.color(0, 0, 0.6));
        energyColorMap.put(3, Color.color(0, 0.2, 0.8));
        energyColorMap.put(4, Color.color(0, 0.4, 0.6));
        energyColorMap.put(5, Color.color(0, 0.6, 0.4));
        energyColorMap.put(6, Color.color(0, 0.8, 0.2));
        energyColorMap.put(7, Color.color(0, 1, 0));
    }

    public static Color getColor(int value){
        Color color = colorMap.get(value);

        if (color == null) {
            color = randomColor();
            setColor(value, color);
        }

        return color;
    }

    public static Color getEnergyColor(int value) {
        return energyColorMap.get(value);
    }

    public static void setColor(int value, Color color) {
        colorMap.put(value, color);
    }

    public static void inverseColorSpectrum() {
        colorsInverted = !colorsInverted;

        if (colorsInverted) {
            redFactor = 1.f;
            greenFactor = 0.1f;
            blueFactor = 0.f;
        } else {
            redFactor = 0f;
            greenFactor = 1f;
            blueFactor = 1f;
        }
    }

    private static Color randomColor(){
        Color c = Color.color(
                Math.random() * redFactor,
                Math.random() * greenFactor,
                Math.random() * blueFactor
        );
        if (colorMap.containsValue(c)) {
            return randomColor();
        }

        return c;
    }
}
