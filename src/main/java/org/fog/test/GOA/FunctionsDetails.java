package org.fog.test.GOA;


public class FunctionsDetails {
    private double[] lb;
    private double[] ub;
    private int dim = 0;
    private String functionName = "f1";
    private boolean oddDimension = false;
    int cMin;
    int cMax;

    public boolean isOddDimension() {
        return oddDimension;
    }

    public void setOddDimension(boolean oddDimension) {
        this.oddDimension = oddDimension;
    }

    public double[] getLb() {
        return lb;
    }

    public double[] getUb() {
        return ub;
    }

    public int getDim() {
        return dim;
    }

    public void setLb(double[] lb) {
        this.lb = lb;
    }

    public void setUb(double[] ub) {
        this.ub = ub;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public FunctionsDetails(String functionName) {

        switch (functionName) {
            case "F1":
                this.functionName = functionName;
                this.dim = 30;
                cMin = -100;
                cMax = 100;
                correctDimension();
                lb = new double[dim];
                ub = new double[dim];
                repeatBound(cMin, cMax, dim);
                break;
            case "F2":
                this.functionName = functionName;
                this.dim = 5;
                cMin = (int) 0.0001;
                cMax = 1;
                correctDimension();
                lb = new double[dim];
                ub = new double[dim];
                repeatBound(cMin, cMax, dim);
                break;
        }
    }

    public void correctDimension() {
        if (dim % 2 != 0) {
            dim += 1;
            oddDimension = true;
        }
    }

    public void repeatBound(double lbValue, double ubValue, int dim) {
        for (int i = 0; i < lb.length; i++) {
            lb[i] = lbValue;
            ub[i] = ubValue;
        }
    }

    public double f1(double[] array) {
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            double element = array[i];
            sum += element * element;
        }
        return sum;
    }


    public double f2(double[] array) {

        double sum = 0;
        double prod = 0;
        for (int i = 0; i < array.length; i++) {
            double element = Math.abs(array[i]);
            sum += element;
            prod *= element;
        }
        return sum + prod;
    }

}
