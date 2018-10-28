package edu.pawkrol.graingrowth.utils;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.HashMap;

public class GridExporter {

    public static final String SEPARATOR = ";";

    public static void exportGridText(Window window, Grid grid) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Save File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt")
        );
        File file = fileChooser.showSaveDialog(window);

        if (file == null) return;

        StringBuilder gridText = new StringBuilder();

        gridText.append(grid.getWidth())
                .append(SEPARATOR)
                .append(grid.getHeight())
                .append(SEPARATOR)
                .append(grid.isCyclic())
                .append("\n");

        grid.forEach(cell ->
            gridText.append(cell.getX())
                    .append(SEPARATOR)
                    .append(cell.getY())
                    .append(SEPARATOR)
                    .append(cell.getCurrentState())
                    .append("\n")
        );

        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(gridText.toString());
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Grid importGridText(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Import File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt")
        );
        File file = fileChooser.showOpenDialog(window);

        if (file == null) return null;

        Grid grid = null;

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String currentLine = br.readLine();
            String[] tokens = currentLine.split(SEPARATOR);

            grid = new Grid(
                    Integer.parseInt(tokens[0]),
                    Integer.parseInt(tokens[1]),
                    Boolean.parseBoolean(tokens[2])
            );

            while ((currentLine = br.readLine()) != null) {
                tokens = currentLine.split(SEPARATOR);
                Cell cell = new Cell(
                        Integer.parseInt(tokens[0]),
                        Integer.parseInt(tokens[1])
                );
                cell.setCurrentState(Integer.parseInt(tokens[2]));

                grid.setCell(
                        Integer.parseInt(tokens[0]),
                        Integer.parseInt(tokens[1]),
                        cell
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return grid;
    }

    public static void exportGridBitmap(Window window, Grid grid) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Save File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.png")
        );
        File file = fileChooser.showSaveDialog(window);

        if (file == null) return;

        int width = grid.getWidth();
        int height = grid.getHeight();

        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        grid.forEach(c -> {
            int x = (int) Math.floor( c.getX() );
            int y = (int) Math.floor( c.getY() );

            Color color = ColorHelper.getColor(c.getCurrentState());

            pixelWriter.setColor(width - x - 1, height - y - 1, color);
        });

        RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);

        try {
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Grid importGridBitmap(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Import File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.png")
        );
        File file = fileChooser.showOpenDialog(window);

        if (file == null) return null;

        Grid grid;

        HashMap<Color, Integer> stateMap = new HashMap<>();
        stateMap.put(Color.BLACK, -1);
        stateMap.put(Color.WHITE, 0);
        int lastState = 0;

        try {
            BufferedImage image = ImageIO.read(file);
            grid = new Grid(image.getWidth(), image.getHeight(), false);

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    int b = rgb & 0xFF;
                    int g = (rgb & 0xFF) >> 8;
                    int r = (rgb & 0xFF) >> 16;

                    double blue = (double) b / 255.0;
                    double green = (double) g / 255.0;
                    double red = (double) r / 255.0;

                    Color color = Color.color(red, green, blue);

                    Integer state = stateMap.get(color);
                    if (state == null) {
                        stateMap.put(color, ++lastState);
                        state = lastState;
                    }

                    Cell cell = new Cell(x, y);
                    cell.setCurrentState(state);

                    grid.setCell(x, y, cell);
                }
            }

            return grid;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
