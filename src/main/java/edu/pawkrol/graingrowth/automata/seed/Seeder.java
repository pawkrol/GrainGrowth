package edu.pawkrol.graingrowth.automata.seed;

import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.strategy.Strategy;

public interface Seeder {

    void seed(Grid grid, int... args);

}
