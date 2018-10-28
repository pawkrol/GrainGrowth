package edu.pawkrol.graingrowth.utils;

import edu.pawkrol.graingrowth.automata.Grid;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GridPainter {

    private Canvas canvas;
    private GraphicsContext gc;
    private Grid grid;

    private double xOffset;
    private double yOffset;
    private double cellSize;

    private boolean gridOverlayVisible = false;

    public GridPainter(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public boolean isGridOverlayVisible() {
        return gridOverlayVisible;
    }

    public void setGridOverlayVisible(boolean gridOverlayVisible) {
        this.gridOverlayVisible = gridOverlayVisible;
    }

    public void init(Grid grid) {
        this.grid = grid;

        initGrid();
    }

    public void paint() {
        if (grid == null) return;

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawCells();

        if (gridOverlayVisible) {
            drawGridOverlay();
        }
    }

    public void onResize() {
        if (grid == null || canvas == null) return;

        initGrid();
        paint();
    }

    private void drawCells(){
        grid.forEach(c -> {
            gc.setFill(ColorHelper.getColor(c.getCurrentState()));
            gc.fillRect(
                    xOffset + c.getX() * cellSize,
                    yOffset + c.getY() * cellSize,
                    cellSize,
                    cellSize
            );
        });
    }

    private void drawGridOverlay(){
        //horizontal lines
        for (int y = 0; y < grid.getHeight() + 1; y++){
            gc.strokeLine(
                    xOffset,
                    yOffset + snap(y * cellSize),
                    xOffset + grid.getWidth() * cellSize,
                    yOffset + snap(y * cellSize)
            );
        }

        //vertical lines
        for (int x = 0; x < grid.getWidth() + 1; x++) {
            gc.strokeLine(
                    xOffset + snap(x * cellSize),
                    yOffset,
                    xOffset + snap(x * cellSize),
                    yOffset + grid.getHeight() * cellSize
            );
        }
    }

    private double snap(double y) {
        return y;//((int) y) + .5;
    }

    private void initGrid(){
        int gw = grid.getWidth();
        int gh = grid.getHeight();
        double cw = Math.floor(canvas.getWidth() / gw);
        double ch = Math.floor(canvas.getHeight() / gh);

        xOffset = 0;
        yOffset = 0;

        cellSize = cw < ch? cw : ch;

        yOffset = Math.floor(canvas.getHeight() / 2 - (gh * cellSize) / 2);
        xOffset = Math.floor(canvas.getWidth() / 2 - (gw * cellSize) / 2);
    }

}
