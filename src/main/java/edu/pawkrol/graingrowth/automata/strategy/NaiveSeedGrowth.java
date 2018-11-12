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
    public void init() {
        finished = false;
    }

    @Override
    public void evaluate(Grid grid, Neighbourhood neighbourhood) {
        changed = false;

        grid.forEach(Cell::updatePreviousState);
        grid.forEach(c -> {
            if (c.getCurrentState() != 0 || c.isLocked()) return;

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
    public boolean isFinished() {
        return finished;
    }

    private boolean anyNeighbourIsSeed(List<Cell> neighbours) {
        return neighbours.stream()
                .filter(cell -> !cell.isLocked())
                .anyMatch(cell -> cell.getPreviousState() > 0);
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
