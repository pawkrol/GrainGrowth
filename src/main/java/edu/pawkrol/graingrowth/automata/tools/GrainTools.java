package edu.pawkrol.graingrowth.automata.tools;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Moore;

import java.util.*;
import java.util.stream.Collectors;

public class GrainTools {

    public static List<Integer> selectRandomGrains(Grid grid, int numberOfGrains) {
        int numberOfStates = grid.getNumberOfStates();

        return new Random().ints(numberOfGrains, 1, numberOfStates)
                .boxed()
                .collect(Collectors.toList());
    }

    public static List<Integer> selectAllGrains(Grid grid) {
        int numberOfStates = grid.getNumberOfStates();
        List<Integer> states = new LinkedList<>();

        for (int i = 1; i <= numberOfStates; i++) {
            states.add(i);
        }

        return states;
    }

    public static List<Cell> getGrainEdgeCells(Grid grid) {
        List<Cell> edgeCells = new ArrayList<>();
        Moore moore = new Moore();

        grid.forEach(cell -> {
            List<Cell> neighbours = moore.neighbours(grid, cell);

            if (neighbours.stream()
                    .anyMatch(c -> c.getCurrentState() != cell.getCurrentState())) {
                edgeCells.add(cell);
            }
        });

        return edgeCells;
    }

    public static void keepAndLockSelectedGrains(Grid grid, List<Integer> statesToKeep, boolean clearState) {
        if (clearState) {
            grid.setNumberOfStates(1);
        } else {
            grid.setNumberOfStates(statesToKeep.size());
        }

        grid.forEach(c -> {
            if (!statesToKeep.contains(c.getCurrentState())) {
                c.setCurrentState(0);
            } else {
                c.setLocked(true);

                if (clearState) {
                    c.setCurrentState(-2);
                }
            }
        });
    }

    public static void keepAndLockGrainEdges(Grid grid, List<Integer> statesToKeep, int thickness) {
        for (int i = 0; i < thickness; i++) { //XD solution
            List<Cell> edgeCells = getGrainEdgeCells(grid);

            edgeCells.forEach(cell -> {
                if (statesToKeep.contains(cell.getCurrentState())) {
                    cell.setCurrentState(-2);
                }
            });
        }

        grid.forEach(cell -> {
            if (cell.getCurrentState() != -2) {
                cell.setCurrentState(0);
            }
        });
    }

    public static void keepAndLockGrainOutsideEdges(Grid grid, List<Integer> statesToKeep, int thickness) {
        grid.forEach(c -> {
            if (statesToKeep.contains(c.getCurrentState())) {
                c.setCurrentState(-1);
            } else {
                c.setCurrentState(0);
            }
        });

        keepAndLockGrainEdges(grid, Collections.singletonList(-1), thickness);
    }

    public static void distributeEnergyHomogenous(Grid grid, int h) {
        grid.forEach(c -> c.setH(h));
    }

    public static void distributeEnergyHeterogenous(Grid grid, int hMin, int hMax) {
        grid.forEach(c -> c.setH(hMin));
        GrainTools.getGrainEdgeCells(grid)
                .forEach(c -> c.setH(hMax));
    }
}
