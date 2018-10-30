package edu.pawkrol.graingrowth.automata;

import java.util.Objects;

public class Cell {

    private int x;
    private int y;

    private int previousState;
    private int currentState;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPreviousState() {
        return previousState;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setPreviousState(int previousState) {
        this.previousState = previousState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public void updatePreviousState() {
        previousState = currentState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x &&
                y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
