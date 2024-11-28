package org.cloudbus.cloudsim.power;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.Calendar;

public class PlanetLabRunner extends RunnerAbstract {

    public PlanetLabRunner(
            boolean enableOutput,
            boolean outputToFile,
            String inputFolder,
            String outputFolder,
            String workload,
            String vmAllocationPolicy,
            String vmSelectionPolicy,
            String parameter) {
        super(
                enableOutput,
                outputToFile,
                inputFolder,
                outputFolder,
                workload,
                vmAllocationPolicy,
                vmSelectionPolicy,
                parameter);
    }

    @Override
    protected void init(String inputFolder) {
        try {
            CloudSim.init(1, Calendar.getInstance(), false);

            broker = Helper.createBroker();
            int brokerId = broker.getId();
            cloudletList = PlanetLabHelper.createCloudletListPlanetLab(brokerId, inputFolder);
            vmList = Helper.createVmList(brokerId, cloudletList.size());
            hostList = Helper.createHostList(1500);
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
            System.exit(0);
        }
    }

}
