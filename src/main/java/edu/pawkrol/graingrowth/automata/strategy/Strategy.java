package edu.pawkrol.graingrowth.automata.strategy;


import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;

public interface Strategy {
    void init(Grid grid);
    void evaluate(Grid grid, Neighbourhood neighbourhood);
    void switchState(Cell cell);

    default boolean isFinished() {
        return false;
    }
}
