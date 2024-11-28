package org.fog.test.GreyWolf;

import net.sourceforge.jswarm_pso.FitnessFunction;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import utils.Constants;
import utils.GenerateMatrices;

import java.util.LinkedList;
import java.util.List;

public class Fitness extends FitnessFunction {
    private static final String[] args = null;
    private static double[][] execMatrix, commMatrix;
    static double Energy;
    private static Object characteristics;
    private static PowerHost host;


    Fitness() {
        super(false);
        commMatrix = GenerateMatrices.getCommMatrix();
        execMatrix = GenerateMatrices.getExecMatrix();
    }

    @Override
    public double evaluate(double[] position) {
        return resourceWastage(position) + ExecTime(position);
    }

    private double ExecTime(double[] position) {
        double totalCost = 0;
        for (int i = 0; i < Constants.NUMBER_OF_Container; i++) {
            int dcId = (int) position[i];
            totalCost = (execMatrix[i][dcId] - commMatrix[i][dcId]);
        }
        return totalCost;
    }

    public double resourceWastage(double[] position) {
        double resourceWastage = 0.1;
        double[] wastage = new double[Constants.NO_OF_DATA_CENTERS];

        for (int j = 0; j < Constants.NUMBER_OF_Container; j++) {
            int dcId = (int) position[j];
            if (wastage[dcId] != 0) --wastage[dcId];
            wastage[dcId] = Math.abs((execMatrix[j][dcId] - commMatrix[j][dcId])) / ((1 - execMatrix[j][dcId]) + (1 - commMatrix[j][dcId]));
            resourceWastage = Math.max(resourceWastage, wastage[dcId]);
        }
        return resourceWastage;
    }

    public static Datacenter container(
            String name,
            Class<? extends Datacenter> datacenterClass,
            List<PowerHost> hostList,
            VmAllocationPolicy vmAllocationPolicy) throws Exception {
        Datacenter datacenter = null;
        Containers contain = new Containers();
        contain.main(args);
        try {
            datacenter = datacenterClass.getConstructor(
                    String.class,
                    DatacenterCharacteristics.class,
                    VmAllocationPolicy.class,
                    List.class,
                    Double.TYPE).newInstance(
                    name,
                    characteristics,
                    vmAllocationPolicy,
                    new LinkedList<Storage>(),
                    Constants.SCHEDULING_INTERVAL);
            PowerVmAllocationPolicyMigrationAbstract power = new PowerVmAllocationPolicyMigrationAbstract(hostList, null) {
                @Override
                protected boolean isHostOverUtilized(PowerHost host) {
                    return false;
                }
            };

            double utilization = power.getUtilizationOfCpuMips(host);
            double Pmax = ((PowerDatacenter) datacenter).getPower() / (103 * 1000);
            double Pmin = ((PowerDatacenter) datacenter).getPower() / (83 * 1000);
            Energy = (Pmax - Pmin) * utilization + Pmin;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        return datacenter;
    }


}
