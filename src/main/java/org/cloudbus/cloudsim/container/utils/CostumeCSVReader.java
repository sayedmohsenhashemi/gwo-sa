package org.cloudbus.cloudsim.container.utils;

import java.io.File;
import java.util.List;


/**
 * Run.
 */
public class CostumeCSVReader {
    private static List<String[]> fileData;

    public CostumeCSVReader(File inputFile) {
        // TODO Auto-generated method stub
        try {
//			Log.printLine(inputFile);
            //Get the CSVReader instance with specifying the delimiter to be used

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    public static List<String[]> getFileData() {
        return fileData;
    }


    public static void setFileData(List<String[]> fileData) {
        CostumeCSVReader.fileData = fileData;
    }

}

