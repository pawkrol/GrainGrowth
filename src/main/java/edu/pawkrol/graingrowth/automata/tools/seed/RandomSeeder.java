package edu.pawkrol.graingrowth.automata.tools.seed;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;

import java.util.Random;

public class RandomSeeder implements Seeder {

    @Override
    public void seed(Grid grid, int... args) {
        int n = args[0];

        int numberOfStates = grid.getNumberOfStates();
        Random random = new Random();

        int x, y;
        Cell c;
        for (int i = 0; i < n;) {
            x = random.nextInt(grid.getWidth());
            y = random.nextInt(grid.getHeight());
            c = grid.getCell(x, y);

            if (c.getCurrentState() == 0) {
                c.setCurrentState(++numberOfStates);
                i++;
            }
        }

        grid.setNumberOfStates(numberOfStates);
    }

    @Override
    public String toString() {
        return "Random";
    }

}
