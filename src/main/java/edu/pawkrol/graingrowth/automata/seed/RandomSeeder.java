package edu.pawkrol.graingrowth.automata.seed;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.strategy.Strategy;

import java.util.Random;

public class RandomSeeder implements Seeder {

    @Override
    public void seed(Strategy strategy, Grid grid, int... args) {
        int n = args[0];

        Random random = new Random();

        int x, y;
        Cell c;
        for (int i = 0; i < n; i++) {
            x = random.nextInt(grid.getWidth());
            y = random.nextInt(grid.getHeight());
            c = grid.getCell(x, y);

            strategy.switchState(c);
        }
    }

    @Override
    public String toString() {
        return "Random";
    }

}
