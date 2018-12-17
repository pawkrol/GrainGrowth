package edu.pawkrol.graingrowth.automata.tools.seed;

import edu.pawkrol.graingrowth.automata.Grid;

@FunctionalInterface
public interface Seeder {
    void seed(Grid grid, int... args);
}
