package edu.pawkrol.graingrowth.automata.strategy;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.FurtherMoore;
import edu.pawkrol.graingrowth.automata.neighbourhood.Moore;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;
import edu.pawkrol.graingrowth.automata.neighbourhood.VonNeumann;

import java.util.List;
import java.util.Random;

public class ShapeControlSeedGrowth extends Strategy {

    private boolean finished;
    private boolean changed;

    private static ShapeControlSeedGrowth naiveSeedGrowth = null;

    private Moore moore;
    private VonNeumann vonNeumann;
    private FurtherMoore furtherMoore;

    private int probability = 0;

    private ShapeControlSeedGrowth() {
        moore = new Moore();
        vonNeumann = new VonNeumann();
        furtherMoore = new FurtherMoore();
    }

    @Override
    public void init() {
        finished = false;
    }

    //Passed neighbourhood is not used in that case
    //SLOW...
    @Override
    public void evaluate(Grid grid, Neighbourhood n) {
        Random random = new Random();

        changed = false;
        grid.forEach(Cell::updatePreviousState);
        grid.forEach(c -> {
            if (c.getCurrentState() != 0 || c.isLocked()) return;

            List<Cell> neighbours;
            int state;

            //Rule 1: Moore
            neighbours = moore.neighbours(grid, c);
            state = getMostFrequentStateInBoundary(neighbours, 5, 8);
            if (state > 0) {
                c.setCurrentState(state);
                changed = true;
                return;
            }

            //Rule 2: Von Neumann
            neighbours = vonNeumann.neighbours(grid, c);
            state = getMostFrequentStateInBoundary(neighbours, 3, 4);
            if (state > 0) {
                c.setCurrentState(state);
                changed = true;
                return;
            }

            //Rule 3: Further Moore
            neighbours = furtherMoore.neighbours(grid, c);
            state = getMostFrequentStateInBoundary(neighbours, 3, 4);
            if (state > 0) {
                c.setCurrentState(state);
                changed = true;
                return;
            }

            //Rule 4: Probability Moore
            neighbours = moore.neighbours(grid, c);
            state = getMostFrequentStateInBoundary(neighbours, 1, 9);
            if (state > 0) {
                if ((random.nextInt(100) + 1) <= probability) {
                    c.setCurrentState(state);
                }
                changed = true;
            }
        });

        if (!changed) finished = true;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
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
