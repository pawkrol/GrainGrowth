package edu.pawkrol.graingrowth.automata.strategy;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.FurtherMoore;
import edu.pawkrol.graingrowth.automata.neighbourhood.Moore;
import edu.pawkrol.graingrowth.automata.neighbourhood.NearestMoore;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;

import java.util.List;
import java.util.Random;

public class ShapeControlSeedGrowth implements Strategy {

    private int types;
    private boolean finished;
    private boolean changed;

    private static ShapeControlSeedGrowth naiveSeedGrowth = null;

    private Moore moore;
    private NearestMoore nearestMoore;
    private FurtherMoore furtherMoore;

    private int probability = 0;

    private ShapeControlSeedGrowth() {
        moore = new Moore();
        nearestMoore = new NearestMoore();
        furtherMoore = new FurtherMoore();
    }

    @Override
    public void init(Grid grid) {
        types = 0;
        finished = false;
    }

    //Passed neighbourhood is not used in that case
    @Override
    public void evaluate(Grid grid, Neighbourhood n) {
        Grid prevGrid = new Grid(grid);
        Random random = new Random();

        changed = false;
        grid.forEach(c -> {
            if (c.getState() != 0) return;
            List<Cell> neighbours;
            int state;

            //Rule 1: Moore
            neighbours = moore.neighbours(prevGrid, c);
            state = getMostFrequentStateInBoundary(neighbours, 5, 8);
            if (state > 0) {
                c.setState(state);
                changed = true;
                return;
            }

            //Rule 2: Nearest Moore
            neighbours = nearestMoore.neighbours(prevGrid, c);
            state = getMostFrequentStateInBoundary(neighbours, 3, 4);
            if (state > 0) {
                c.setState(state);
                changed = true;
                return;
            }

            //Rule 3: Further Moore
            neighbours = furtherMoore.neighbours(prevGrid, c);
            state = getMostFrequentStateInBoundary(neighbours, 3, 4);
            if (state > 0) {
                c.setState(state);
                changed = true;
                return;
            }

            //Rule 4: Probability Moore
            neighbours = moore.neighbours(prevGrid, c);
            state = getMostFrequentStateInBoundary(neighbours, 1, 9);
            if (state > 0) {
                if ((random.nextInt(100) + 1) <= probability) {
                    c.setState(state);
                }
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

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    private boolean anyNeighbourIsSeed(List<Cell> neighbours) {
        return neighbours.stream().anyMatch(cell -> cell.getState() > 0);
    }

    @SuppressWarnings("Duplicates")
    public int getMostFrequentStateInBoundary(List<Cell> neighbours, int lower, int upper) {
        int[] freq = new int[types + 1];
        Random random = new Random();

        int max = 0;
        int mostState = 0;
        for (Cell c: neighbours) {
            int state = c.getState();

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

    public static ShapeControlSeedGrowth getInstance(){
        if (naiveSeedGrowth == null) {
            naiveSeedGrowth = new ShapeControlSeedGrowth();
        }

        return naiveSeedGrowth;
    }

    @Override
    public String toString() {
        return "Shape Control Growth";
    }
}
