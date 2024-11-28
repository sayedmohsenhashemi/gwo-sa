package org.fog.test.GreyWolf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parameters contains information about the wolf pack for a given problem.
 *
 * @author Matt
 */
public class Parameters {
    private int N = 20;
    private int D = 1;
    private int I = 30;

    private List<Double> uLimits;
    private List<Double> lLimits;

    public Parameters(int dimensions) {
        D = dimensions;
        uLimits = new ArrayList<Double>(Collections.nCopies(D, 1.0));
        lLimits = new ArrayList<Double>(Collections.nCopies(D, 0.0));
    }

    public int getIterations() {
        return I;
    }

    public int getDimensions() {
        return D;
    }

    public int getWolfCount() {
        return N;
    }

    public List<Double> getULimits() {
        return uLimits;
    }

    public List<Double> getLLimits() {
        return lLimits;
    }

    public void setPackParameters(int packSize, int iterations) {
        I = iterations;
        N = packSize;
    }

    public void setLimits(List<Double> lLimits, List<Double> uLimits) throws Exceptions {
        if (uLimits.size() != D || lLimits.size() != D) {
            throw new Exceptions();
        }
        this.uLimits = uLimits;
        this.lLimits = lLimits;
    }


}
