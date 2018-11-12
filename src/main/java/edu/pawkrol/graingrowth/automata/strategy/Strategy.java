package edu.pawkrol.graingrowth.automata.strategy;


import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Strategy {

    public abstract void init();
    public abstract void evaluate(Grid grid, Neighbourhood neighbourhood);

    public boolean isFinished() {
        return false;
    }

    protected Map<Integer, Long> getStatesFrequency(List<Cell> neighbours) {
        return neighbours.stream()
                .filter(c -> !c.isLocked())
                .map(Cell::getPreviousState)
                .filter(s -> s > 0)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    protected int getMostFrequentState(List<Cell> neighbours) {
        return getStatesFrequency(neighbours)
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    protected int getMostFrequentStateInBoundary(List<Cell> neighbours, long lower, long upper) {
        return getStatesFrequency(neighbours)
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .filter(e -> e.getValue() >= lower && e.getValue() <= upper)
                .map(Map.Entry::getKey)
                .orElse(0);
    }

}
