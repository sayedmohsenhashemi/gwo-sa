package org.fog.test.GreyWolf;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
import org.fog.entities.*;
import org.fog.placement.ModuleMapping;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.fog.utils.TimeKeeper;
import org.fog.utils.distribution.DeterministicDistribution;
import utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class Start {
    static List<FogDevice> fogDevices = new ArrayList<FogDevice>();
    static List<Sensor> sensors = new ArrayList<Sensor>();
    static List<Actuator> actuators = new ArrayList<Actuator>();
    static boolean CLOUD = true;
    static int numOfDepts = 4;
    static int numOfMobilesPerDept = 6;
    static double EEG_TRANSMISSION_TIME = 5.1;
    static int NumWolf = Constants.POPULATION_SIZE;
    static int D = 1;
    static int iterations = 30;

    public static void main(String[] args) {

        Log.printLine("Starting Simulation...");

        try {
            Log.disable();
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;

            CloudSim.init(15, calendar, trace_flag);

            String appId = "application";

            FogBroker broker = new FogBroker("broker");

            createFogDevices(broker.getId(), appId);

            ModuleMapping moduleMapping = ModuleMapping.createModuleMapping();
            moduleMapping.addModuleToDevice("connector", "cloud");

            TimeKeeper.getInstance().setSimulationStartTime(Calendar.getInstance().getTimeInMillis());

            Function f = new Function(1) {
                public double eval(List<Double> args) throws Exceptions {
                    checkDimensions(args);
                    double x = args.get(0);
                    double wyn = (x - Math.PI) * (x - Math.PI) + 1;
                    return wyn;
                }
            };

            Parameters params = new Parameters(D);
            params.setPackParameters(NumWolf, iterations);
            GrayWolf pack = new GrayWolf();
            Solution solution = pack.find(f, params);
            System.out.println(solution);

        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happen");
        }
    }

    private static void createFogDevices(int userId, String appId) {
        FogDevice cloud = createFogDevice("cloud", 44800, 40000, 100, 10000, 0, 0.01, 16 * 103, 16 * 83.25);
        cloud.setParentId(-1);
        FogDevice proxy = createFogDevice("proxy-server", 2800, 4000, 10000, 10000, 1, 0.0, 107.339, 83.4333);
        proxy.setParentId(cloud.getId());
        proxy.setUplinkLatency(100);

        fogDevices.add(cloud);
        fogDevices.add(proxy);

        for (int i = 0; i < numOfDepts; i++) {
            addGw(i + "", userId, appId, proxy.getId());
        }
    }

    private static FogDevice addGw(String id, int userId, String appId, int parentId) {
        FogDevice dept = createFogDevice("d-" + id, 2800, 4000, 10000, 10000, 1, 0.0, 107.339, 83.4333);
        fogDevices.add(dept);
        dept.setParentId(parentId);
        dept.setUplinkLatency(4);
        for (int i = 0; i < numOfMobilesPerDept; i++) {
            String mobileId = id + "-" + i;
            FogDevice mobile = addMobile(mobileId, userId, appId, dept.getId());
            mobile.setUplinkLatency(2);
            fogDevices.add(mobile);
        }
        return dept;
    }

    private static FogDevice addMobile(String id, int userId, String appId, int parentId) {
        FogDevice mobile = createFogDevice("m-" + id, 1000, 1000, 10000, 270, 3, 0, 87.53, 82.44);
        mobile.setParentId(parentId);
        Sensor eegSensor = new Sensor("s-" + id, "EEG", userId, appId, new DeterministicDistribution(EEG_TRANSMISSION_TIME));
        sensors.add(eegSensor);
        Actuator display = new Actuator("a-" + id, userId, appId, "DISPLAY");
        actuators.add(display);
        eegSensor.setGatewayDeviceId(mobile.getId());
        eegSensor.setLatency(6.0);
        display.setGatewayDeviceId(mobile.getId());
        display.setLatency(1.0);
        return mobile;
    }

    private static FogDevice createFogDevice(String nodeName, long mips,
                                             int ram, long upBw, long downBw, int level, double ratePerMips, double busyPower, double idlePower) {

        List<Pe> peList = new ArrayList<Pe>();

        peList.add(new Pe(0, new PeProvisionerOverbooking(mips)));

        int hostId = FogUtils.generateEntityId();
        long storage = 1000000;
        int bw = 10000;

        PowerHost host = new PowerHost(
                hostId,
                new RamProvisionerSimple(ram),
                new BwProvisionerOverbooking(bw),
                storage,
                peList,
                new StreamOperatorScheduler(peList),
                new FogLinearPowerModel(busyPower, idlePower)
        );

        List<Host> hostList = new ArrayList<Host>();
        hostList.add(host);

        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double time_zone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.03;
        LinkedList<Storage> storageList = new LinkedList<Storage>();
        FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(
                arch, os, vmm, host, time_zone, cost, costPerMem,
                costPerStorage, costPerBw);

        FogDevice fogdevice = null;
        try {
            fogdevice = new FogDevice(nodeName, characteristics,
                    new AppModuleAllocationPolicy(hostList), storageList, 10, upBw, downBw, 0, ratePerMips);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fogdevice.setLevel(level);
        return fogdevice;
    }

}