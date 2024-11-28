package org.fog.test.GreyWolf;

import java.util.List;


public abstract class Function {
    protected int D;

    public Function(int D) {
        this.D = D;
    }

    protected void checkDimensions(List<Double> args) throws Exceptions {
        if (D != args.size())
            throw new Exceptions();
    }

    public abstract double eval(List<Double> args) throws Exceptions;
}
