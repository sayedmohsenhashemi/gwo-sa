package org.fog.test.AntColony;

import java.util.Random;

public class Utilities {

    public static final int MAXIMUM_NO_TRIES = 100;

    private static Random random;

    static int seed;

    static double mean(int[] values, int max) {
        int j;
        double m;

        m = 0.;
        for (j = 0; j < max; j++) {
            m += (double) values[j];
        }
        m = m / (double) max;
        return m;
    }

    static double meanr(double[] values, int max) {
        int j;
        double m;

        m = 0.;
        for (j = 0; j < max; j++) {
            m += values[j];
        }
        m = m / (double) max;
        return m;
    }

    static double std_deviation(int[] values, int max, double mean) {
        int j;
        double dev = 0.;

        if (max <= 1)
            return 0.;
        for (j = 0; j < max; j++) {
            dev += ((double) values[j] - mean) * ((double) values[j] - mean);
        }
        return Math.sqrt(dev / (double) (max - 1));
    }

    static double std_deviationr(double[] values, int max, double mean) {
        int j;
        double dev;

        if (max <= 1)
            return 0.;
        dev = 0.;
        for (j = 0; j < max; j++) {
            dev += ((double) values[j] - mean) * ((double) values[j] - mean);
        }
        return Math.sqrt(dev / (double) (max - 1));
    }

    static int best_of_vector(int[] values, int l) {
        int min, k;

        k = 0;
        min = values[k];
        for (k = 1; k < l; k++) {
            if (values[k] < min) {
                min = values[k];
            }
        }
        return min;
    }

    static int worst_of_vector(int[] values, int l) {
        int max, k;

        k = 0;
        max = values[k];
        for (k = 1; k < l; k++) {
            if (values[k] > max) {
                max = values[k];
            }
        }
        return max;
    }

    static double quantil(int v[], double q, int l) {
        int i, j;
        double tmp;

        tmp = q * (double) l;
        if ((double) ((int) tmp) == tmp) {
            i = (int) tmp;
            j = (int) (tmp + 1.);
            return ((double) v[i - 1] + (double) v[j - 1]) / 2.;
        } else {
            i = (int) (tmp + 1.);
            return v[i - 1];
        }
    }

    static void swap(int v[], int i, int j) {
        int tmp;

        tmp = v[i];
        v[i] = v[j];
        v[j] = tmp;
    }

    static void sort(int v[], int left, int right) {
        int k, last;

        if (left >= right)
            return;
        swap(v, left, (left + right) / 2);
        last = left;
        for (k = left + 1; k <= right; k++)
            if (v[k] < v[left])
                swap(v, ++last, k);
        swap(v, left, last);
        sort(v, left, last);
        sort(v, last + 1, right);
    }

    static void swap2(int v[], int v2[], int i, int j) {
        int tmp;

        tmp = v[i];
        v[i] = v[j];
        v[j] = tmp;
        tmp = v2[i];
        v2[i] = v2[j];
        v2[j] = tmp;
    }

    static void sort2(int v[], int v2[], int left, int right) {
        int k, last;

        if (left >= right)
            return;
        swap2(v, v2, left, (left + right) / 2);
        last = left;
        for (k = left + 1; k <= right; k++)
            if (v[k] < v[left])
                swap2(v, v2, ++last, k);
        swap2(v, v2, left, last);
        sort2(v, v2, left, last);
        sort2(v, v2, last + 1, right);
    }

    static double ran01(long idum) {
        if (random == null) {
            random = new Random(seed);
        }

        return random.nextDouble();
    }

    static int random_number(long idum) {
        if (random == null) {
            random = new Random(seed);
        }

        return random.nextInt(2147483647);
    }

    static int[][] generate_int_matrix(int n, int m) {
        return new int[n][m];
    }

    static double[][] generate_double_matrix(int n, int m) {
        return new double[n][m];
    }

    int min, k;


}
