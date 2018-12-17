package edu.pawkrol.graingrowth.automata;

import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;
import edu.pawkrol.graingrowth.automata.strategy.Strategy;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class AutomataResolver extends ScheduledService<Grid> {

    private Strategy strategy;
    private Neighbourhood neighbourhood;
    private Grid grid;

    private int iteration;
    private int maxIterations;

    public void init() {
        iteration = 0;
    }

    public void setStrategy(Strategy strategy){
        this.strategy = strategy;
        this.strategy.init(grid);
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

    public int getIteration() {
        return iteration;
    }

    public boolean isFinished(){
        return strategy.isFinished();
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public boolean cancel() {
        strategy.clean();
        return super.cancel();
    }

    public Grid makeStep(){
        strategy.evaluate(grid, neighbourhood);
        return grid;
    }

    @Override
    protected Task<Grid> createTask() {
        return new Task<Grid>() {
            @Override
            protected Grid call() throws Exception {
                if (isFinished() || (maxIterations > 0 && iteration >= maxIterations)) {
                    this.cancel();
                }
                iteration++;

                return makeStep();
            }
        };
    }

}
