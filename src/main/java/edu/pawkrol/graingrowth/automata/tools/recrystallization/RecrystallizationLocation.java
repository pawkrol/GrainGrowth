package edu.pawkrol.graingrowth.automata.tools.recrystallization;

import edu.pawkrol.graingrowth.automata.Grid;

@FunctionalInterface
public interface RecrystallizationLocation {
    void recrystallize(Grid grid, int... args);
}
