/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.util;

import org.cloudbus.cloudsim.Cloudlet;

import java.util.List;

/**
 * This interface defines what a workload model should provide. A workload model generates a list of
 * jobs that can be dispatched to a resource by {@link Workload}.
 *
 * @author Marcos Dias de Assuncao
 * @see Workload
 * @see WorkloadFileReader
 * @since 5.0
 */
public interface WorkloadModel {

    /**
     * Returns a list with the jobs generated by the workload.
     *
     * @return a list with the jobs generated by the workload.
     */
    List<Cloudlet> generateWorkload();

}
