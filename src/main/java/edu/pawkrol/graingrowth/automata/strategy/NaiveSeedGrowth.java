package edu.pawkrol.graingrowth.automata.strategy;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;

import java.util.List;

public class NaiveSeedGrowth extends Strategy {

    private boolean finished;
    private boolean changed;

    private static NaiveSeedGrowth naiveSeedGrowth = null;

    private NaiveSeedGrowth() {}

    @Override
    public void init(Grid grid) {
        types = 0;
        finished = false;
    }

    @Override
    public void evaluate(Grid grid, Neighbourhood neighbourhood) {
        changed = false;

        grid.forEach(Cell::updatePreviousState);
        grid.forEach(c -> {
            if (c.getCurrentState() != 0) return;

            List<Cell> neighbours = neighbourhood.neighbours(grid, c);

            if (anyNeighbourIsSeed(neighbours)) {
                int state = getMostFrequentState(neighbours);
                c.setCurrentState(state);
                changed = true;
            }
        });

        if (!changed) finished = true;
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

    private boolean anyNeighbourIsSeed(List<Cell> neighbours) {
        return neighbours.stream().anyMatch(cell -> cell.getPreviousState() > 0);
    }

    public static NaiveSeedGrowth getInstance(){
        if (naiveSeedGrowth == null) {
            naiveSeedGrowth = new NaiveSeedGrowth();
        }

        return naiveSeedGrowth;
    }

    @Override
    public String toString() {
        return "Naive Seed Growth";
    }
}
