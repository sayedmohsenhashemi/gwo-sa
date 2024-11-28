package org.fog.test.cuckoo;


import java.util.ArrayList;

public abstract class OptimizationProblem {
    protected double scalingFactor = 1;

    protected ConstraintSet constraints;

    public OptimizationProblem() {
        this.constraints = new ConstraintSet();
    }

    public abstract int getNumVar();

    public abstract double fitness(Search s);

    public abstract boolean withinCustomConstraints(Search s);

    public abstract String solToString(Search s);

    public String solToJson(Search s) {
        return null;
    }

    public String solToTable(Search s) {
        return null;
    }

    public double getScalingFactor() {
        return scalingFactor;
    }

    public class Constraint {
        int varIndex;
        double min, max;

        Constraint(int varIndex, double min, double max) {
            this.varIndex = varIndex;
            this.min = min;
            this.max = max;
        }

        public int getVarIndex() {
            return this.varIndex;
        }

        public double getMin() {
            return this.min;
        }

        public double getMax() {
            return this.max;
        }
    }

    public class ConstraintSet {
        ArrayList<Constraint> constraints;

        public ConstraintSet() {
            this.constraints = new ArrayList<Constraint>();
        }

        private int indexOfConstraintFor(int varIndex) {
            int size = this.constraints.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    if (this.constraints.get(i).getVarIndex() == varIndex) return i;
                }
            }
            return -1;
        }

        public boolean hasConstraintFor(int varIndex) {
            return this.indexOfConstraintFor(varIndex) != -1;
        }

        public boolean add(Constraint varConstraint) {
            int varIndex = varConstraint.getVarIndex();
            int i = this.indexOfConstraintFor(varIndex);
            if (i == -1) {
                this.constraints.add(varConstraint);
                return false;
            } else {
                this.constraints.set(i, varConstraint);
                return true;
            }
        }

        public Constraint getConstraintFor(int varIndex) throws Exception {
            if (!this.hasConstraintFor(varIndex)) {
                throw new Exception("Constraint not initialized.");
            }
            return this.constraints.get(this.indexOfConstraintFor(varIndex));
        }
    }

    private Constraint getConstraint(int varIndex) {
        try {
            return this.constraints.getConstraintFor(varIndex);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
            return null; // never reached
        }
    }

    public final double getMinVar(int varIndex) {
        Constraint varConstraint;
        varConstraint = this.getConstraint(varIndex);
        return varConstraint.getMin();
    }

    public final double getMaxVar(int varIndex) {
        Constraint varConstraint;
        varConstraint = this.getConstraint(varIndex);
        return varConstraint.getMax();
    }

    private boolean withinConstraints(Search sol, int varIndex) {
        double var = sol.getVars().get(varIndex);
        Constraint constraint = this.getConstraint(varIndex);
        return constraint.getMin() <= var && var <= constraint.getMax();
    }

    public boolean withinConstraints(Search sol) {
        for (int i = 0; i < this.getNumVar(); i++) {
            if (!withinConstraints(sol, i)) return false;
        }

        return this.withinCustomConstraints(sol);
    }
}
