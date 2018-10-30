package edu.pawkrol.graingrowth.utils;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;

public class GridPainter {

    private static final int HOVERED_STATE = -3;

    private Canvas canvas;
    private GraphicsContext gc;
    private Grid grid;

    private double xOffset;
    private double yOffset;
    private double cellSize;

    private boolean gridOverlayVisible = false;
    private boolean gridSelectionEnabled = false;

    private int hoveredState = HOVERED_STATE;
    private List<Integer> selectedStates = new ArrayList<>();

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

    public boolean isGridSelectionEnabled() {
        return gridSelectionEnabled;
    }

    public Observer<Integer> setGridSelectionEnabled(boolean gridSelectionEnabled) {
        this.gridSelectionEnabled = gridSelectionEnabled;
        this.selectedStates.clear();
        this.hoveredState = HOVERED_STATE;

        if (gridSelectionEnabled) {
            return setGrainSelectorListener();
        }

        paint();

        return null;
    }

    private void drawCells(){
        grid.forEach(c -> {
            double alpha = 1;
            if (hoveredState == -2 || hoveredState > 0 || !selectedStates.isEmpty()) {
                int state = c.getCurrentState();
                boolean isSelected = selectedStates.contains(state) || c.getCurrentState() == hoveredState;
                alpha = isSelected? alpha : 0.25;
            }

            gc.setGlobalAlpha(alpha);
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
        return ((int) y) + .5;
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

    private Observer<Integer> setGrainSelectorListener() {
        canvas.setOnMouseMoved(event -> {
            if (!gridSelectionEnabled) return;

            double x = event.getX();
            double y = event.getY();

            Cell c = getCellFromCoords(x, y);

            if (c == null) {
                hoveredState = HOVERED_STATE;
                paint();
                return;
            }

            if (hoveredState != c.getCurrentState()) {
                hoveredState = c.getCurrentState();
                paint();
            }
        });

        Observer<Integer> observer = new Observer<>();
        canvas.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;
            if (!gridSelectionEnabled) return;

            double x = event.getX();
            double y = event.getY();

            Cell c = getCellFromCoords(x, y);
            if (c != null) {
                if (selectedStates.contains(c.getCurrentState())) return;

                selectedStates.add(c.getCurrentState());
                observer.emit(c.getCurrentState());
            }
        });

        return observer;
    }

    private Cell getCellFromCoords(double x, double y) {
        if ((x >= xOffset && x < xOffset + grid.getWidth() * cellSize)
                && (y >= yOffset && y < yOffset + grid.getHeight() * cellSize)) {
            x = Math.floor((x - xOffset) / cellSize);
            y = Math.floor((y - yOffset) / cellSize);

            return grid.getCell((int) x, (int) y);
        }

        return null;
    }

}
