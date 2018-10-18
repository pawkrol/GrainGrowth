package edu.pawkrol.graingrowth.automata.seed;

import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.strategy.Strategy;

public interface Seeder {

    void seed(Strategy strategy, Grid grid, int... args);

}
