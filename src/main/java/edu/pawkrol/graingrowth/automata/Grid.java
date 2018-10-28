package edu.pawkrol.graingrowth.automata;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Grid {

    private List<Cell> grid;
    private int width;
    private int height;

    private int maxPossibleStates;

    private boolean isCyclic;

    public Grid(int width, int height, boolean isCyclic) {
        this.width = width;
        this.height = height;
        this.isCyclic = isCyclic;

        initGrid();
    }

    public void forEach(Consumer<Cell> f){
        grid.forEach(f);
    }

    public List<Cell> getGrid() {
        return grid;
    }

    public void setGrid(List<Cell> grid) {
        this.grid = grid;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMaxPossibleStates() {
        return maxPossibleStates;
    }

    public void setMaxPossibleStates(int maxPossibleStates) {
        this.maxPossibleStates = maxPossibleStates;
    }

    public boolean isCyclic() {
        return isCyclic;
    }

    public void setCyclic(boolean cyclic) {
        isCyclic = cyclic;
    }

    public int getSize(){
        return width * height;
    }

    public void setCell(int x, int y, Cell cell){
        grid.set(y * width + x, cell);
    }

    public Cell getCell(int x, int y){
        if (isCyclic) {
            return getCellCyclic(x, y);
        } else {
            return getCellNormal(x, y);
        }
    }

    private Cell getCellCyclic(int x, int y){
        if (x >= width) x -= width;
        if (y >= height) y -= height;
        if (x < 0) x += width;
        if (y < 0) y += height;

        return getCellNormal(x, y);
    }

    private Cell getCellNormal(int x, int y){
        if (x >= width || y >= height) return null;
        if (x < 0 || y < 0) return null;

        return grid.get(y * width + x);
    }

    private void initGrid(){
        grid = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++){
                grid.add(new Cell(x, y));
            }
        }
    }

}
