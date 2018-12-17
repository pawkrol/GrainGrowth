package edu.pawkrol.graingrowth.automata.tools.recrystallization;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;

import java.util.Random;

public class RandomRecrystallize implements RecrystallizationLocation {

    @Override
    public void recrystallize(Grid grid, int... args) {
        int n = args[0];

        int numberOfStates = grid.getNumberOfStates();
        Random random = new Random();

        int x, y;
        Cell c;
        for (int i = 0; i < n;) {
            x = random.nextInt(grid.getWidth());
            y = random.nextInt(grid.getHeight());
            c = grid.getCell(x, y);

            if (!c.isRecrystallized()) {
                c.setCurrentState(++numberOfStates);
                c.setRecrystallized(true);
                ++i;
            }
        }

        grid.setNumberOfStates(numberOfStates);
    }

}
