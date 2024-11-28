package org.cloudbus.cloudsim.power;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.UtilizationModelPlanetLabInMemory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PlanetLabHelper {

    public static List<Cloudlet> createCloudletListPlanetLab(int brokerId, String inputFolderName)
            throws FileNotFoundException {
        List<Cloudlet> list = new ArrayList<Cloudlet>();

        long fileSize = 300;
        long outputSize = 300;
        UtilizationModel utilizationModelNull = new UtilizationModelNull();

        File inputFolder = new File(inputFolderName);
        File[] files = inputFolder.listFiles();

        for (int i = 0; i < files.length; i++) {
            Cloudlet cloudlet = null;
            try {
                cloudlet = new Cloudlet(
                        i,
                        Constants.CLOUDLET_LENGTH,
                        Constants.CLOUDLET_PES,
                        fileSize,
                        outputSize,
                        new UtilizationModelPlanetLabInMemory(
                                files[i].getAbsolutePath(),
                                Constants.SCHEDULING_INTERVAL), utilizationModelNull, utilizationModelNull);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
            cloudlet.setUserId(brokerId);
            cloudlet.setVmId(i);
            list.add(cloudlet);
        }

        return list;
    }

}
