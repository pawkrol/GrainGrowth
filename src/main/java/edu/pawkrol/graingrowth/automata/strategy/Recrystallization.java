package edu.pawkrol.graingrowth.automata.strategy;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;
import edu.pawkrol.graingrowth.automata.tools.recrystallization.BoundaryRecrystallize;
import edu.pawkrol.graingrowth.automata.tools.recrystallization.RandomRecrystallize;
import edu.pawkrol.graingrowth.automata.tools.recrystallization.RecrystallizationLocation;
import edu.pawkrol.graingrowth.automata.tools.recrystallization.RecrystallizationOptions;
import edu.pawkrol.graingrowth.utils.ColorHelper;
import edu.pawkrol.graingrowth.view.RecrystallizationDialog;

import java.util.List;
import java.util.Random;

public class Recrystallization extends Strategy {

    private static Recrystallization recrystallization;

    private RecrystallizationOptions options;
    private Random random;

    private boolean finished;
    private int nucleationValue = 0;

    private RecrystallizationLocation recrystallizationLocation;

    @Override
    public void init(Grid grid) {
        options = null;
        finished = false;
        nucleationValue = 0;

        ColorHelper.inverseColorSpectrum();

        new RecrystallizationDialog()
                .open()
                .ifPresent(options -> this.options = options);

        if (options == null) {
            finished = true;
            return;
        }

        nucleationValue = options.getNucleationValue();

        if (options.getNucleationGrainBoundary())
            recrystallizationLocation = new BoundaryRecrystallize();
        else
            recrystallizationLocation = new RandomRecrystallize();

        if (options.getNucleationType() == RecrystallizationOptions.NucleationType.STATIC) {
            recrystallizationLocation.recrystallize(grid, nucleationValue);
        }
    }

    @Override
    public void evaluate(Grid grid, Neighbourhood neighbourhood) {
        if (options == null) {
            finished = true;
            return;
        }

        random = new Random();

        addNucleons(grid);
        grid.forEach(cell -> cell.setChecked(false));

        for (;;) {
            int cx = random.nextInt(grid.getWidth());
            int cy = random.nextInt(grid.getHeight());
            Cell c = grid.getCell(cx, cy);

            if (c.isChecked()) continue;

            c.setChecked(true);

            List<Cell> neighbours = neighbourhood.neighbours(grid, c);
            Cell nc = neighbours.get(random.nextInt(neighbours.size()));

            if (nc.isRecrystallized()) {
                int newState = nc.getCurrentState();

                int prevE = getEnergy(c.getCurrentState(), neighbours) + c.getH();
                int newE = getEnergy(newState, neighbours);

                if (newE - prevE < 0) {
                    c.setH(newE);
                    c.setCurrentState(newState);
                    c.setRecrystallized(true);
                }
            }

            boolean allChecked = grid.stream().allMatch(Cell::isChecked);
            if (allChecked) break;
        }

        finished = grid.stream().allMatch(Cell::isRecrystallized);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void clean() {
        ColorHelper.inverseColorSpectrum();
    }

    private void addNucleons(Grid grid) {
        if (options.getNucleationType() == RecrystallizationOptions.NucleationType.CONSTANT) {
            recrystallizationLocation.recrystallize(grid, nucleationValue);
        } else if (options.getNucleationType() == RecrystallizationOptions.NucleationType.INCREASING) {
            recrystallizationLocation.recrystallize(grid, nucleationValue);
            nucleationValue += options.getNucleationValue();
        }
    }

    private int getEnergy(int cellState, List<Cell> neighbours) {
        return (int) neighbours.stream().filter(cell -> cell.getCurrentState() != cellState).count();
    }

    public static Recrystallization getInstance(){
        if (recrystallization == null) {
            recrystallization = new Recrystallization();
        }

        return recrystallization;
    }

    @Override
    public String toString() {
        return "Recrystallization";
    }


}
