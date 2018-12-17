package edu.pawkrol.graingrowth.automata.tools.recrystallization;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.tools.GrainTools;

import java.util.List;
import java.util.Random;

public class BoundaryRecrystallize implements RecrystallizationLocation {

    @Override
    public void recrystallize(Grid grid, int... args) {
        List<Cell> boundary = GrainTools.getGrainEdgeCells(grid);

        if (boundary.isEmpty()) return;

        int n = args[0];

        int numberOfStates = grid.getNumberOfStates();
        Random random = new Random();

        for (int i = 0; i < n;) {
            int idx = random.nextInt(boundary.size());
            Cell c = boundary.get(idx);

            if (!c.isRecrystallized()) {
                c.setCurrentState(++numberOfStates);
                c.setRecrystallized(true);
                ++i;
            }
        }

        grid.setNumberOfStates(numberOfStates);
    }

    @Override
    public String toString() {
        return "Boundary";
    }

}
