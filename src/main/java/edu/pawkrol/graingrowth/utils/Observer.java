package edu.pawkrol.graingrowth.utils;

import java.util.function.Consumer;

public class Observer<T> {

    private Consumer<T> observingFunction;

    public void emit(T value) {
        observingFunction.accept(value);
    }

    public void observe(Consumer<T> f) {
        this.observingFunction = f;
    }

}
