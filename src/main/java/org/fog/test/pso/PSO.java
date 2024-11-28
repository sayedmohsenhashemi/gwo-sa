package org.fog.test.pso;

import net.sourceforge.jswarm_pso.Swarm;
import utils.Constants;

public class PSO {
    private static Swarm swarm;
    private static SchedulerParticle particles[];
    private static SchedulerFitnessFunction ff = new SchedulerFitnessFunction();

    public PSO() {
        initParticles();
    }


    public double[] run() {
        swarm = new Swarm(Constants.POPULATION_SIZE, new SchedulerParticle(), ff);

        swarm.setMinPosition(0.9);
        swarm.setMaxPosition(2);
        swarm.setMaxMinVelocity(3);
        swarm.setParticles(particles);
        swarm.setParticleUpdate(new SchedulerParticleUpdate(new SchedulerParticle()));

        for (int i = 0; i < 50; i++) {
            swarm.evolve();
        }

        return swarm.getBestPosition();
    }

    private static void initParticles() {
        particles = new SchedulerParticle[Constants.POPULATION_SIZE];
        for (int i = 0; i < Constants.POPULATION_SIZE; ++i)
            particles[i] = new SchedulerParticle();
    }

}
