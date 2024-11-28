package utils;


import java.io.*;

public class ResourceWastage {
    private static double[][] Memory, Processor;
    private File memFile = new File("MemoryMatrix.txt");
    private File execFile = new File("ProcessorMatrix.txt");

    public ResourceWastage() {
        Memory = new double[Constants.NO_OF_TASKS][Constants.NO_OF_DATA_CENTERS];
        Processor = new double[Constants.NO_OF_TASKS][Constants.NO_OF_DATA_CENTERS];
        try {
            if (memFile.exists() && execFile.exists()) {
                readCostMatrix();
            } else {
                initCostMatrix();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCostMatrix() throws IOException {
        BufferedWriter commBufferedWriter = new BufferedWriter(new FileWriter(memFile));
        BufferedWriter execBufferedWriter = new BufferedWriter(new FileWriter(execFile));

        for (int i = 0; i < Constants.NO_OF_TASKS; i++) {
            for (int j = 0; j < Constants.NO_OF_DATA_CENTERS; j++) {
                Memory[i][j] = Math.random() * 600 + 20;
                Processor[i][j] = Math.random() * 500 + 10;
                commBufferedWriter.write(String.valueOf(Memory[i][j]) + ' ');
                execBufferedWriter.write(String.valueOf(Processor[i][j]) + ' ');
            }
            commBufferedWriter.write('\n');
            execBufferedWriter.write('\n');
        }
        commBufferedWriter.close();
        execBufferedWriter.close();
    }

    @SuppressWarnings("resource")
    private void readCostMatrix() throws IOException {
        BufferedReader commBufferedReader = new BufferedReader(new FileReader(memFile));

        int i = 0, j = 0;
        do {
            String line = commBufferedReader.readLine();
            for (String num : line.split(" ")) {
                Memory[i][j++] = new Double(num);
            }
            ++i;
            j = 0;
        } while (commBufferedReader.ready());


        BufferedReader execBufferedReader = new BufferedReader(new FileReader(execFile));

        i = j = 0;
        do {
            String line = execBufferedReader.readLine();
            for (String num : line.split(" ")) {
                Processor[i][j++] = new Double(num);
            }
            ++i;
            j = 0;
        } while (execBufferedReader.ready());
    }

    public static double[][] getMemMatrix() {
        return Memory;
    }

    public static double[][] getPrMatrix() {
        return Processor;
    }
}
