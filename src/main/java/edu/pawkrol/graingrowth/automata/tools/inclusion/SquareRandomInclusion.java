package edu.pawkrol.graingrowth.automata.tools.inclusion;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;

public class SquareRandomInclusion extends Inclusion {

    @Override
    protected void addInclusion(Grid grid, int x, int y, int size) {
        for (int w = 0; w < size; w++) {
            for (int h = 0; h < size; h++) {
                Cell c = grid.getCell(x + w, y + h);
                if (c != null) {
                    c.setCurrentState(INCLUDED_STATE);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Square Random";
    }

}
