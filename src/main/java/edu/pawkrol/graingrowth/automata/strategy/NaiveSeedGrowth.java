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
        Grid prevGrid = new Grid(grid);

        changed = false;
        grid.forEach(c -> {
            List<Cell> neighbours = neighbourhood.neighbours(prevGrid, c);

            if (c.getState() == 0 && anyNeighbourIsSeed(neighbours)) {
                int state = getMostFrequentState(neighbours);
                c.setState(state);
                changed = true;
            }
        });

        if (!changed) finished = true;

        grid.setMaxPossibleStates(types);
    }

    @Override
    public void switchState(Cell cell) {
        if (cell.getState() == 0) {
            cell.setState(getNewTypes());
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
        return neighbours.stream().anyMatch(cell -> cell.getState() != 0);
    }

    public int getMostFrequentState(List<Cell> neighbours) {
        int[] freq = new int[types + 1];
        Random random = new Random();

        int max = 0;
        int mostState = 0;
        for (Cell c: neighbours) {
            int state = c.getState();

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
