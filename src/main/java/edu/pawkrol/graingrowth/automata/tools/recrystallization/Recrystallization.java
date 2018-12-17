package edu.pawkrol.graingrowth.automata.tools.recrystallization;

import edu.pawkrol.graingrowth.automata.Grid;

@FunctionalInterface
public interface Recrystallization {
    void recrystallize(Grid grid, int... args);
}
