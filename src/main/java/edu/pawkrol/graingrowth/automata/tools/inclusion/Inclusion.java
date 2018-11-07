package edu.pawkrol.graingrowth.automata.tools.inclusion;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Moore;
import edu.pawkrol.graingrowth.automata.tools.GrainTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Inclusion {

    public static final int INCLUDED_STATE = -1;

    protected Random random = new Random();

    public void add(Grid grid, boolean setOnEdges, int... args) {
        int n = args[0];
        int size = 1;

        if (args.length > 1) {
            size = args[1];
        }

        if (setOnEdges) {
            addOnEdges(grid, n, size);
        } else {
            addAnywhere(grid, n, size);
        }
    }

    protected abstract void addInclusion(Grid grid, int x, int y, int size);

    private void addAnywhere(Grid grid, int n, int size) {
        for (int i = 0; i < n; i++) {
            int x = random.nextInt(grid.getWidth());
            int y = random.nextInt(grid.getHeight());

            addInclusion(grid, x, y, size);
        }
    }

    private void addOnEdges(Grid grid, int n, int size) {
        List<Cell> edgeCells = GrainTools.getGrainEdgeCells(grid);

        for (int i = 0; i < n; i++) {
            int idx = random.nextInt(edgeCells.size());
            Cell c = edgeCells.get(idx);

            int x = c.getX();
            int y = c.getY();

            addInclusion(grid, x, y, size);
        }
    }

}
