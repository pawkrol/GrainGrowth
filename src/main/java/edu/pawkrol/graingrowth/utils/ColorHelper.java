package edu.pawkrol.graingrowth.utils;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class ColorHelper {

    private static Map<Integer, Color> colorMap = new HashMap<>();

    static {
        colorMap.put(-1, Color.BLACK);
        colorMap.put(0, Color.WHITE);
    }

    public static Color getColor(int value){
        Color color = colorMap.get(value);

        if (color == null) {
            color = randomColor();
            setColor(value, color);
        }

        return color;
    }

    public static void setColor(int value, Color color) {
        colorMap.put(value, color);
    }

    private static Color randomColor(){
        return Color.color(Math.random(), Math.random(), Math.random());
    }
}
