package edu.pawkrol.graingrowth.automata;

import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;
import edu.pawkrol.graingrowth.automata.seed.Seeder;
import edu.pawkrol.graingrowth.automata.strategy.Strategy;

public class AutomataResolver {

    private Strategy strategy;
    private Neighbourhood neighbourhood;
    private Grid grid;

    public void init() throws NullPointerException {
        if (grid == null) throw new NullPointerException("Grid is null - Grid not set");
        if (strategy == null) throw new NullPointerException("Strategy is null - Strategy not set");
        if (neighbourhood == null) throw new NullPointerException("Neighbourhood is null - Neighbourhood not set");

        strategy.init(grid);
    }

    public void seedWith(Seeder seeder, int... args) {
        seeder.seed(strategy, grid, args);
    }

    public void switchState(Cell c){
        strategy.switchState(c);
    }

    public void setStrategy(Strategy strategy){
        this.strategy = strategy;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setNeighbourhood(Neighbourhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    public Grid makeStep(){
        strategy.evaluate(grid, neighbourhood);
        return grid;
    }

}
