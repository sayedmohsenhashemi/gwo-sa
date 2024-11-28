package org.fog.test.AntColony;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.Helper;
import org.cloudbus.cloudsim.power.PlanetLabHelper;
import org.cloudbus.cloudsim.power.RunnerAbstract;
import utils.Constants;
import utils.DatacenterCreator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

public class AntColony {
    private static Datacenter[] datacenter;
    private LocalSearch localSearch;

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


    public AntColony() {
        this.localSearch = localSearch;
    }

    static class ant_struct {
        int[] tour;
        boolean[] visited;
        int tour_length;
    }

    ant_struct ant[];
    ant_struct best_so_far_ant;
    ant_struct restart_best_ant;

    double pheromone[][];
    double total[][];
    double prob_of_selection[];
    int n_ants = 40;
    static int rov = 2;
    double alpha = 0.3;
    double beta = 0.2;


    double HEURISTIC(int m, int n) {
        return (1.0 / (Tsp.instance.distance[m][n] + 0.1));
    }

    public static final double EPSILON = 0.001;

    void allocate_ants() {
        int i;

        ant = new ant_struct[n_ants];

        for (i = 0; i < n_ants; i++) {
            ant[i] = new ant_struct();
            ant[i].tour = new int[Tsp.n + 1];
            ant[i].visited = new boolean[Tsp.n];
        }

        best_so_far_ant = new ant_struct();
        best_so_far_ant.tour = new int[Tsp.n + 1];
        best_so_far_ant.visited = new boolean[Tsp.n];
        restart_best_ant = new ant_struct();
        restart_best_ant.tour = new int[Tsp.n + 1];
        restart_best_ant.visited = new boolean[Tsp.n];
        prob_of_selection = new double[rov + 1];

        for (i = 0; i < rov + 1; i++) {
            prob_of_selection[i] = Double.POSITIVE_INFINITY;
        }
    }

    int find_best() {
        int min;
        int k, k_min;

        min = ant[0].tour_length;
        k_min = 0;
        for (k = 1; k < n_ants; k++) {
            if (ant[k].tour_length < min) {
                min = ant[k].tour_length;
                k_min = k;
            }
        }
        return k_min;
    }

    int find_worst() {
        int max;
        int k, k_max;

        max = ant[0].tour_length;
        k_max = 0;
        for (k = 1; k < n_ants; k++) {
            if (ant[k].tour_length > max) {
                max = ant[k].tour_length;
                k_max = k;
            }
        }
        return k_max;
    }

    void init_pheromone_trails(double initial_trail) {
        int i, j;

        for (i = 0; i < Tsp.n; i++) {
            for (j = 0; j <= i; j++) {
                pheromone[i][j] = initial_trail;
                pheromone[j][i] = initial_trail;
                total[i][j] = initial_trail;
                total[j][i] = initial_trail;
            }
        }
    }

    void evaporation_nn_list(ant_struct a) {
        int i, j, help_node;
        double d_tau;

        d_tau = 1.0 / a.tour_length;
        for (i = 0; i < Tsp.n; i++) {
            for (j = 0; j < rov; j++) {
                help_node = Tsp.instance.nn_list[i][j];
                pheromone[j][help_node] = pheromone[j][help_node] - d_tau * (pheromone[j][help_node]);

            }
        }
    }

    void global_update_pheromone(ant_struct a) {
        int i, j, h;
        double d_tau;

        d_tau = 1.0 / a.tour_length;
        for (i = 0; i < Tsp.n; i++) {
            j = a.tour[i];
            h = a.tour[i + 1];
            pheromone[j][h] = pheromone[j][h] + d_tau * (1 - pheromone[j][h]);
        }
    }

    void global_update_pheromone_weighted(ant_struct a, int weight) {
        int i, j, h;
        double d_tau;

        d_tau = (double) weight / (double) a.tour_length;
        for (i = 0; i < Tsp.n; i++) {
            j = a.tour[i];
            h = a.tour[i + 1];
            pheromone[j][h] = pheromone[j][h] + d_tau * (1 - pheromone[j][h]);
            pheromone[h][j] = pheromone[j][h];
        }
    }

    void compute_total_information() {
        int i, j;

        for (i = 0; i < Tsp.n; i++) {
            for (j = 0; j < i; j++) {
                total[i][j] = Math.pow(pheromone[i][j], alpha) * Math.pow(HEURISTIC(i, j), beta);
                total[j][i] = total[i][j];
            }
        }
    }

    void compute_nn_list_total_information() {
        int i, j, h;

        for (i = 0; i < Tsp.n; i++) {
            for (j = 0; j < rov; j++) {
                h = Tsp.instance.nn_list[i][j];
                if (pheromone[i][h] < pheromone[h][i])
                    pheromone[h][i] = pheromone[i][h];
                total[i][h] = Math.pow(pheromone[i][h], alpha) * Math.pow(HEURISTIC(i, h), beta);
                total[h][i] = total[i][h];
            }
        }
    }

    public static void main(String[] args) {
        boolean enableOutput = true;
        boolean outputToFile = false;
        String str = "mc";
        String inputFolder = AntColony.class.getClassLoader().getResource("workload").getPath();
        String workload = "workload";
        String parameter = "1.2";
        String AllocationPolicy = "AntColony";
        String outputFolder = "output";

        try {
            int user = 15;
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;

            CloudSim.init(user, calendar, trace_flag);

            datacenter = new Datacenter[Constants.NUMBER_OF_Nodes];
            for (int i = 0; i < Constants.NUMBER_OF_Nodes; i++) {
                datacenter[i] = DatacenterCreator.createDatacenter("Datacenter_" + i);
            }

            HashSet<Integer> dcIds = new HashSet<>();
            new HashMap<>();
            for (Datacenter dc : datacenter) {
                if (!dcIds.contains(dc.getId()))
                    dcIds.add(dc.getId());
            }
            dcIds.iterator();
            AntColony newRunner = new AntColony();
            newRunner.new Nodes(enableOutput, outputToFile, inputFolder, outputFolder, workload, AllocationPolicy, str, parameter);

        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("an unexpected error");
        }
    }


}
