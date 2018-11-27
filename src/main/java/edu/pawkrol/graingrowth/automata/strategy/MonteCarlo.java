package edu.pawkrol.graingrowth.automata.strategy;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;

import java.util.List;
import java.util.Random;

public class MonteCarlo extends Strategy {

    private static MonteCarlo monteCarlo = null;

    private Random random;

    private double grainBoundaryEnergy = 1.0;

    @Override
    public void init() {
        random = new Random();
    }

    @Override
    public void evaluate(Grid grid, Neighbourhood neighbourhood) {
        List<Cell> neighbours;
        Cell c;

        for (int i = 0; i < grid.getSize(); i++) {
            int cx = random.nextInt(grid.getWidth());
            int cy = random.nextInt(grid.getHeight());

            c = grid.getCell(cx, cy);
            neighbours = neighbourhood.neighbours(grid, c);

            long prevE = getEnergy(c.getCurrentState(), neighbours);
            int newState = getNewState(neighbours);
            long newE = getEnergy(newState, neighbours);

            if (newE - prevE <= 0) {
                c.setCurrentState(newState);
            }
        }
    }

    private long getEnergy(int cellState, List<Cell> neighbours) {
        return neighbours.stream().filter(cell -> cell.getCurrentState() != cellState).count();
    }

    private int getNewState(List<Cell> neighbours) {
        return neighbours.get( random.nextInt( neighbours.size() ) ).getCurrentState();
    }

    public double getGrainBoundaryEnergy() {
        return grainBoundaryEnergy;
    }

    //Range of <0.1;1.0>
    public void setGrainBoundaryEnergy(double grainBoundaryEnergy) {
        this.grainBoundaryEnergy
                = grainBoundaryEnergy > 1.0? 1.0 : grainBoundaryEnergy < 0.1? 0.1 : grainBoundaryEnergy;
    }

    public static MonteCarlo getInstance(){
        if (monteCarlo == null) {
            monteCarlo = new MonteCarlo();
        }

        return monteCarlo;
    }

    @Override
    public String toString() {
        return "Monte Carlo";
    }
}
