package org.fog.test.AntColony;

public class Tsp {
    static class point {
        double x;
        double y;
    }

    static class problem {
        String name;
        String edge_weight_type;
        int optimum;
        int n;
        int n_near;
        point[] nodeptr;
        int[][] distance;
        int[][] nn_list;
    }

    static int n;
    static problem instance;

    static double dtrunc(double x) {
        int k;
        k = (int) x;
        x = (double) k;
        return x;
    }

    static int round_distance(int i, int j) {
        double xd = instance.nodeptr[i].x - instance.nodeptr[j].x;
        double yd = instance.nodeptr[i].y - instance.nodeptr[j].y;
        double r = Math.sqrt(xd * xd + yd * yd) + 0.5;

        return (int) r;
    }

    static int ceil_distance(int i, int j) {
        double xd = instance.nodeptr[i].x - instance.nodeptr[j].x;
        double yd = instance.nodeptr[i].y - instance.nodeptr[j].y;
        double r = Math.sqrt(xd * xd + yd * yd);

        return (int) Math.ceil(r);
    }

    static int geo_distance(int i, int j) {
        double deg, min;
        double lati, latj, longi, longj;
        double q1, q2, q3;
        int dd;
        double x1 = instance.nodeptr[i].x, x2 = instance.nodeptr[j].x, y1 = instance.nodeptr[i].y, y2 = instance.nodeptr[j].y;

        deg = dtrunc(x1);
        min = x1 - deg;
        lati = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
        deg = dtrunc(x2);
        min = x2 - deg;
        latj = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;

        deg = dtrunc(y1);
        min = y1 - deg;
        longi = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
        deg = dtrunc(y2);
        min = y2 - deg;
        longj = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;

        q1 = Math.cos(longi - longj);
        q2 = Math.cos(lati - latj);
        q3 = Math.cos(lati + latj);
        dd = (int) (6378.388 * Math.acos(0.5 * ((1.0 + q1) * q2 - (1.0 - q1) * q3)) + 1.0);
        return dd;

    }

    static int att_distance(int i, int j) {
        double xd = instance.nodeptr[i].x - instance.nodeptr[j].x;
        double yd = instance.nodeptr[i].y - instance.nodeptr[j].y;
        double rij = Math.sqrt((xd * xd + yd * yd) / 10.0);
        double tij = dtrunc(rij);
        int dij;

        if (tij < rij)
            dij = (int) tij + 1;
        else
            dij = (int) tij;
        return dij;
    }

    static int[][] compute_distances() {
        int matrix[][] = new int[n][n];

        return matrix;
    }

    static int[][] compute_nn_lists() {
        int i, node, nn;
        int[] distance_vector = new int[n];
        int[] help_vector = new int[n];
        nn = AntColony.rov;
        if (nn >= n)
            nn = n - 1;
        int[][] m_nnear = new int[n][nn];
        for (node = 0; node < n; node++) {

            for (i = 0; i < n; i++) {
                distance_vector[i] = instance.distance[node][i];
                help_vector[i] = i;
            }
            distance_vector[node] = Integer.MAX_VALUE;
            Utilities.sort2(distance_vector, help_vector, 0, n - 1);
            for (i = 0; i < nn; i++) {
                m_nnear[node][i] = help_vector[i];
            }
        }
        return m_nnear;
    }

    static int compute_tour_length(int[] t) {
        int i;
        int tour_length = 0;

        for (i = 0; i < n; i++) {
            tour_length += instance.distance[t[i]][t[i + 1]];
        }
        return tour_length;
    }

    static boolean tsp_check_tour(int[] t) {
        boolean error = false;

        int i;
        int[] used = new int[n];
        int size = n;

        if (t == null) {
            System.err.println("error: permutation is not initialized!");
            System.exit(1);
        }

        for (i = 0; i < size; i++) {
            if (used[t[i]] != 0) {
                error = true;
            } else
                used[t[i]] = 1;
        }

        if (!error)
            for (i = 0; i < size; i++) {
                if (used[i] == 0) {
                    error = true;
                }
            }

        if (!error)
            if (t[0] != t[size]) {
                error = true;
            }

        if (!error)
            return true;
        for (i = 0; i < size; i++)
            System.err.println(t[i]);
        System.out.println();

        return false;
    }
}
