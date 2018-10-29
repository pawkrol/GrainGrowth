package edu.pawkrol.graingrowth.automata.neighbourhood;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;

import java.util.List;

public class VonNeumann extends Neighbourhood {

    /**
     * .#.
     * #0#
     * .#.
     */
    @Override
    public List<Cell> neighbours(Grid grid, Cell cell) {
        cells.clear();

        int cx = cell.getX();
        int cy = cell.getY();

        addCellToList( cells, grid.getCell(cx, cy - 1) );
        addCellToList( cells, grid.getCell(cx + 1, cy) );
        addCellToList( cells, grid.getCell(cx, cy + 1) );
        addCellToList( cells, grid.getCell(cx - 1, cy) );

        return cells;
    }

    @Override
    public String toString() {
        return "Von Neumann";
    }
}
