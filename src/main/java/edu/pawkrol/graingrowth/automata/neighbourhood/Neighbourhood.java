package edu.pawkrol.graingrowth.automata.neighbourhood;

import edu.pawkrol.graingrowth.automata.Cell;
import edu.pawkrol.graingrowth.automata.Grid;

import java.util.ArrayList;
import java.util.List;

public abstract class Neighbourhood {

    protected List<Cell> cells = new ArrayList<>();

    public abstract List<Cell> neighbours(Grid grid, Cell cell);

    protected void addCellToList(List<Cell> list, Cell cell){
        if (cell != null){
            list.add(cell);
        }
    }

}
