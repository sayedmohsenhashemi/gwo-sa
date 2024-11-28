package org.fog.test.cuckoo;


import java.util.ArrayList;


public class Solution extends Search {
    protected bestPosClass bestPos;
    protected ArrayList<Double> velocity;

    public class bestPosClass extends Search {
        public bestPosClass(ArrayList<Double> vars) {
            super(vars);
        }

        public bestPosClass(int numVars) {
            super(numVars);
        }
    }

    public Solution(ArrayList<Double> vars) {
        super(vars);
        this.bestPos = new bestPosClass(vars);
        velocity = new ArrayList<Double>(vars.size());
    }

    public Solution(int numVars) {
        super(numVars);
        this.bestPos = new bestPosClass(numVars);
        velocity = new ArrayList<Double>(numVars);
    }

    public void setVelocity(ArrayList<Double> v) {
        this.velocity = v;
    }

    public ArrayList<Double> getVelocity() {
        return this.velocity;
    }

    public void setRandVel(OptimizationProblem optProb, int numVars) {
        for (int j = 0; j < numVars; j++) {
            this.velocity.add(2 * (rand.nextDouble() - 0.5) * (optProb.getMaxVar(j) - optProb.getMinVar(j)));
        }
    }

    public ArrayList<Double> getCurrPos() {
        return super.getVars();
    }

    public void setCurrPos(ArrayList<Double> vars) {
        super.setVars(vars);
    }

    public void setBestPos() {
        this.bestPos.setVars(this.vars);
    }

    public void setBestPos(ArrayList<Double> vars) {
        this.bestPos.setVars(vars);
    }

    public ArrayList<Double> getBestPos() {
        return bestPos.getVars();
    }

    public bestPosClass getBestPosSol() {
        return bestPos;
    }

    public void printAll() {

        for (int i = 0; i < this.numVars; i++) {
            System.out.printf("v%d:\t%f\n", i, this.velocity.get(i));
        }
        this.print();
    }
}
