package edu.pawkrol.graingrowth.automata.strategy;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;

import java.util.List;
import java.util.Random;

public class NaiveSeedGrowth implements Strategy {

    private int types;
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

        grid.setMaxPossibleStates(types);
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

    public int getTypes() {
        return types;
    }

    public int getNewTypes() {
        return ++types;
    }

    private boolean anyNeighbourIsSeed(List<Cell> neighbours) {
        return neighbours.stream().anyMatch(cell -> cell.getPreviousState() > 0);
    }

    public int getMostFrequentState(List<Cell> neighbours) {
        int[] freq = new int[types + 1];
        Random random = new Random();

        int max = 0;
        int mostState = 0;
        for (Cell c: neighbours) {
            int state = c.getPreviousState();

            if (state == -1) continue;

            freq[state]++;

            if (freq[state] > max && state != 0) {
                max = freq[state];
                mostState = state;
            } else if (freq[state] == max && state != 0) {
                if (random.nextDouble() > .5) {
                    max = freq[state];
                    mostState = state;
                }
            }
        }

        return mostState;
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
