package org.cloudbus.cloudsim.container.utils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by sareh on 30/07/15.
 */
public class CostumeCSVWriter {
    String fileAddress;
    Writer fileWriter;

    public CostumeCSVWriter(String fileAddress) throws IOException {
        File f = new File(fileAddress);
        File parent3 = f.getParentFile();
        if (!parent3.exists() && !parent3.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + parent3);
        }
        if (!f.exists())
            f.createNewFile();
        setFileAddress(fileAddress);


    }

    public void writeTofile(String[] entries) throws IOException {

    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }
}




