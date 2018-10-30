package edu.pawkrol.graingrowth.automata.tools;

import edu.pawkrol.graingrowth.automata.Grid;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GrainSelector {

    public static void keepAndLockSelectedGrains(Grid grid, List<Integer> statesToKeep) {
        grid.setNumberOfStates(1);
        grid.forEach(c -> {
            if (!statesToKeep.contains(c.getCurrentState())) {
                c.setCurrentState(0);
            } else {
                c.setCurrentState(-2);
            }
        });
    }

    public static List<Integer> selectRandomGrains(Grid grid, int numberOfGrains) {
        int numberOfStates = grid.getNumberOfStates();

        return new Random().ints(numberOfGrains, 1, numberOfStates)
                .boxed()
                .collect(Collectors.toList());
    }

}
