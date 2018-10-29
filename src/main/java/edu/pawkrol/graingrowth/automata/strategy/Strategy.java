package edu.pawkrol.graingrowth.automata.strategy;


import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;

import java.util.List;
import java.util.Random;

public abstract class Strategy {

    private Random random = new Random();

    protected int types;

    public abstract void init(Grid grid);
    public abstract void evaluate(Grid grid, Neighbourhood neighbourhood);
    public abstract void switchState(Cell cell);

    public boolean isFinished() {
        return false;
    }

    protected int getMostFrequentState(List<Cell> neighbours) {
        return getMostFrequentStateInBoundary(neighbours, 0, neighbours.size());
    }

    protected int getMostFrequentStateInBoundary(List<Cell> neighbours, int lower, int upper) {
        int[] freq = new int[types + 1];

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

        if (freq[mostState] >= lower && freq[mostState] <= upper) {
            return mostState;
        } else {
            return 0;
        }
    }

}
