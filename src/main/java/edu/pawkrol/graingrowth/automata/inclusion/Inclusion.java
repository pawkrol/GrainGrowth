package edu.pawkrol.graingrowth.automata.inclusion;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;

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
        List<Cell> edgeCells = getEdgeCells(grid);

        for (int i = 0; i < n; i++) {
            int idx = random.nextInt(edgeCells.size());
            Cell c = edgeCells.get(idx);

            int x = c.getX();
            int y = c.getY();

            addInclusion(grid, x, y, size);
        }
    }

    private List<Cell> getEdgeCells(Grid grid) {
        List<Cell> edgeCells = new ArrayList<>();

        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Cell a = grid.getCell(x, y);
                Cell b = grid.getCell(x + 1, y);
                Cell c = grid.getCell(x, y + 1);
                Cell d = grid.getCell(x + 1, y + 1);

                if ((b != null && a.getCurrentState() != b.getCurrentState())
                        || (c != null && a.getCurrentState() != c.getCurrentState())
                        || (d != null && a.getCurrentState() != d.getCurrentState())) {
                    edgeCells.add(a);
                }
            }
        }

        return edgeCells;
    }

}
