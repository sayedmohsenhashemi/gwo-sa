package org.fog.test.pso;


import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.Helper;
import org.cloudbus.cloudsim.power.PlanetLabHelper;
import org.cloudbus.cloudsim.power.RunnerAbstract;
import utils.Constants;
import utils.DatacenterCreator;
import utils.ResourceWastage;

import java.util.*;

public class PSO_Scheduler {
    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmList;
    private static Datacenter[] datacenter;
    private static PSO PSOSchedularInstance;
    private static double mapping[];
    private static double[][] memory;
    private static double[][] processor;

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

    private static List<Vm> createVM(int userId, int vms) {
        LinkedList<Vm> list = new LinkedList<Vm>();
        long size = 10000;
        int ram = 512;
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1;
        String vmm = "Xen";
        Vm[] vm = new Vm[vms];

        for (int i = 0; i < vms; i++) {
            vm[i] = new Vm(datacenter[i].getId(), userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            list.add(vm[i]);
        }

        return list;
    }

    private static List<Cloudlet> createCloudlet(int userId, int cloudlets, int idShift) {
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        Cloudlet[] cloudlet = new Cloudlet[cloudlets];

        for (int i = 0; i < cloudlets; i++) {
            int dcId = (int) (mapping[i]);
            long length = (long) ((memory[i][dcId] + processor[i][dcId]));
            cloudlet[i] = new Cloudlet(idShift + i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
            cloudlet[i].setUserId(userId);
            list.add(cloudlet[i]);
        }

        return list;
    }

    public static void main(String[] args) {
        boolean enableOutput = true;
        boolean outputToFile = false;
        String srt = "mmt";
        String inputFolder = Run.class.getClassLoader().getResource("workload").getPath();
        String workload = "workload";
        String parameter = "1.2";
        String AllocationPolicy = "PSO";
        String outputFolder = "output";
        new ResourceWastage();
        memory = ResourceWastage.getMemMatrix();
        processor = ResourceWastage.getPrMatrix();
        PSOSchedularInstance = new PSO();
        mapping = PSOSchedularInstance.run();

        try {
            int user = 15;
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;

            CloudSim.init(user, calendar, trace_flag);

            datacenter = new Datacenter[Constants.NO_OF_DATA_CENTERS];
            for (int i = 0; i < Constants.NO_OF_DATA_CENTERS; i++) {
                datacenter[i] = DatacenterCreator.createDatacenter("Datacenter_" + i);
            }

            PSODatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            vmList = createVM(brokerId, Constants.NO_OF_DATA_CENTERS);
            cloudletList = createCloudlet(brokerId, Constants.NO_OF_TASKS, 0);

            HashSet<Integer> dcIds = new HashSet<>();
            HashMap<Integer, Integer> hm = new HashMap<>();
            for (Datacenter dc : datacenter) {
                if (!dcIds.contains(dc.getId()))
                    dcIds.add(dc.getId());
            }
            Iterator<Integer> it = dcIds.iterator();
            for (int i = 0; i < mapping.length; i++) {
                if (hm.containsKey((int) mapping[i])) continue;
                hm.put((int) mapping[i], it.next());
            }

            for (int i = 0; i < mapping.length; i++)
                mapping[i] = hm.containsKey((int) mapping[i]) ? hm.get((int) mapping[i]) : mapping[i];
            broker.submitVmList(vmList);
            broker.setMapping(mapping);
            broker.submitCloudletList(cloudletList);
            PSO_Scheduler newRunner = new PSO_Scheduler();
            PSO_Scheduler.Nodes runner = newRunner.new Nodes(enableOutput, outputToFile, inputFolder, outputFolder, workload, AllocationPolicy, srt, parameter);

        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("an unexpected error");
        }
    }


    private static PSODatacenterBroker createBroker(String name) throws Exception {
        return new PSODatacenterBroker(name);
    }

}