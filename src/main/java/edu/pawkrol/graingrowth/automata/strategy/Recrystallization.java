package edu.pawkrol.graingrowth.automata.strategy;

import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;
import edu.pawkrol.graingrowth.view.RecrystallizationDialog;

public class Recrystallization extends Strategy {

    private static Recrystallization recrystallization;

    @Override
    public void init() {
        new RecrystallizationDialog()
                .open()
                .ifPresent(options -> {
                    System.out.println();
                });
    }

    @Override
    public void evaluate(Grid grid, Neighbourhood neighbourhood) {

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
