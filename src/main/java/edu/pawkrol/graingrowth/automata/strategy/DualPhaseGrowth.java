package edu.pawkrol.graingrowth.automata.strategy;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//ITS NOT STRATEGY!
public class DualPhaseGrowth extends Strategy {

    private boolean finished;

    private int toKeep;
    private Grid grid;

    private static DualPhaseGrowth dualPhaseGrowth = null;

    private DualPhaseGrowth() {}

    @Override
    public void init(Grid grid) {
        this.types = grid.getMaxPossibleStates();
        finished = false;
    }

    @Override
    public void evaluate(Grid grid, Neighbourhood neighbourhood) {
        this.grid = grid;

        grid.forEach(Cell::updatePreviousState);
        keepGrains();

        finished = true;
    }

    @Override
    public void switchState(Cell cell) {
        if (cell.getCurrentState() == 0) {
            int newType = getNewTypes();
            cell.setCurrentState(newType);
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public int getNewTypes() {
        return ++types;
    }

    public void setAmountToKeep(int toKeep) {
        this.toKeep = toKeep;
    }

    public static DualPhaseGrowth getInstance(){
        if (dualPhaseGrowth == null) {
            dualPhaseGrowth = new DualPhaseGrowth();
        }

        return dualPhaseGrowth;
    }

    private void keepGrains() {
        Random random = new Random();
        int[] statesToKeep = random
                .ints(toKeep, 1, grid.getMaxPossibleStates())
                .toArray();

        grid.forEach(c -> {
            if (isInArray(statesToKeep, c.getCurrentState())) {
                c.setCurrentState(1);
            } else {
                c.setCurrentState(0);
            }
        });
    }

    private boolean isInArray(int[] array, int key) {
        for (int a : array) {
            if (a == key) return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Dual Phase Growth";
    }
}
