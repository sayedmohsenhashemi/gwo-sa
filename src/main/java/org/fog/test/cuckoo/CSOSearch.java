package org.fog.test.cuckoo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class CSOSearch {

    private int landa = 1;

    public void position(String inpFile, String outFile, int col, int row) {
        try {
            row++;
            String data[][] = new String[row][col];

            for (int j = 0; j < col; j++) {
                ArrayList<Double> Xnh = new ArrayList<Double>();
                try {
                    for (int i = 1; i < row; i++) {
                        Xnh.add(Double.parseDouble(data[i][j]));
                    }
                    Cuckoo_Run cs = new Cuckoo_Run(Xnh);
                    Cuckoo_Run res = cs.NumEggs(Xnh.size());
                    for (int Xg = 1; Xg < row; Xg++) {
                        data[Xg][j] = "" + res.vars.get(landa * (Xg - Xnh.size()));
                    }
                } catch (Exception ex) {
                    System.out.println("Exceptions " + ex);
                }
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (j == col - 1) {
                        bw.write(data[i][j]);
                    } else {
                        bw.write(data[i][j] + ",");
                    }
                }
                bw.write("\n");
            }
            bw.close();
        } catch (Exception ex) {
            System.out.println("Ex :" + ex);
        }
    }
}
