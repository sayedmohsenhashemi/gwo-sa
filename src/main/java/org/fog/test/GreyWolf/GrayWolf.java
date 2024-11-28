package org.fog.test.GreyWolf;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.Helper;
import org.cloudbus.cloudsim.power.PlanetLabHelper;
import org.cloudbus.cloudsim.power.RunnerAbstract;
import utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class GrayWolf {
    private List<Define> data;
    Define wAlpha = null;
    Define wBeta = null;
    Define wDelta = null;
    double wAlphaBest = Double.POSITIVE_INFINITY;
    double wBetaBest = Double.POSITIVE_INFINITY;
    double wDeltaBest = Double.POSITIVE_INFINITY;
    List<Double> lLimits, uLimits;

    public class Nodes extends RunnerAbstract {

        public Nodes(boolean enableOutput, boolean outputToFile, String inputFolder, String outputFolder, String workload, String vmAllocationPolicy,
                     String vmSelectionPolicy,
                     String parameter) {
            super(enableOutput, outputToFile, inputFolder, outputFolder, workload, vmAllocationPolicy, vmSelectionPolicy, parameter);
        }

        @Override
        public void init(String inputFolder) {
            try {

                CloudSim.init(1, Calendar.getInstance(), false);
                broker = Helper.createBroker();
                int brokerId = broker.getId();
                cloudletList = PlanetLabHelper.createCloudletListPlanetLab(brokerId, inputFolder);
                vmList = Helper.createVmList(brokerId, cloudletList.size());
                hostList = Helper.createHostList(Constants.NUMBER_OF_Nodes);

            } catch (Exception e) {
                e.printStackTrace();
                Log.printLine("unexpected error");
                System.exit(0);
            }
        }
    }

    public GrayWolf() {

    }

    private void initialize(int N, int D, List<Double> lLimits, List<Double> uLimits) {
        Random rand = new Random();
        data = new ArrayList<Define>();
        this.lLimits = lLimits;
        this.uLimits = uLimits;

        for (int i = 0; i < N; i++) {
            List<Double> pos = new ArrayList<Double>();
            for (int j = 0; j < D; j++) {
                double P = rand.nextDouble() * (uLimits.get(j) - lLimits.get(j)) + lLimits.get(j);
                pos.add(P);
            }
            data.add(new Define(pos));
        }
        wAlpha = null;
        wBeta = null;
        wDelta = null;
        wAlphaBest = Double.POSITIVE_INFINITY;
        wBetaBest = Double.POSITIVE_INFINITY;
        wDeltaBest = Double.POSITIVE_INFINITY;

    }

    private void resetWolfBests(Double val) {
        wAlphaBest = val;
        wBetaBest = val;
        wDeltaBest = val;
    }

    private void chooseLeadingWolves(Function f) {
        Comparator comp = new Comparator() {
            public boolean compare(double x, double y) {
                return x < y;
            }
        };

        chooseLeadingWolves(f, comp);
    }

    private void chooseLeadingWolves(Function f, Comparator comp) {

        for (Define w : data) {
            double fVal = f.eval(w.getPos());
            if (comp.compare(fVal, wAlphaBest)) {
                wAlpha = w;
                wAlphaBest = fVal;
            }
        }
        for (Define w : data) {
            double fVal = f.eval(w.getPos());
            if (comp.compare(fVal, wBetaBest) && w != wAlpha) {
                wBeta = w;
                wBetaBest = fVal;
            }
        }
        for (Define w : data) {
            double fVal = f.eval(w.getPos());
            if (comp.compare(fVal, wDeltaBest) && w != wBeta && w != wAlpha) {
                wDelta = w;
                wDeltaBest = fVal;
            }
        }

        wAlpha = new Define(wAlpha);
        wBeta = new Define(wBeta);
        wDelta = new Define(wDelta);
    }

    private void moveTheWolf(Define w, double a) {
        for (int j = 0; j < w.getPos().size(); j++) {

            double r1 = Math.random();
            double r2 = Math.random();

            double M1 = 2 * a * r1 * a;
            double N1 = 2 * r2;
            double DAlpha = Math.abs(N1 * wAlpha.posAtIndex(j) - w.posAtIndex(j));
            double X1 = wAlpha.posAtIndex(j) - M1 * DAlpha;

            r1 = Math.random();
            r2 = Math.random();

            double M2 = 2 * a * r1 * a;
            double N2 = 2 * r2;
            double DBeta = Math.abs(N2 * wBeta.posAtIndex(j) - w.posAtIndex(j));
            double X2 = wBeta.posAtIndex(j) - M2 * DBeta;

            r1 = Math.random();
            r2 = Math.random();

            double M3 = 2 * a * r1 * a;
            double N3 = 2 * r2;
            double DDelta = Math.abs(N3 * wDelta.posAtIndex(j) - w.posAtIndex(j));
            double X3 = wDelta.posAtIndex(j) - M3 * DDelta;

            w.setAtIndex(j, (X1 + X2 + X3) / 3);
        }
    }

    private void trimToLimits() {
        for (int i = 0; i < data.size(); i++) {
            Define w = data.get(i);
            for (int j = 0; j < w.getPos().size(); j++) {
                if (w.posAtIndex(j) < lLimits.get(j)) {
                    w.setAtIndex(j, lLimits.get(j));
                }
                if (w.posAtIndex(j) > uLimits.get(j)) {
                    w.setAtIndex(j, uLimits.get(j));
                }
            }
        }
    }

    public Solution find(Function f, Parameters parameters) throws Exceptions {
        boolean enableOutput = true;
        boolean outputToFile = false;
        String srt = "mu";
        String inputFolder = GrayWolf.class.getClassLoader().getResource("workload").getPath();
        String workload = "workload";
        String parameter = "1.2";
        String AllocationPolicy = "GrayWolf";
        String outputFolder = "output";
        double MaxA = 2.0;
        List<Double> progression = new ArrayList<Double>();

        initialize(parameters.getWolfCount(), parameters.getDimensions(), parameters.getLLimits(), parameters.getULimits());
        resetWolfBests(Double.POSITIVE_INFINITY);
        chooseLeadingWolves(f);

        int I = parameters.getIterations();
        for (int h = 0; h < I; h++) {

            double a = MaxA - h * MaxA / (double) I;
            for (int i = 0; i < data.size(); i++) {
                moveTheWolf(data.get(i), a);
            }
            Fitness fitness = new Fitness();
            fitness.setMaximize(true);
            trimToLimits();
            resetWolfBests(Double.POSITIVE_INFINITY);
            chooseLeadingWolves(f);
            progression.add(f.eval(wAlpha.getPos()));
            GrayWolf newRunner = new GrayWolf();
            newRunner.new Nodes(enableOutput, outputToFile, inputFolder, outputFolder, workload, AllocationPolicy, srt, parameter);

        }
        return new Solution(wAlpha, progression);
    }

}
