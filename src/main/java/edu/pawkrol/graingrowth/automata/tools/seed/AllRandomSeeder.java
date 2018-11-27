package edu.pawkrol.graingrowth.automata.tools.seed;

import edu.pawkrol.graingrowth.automata.Grid;

import java.util.Random;

public class AllRandomSeeder implements Seeder{

    @Override
    public void seed(Grid grid, int... args) {
        int maxStates = args[0];

        Random random = new Random();
        grid.forEach(c -> {
            if (c.getCurrentState() == 0) {
                c.setCurrentState(random.nextInt(maxStates) + 1);
            }
        });

        grid.setNumberOfStates(maxStates);
    }

    @Override
    public String toString() {
        return "All Random";
    }
}
