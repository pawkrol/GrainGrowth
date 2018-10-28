package edu.pawkrol.graingrowth.automata.inclusion;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;

public class RadiusRandomInclusion extends Inclusion {

    protected void addInclusion(Grid grid, int x, int y, int size) {
        Cell sc = grid.getCell(x, y);

        grid.forEach(c -> {
            double dist = Math.sqrt((c.getX() - sc.getX()) * (c.getX() - sc.getX())
                    + (c.getY() - sc.getY()) * (c.getY() - sc.getY()));

            if (dist <= size) {
                c.setCurrentState(INCLUDED_STATE);
            }
        });

        sc.setCurrentState(INCLUDED_STATE);
    }

    @Override
    public String toString() {
        return "Radius Random";
    }

}
