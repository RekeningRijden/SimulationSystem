package web;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import simulation.Simulator;

/**
 * @author Sam
 */
@Named
@ApplicationScoped
public class SimulatorController implements Serializable {

    private long simulationInterval = 3000; //3 seconds
    private long amountOfTrackers = 1;
    //(int) (14400 / (SIMULATION_INTERVAL / 1000)); //14400 sec = 4 hours
    private int trackingPeriodCycles = 1;  //use for testing

    private Simulator simulator;

    /**
     * Start the simulation.
     */
    public void start() {
        simulator = new Simulator(simulationInterval, amountOfTrackers, trackingPeriodCycles);
        simulator.start();
    }

    /**
     * Stop the simulation.
     */
    public void stop() {
        simulator.stop();
    }

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public long getSimulationInterval() {
        return simulationInterval;
    }

    public void setSimulationInterval(long simulationInterval) {
        this.simulationInterval = simulationInterval;
    }

    public long getAmountOfTrackers() {
        return amountOfTrackers;
    }

    public void setAmountOfTrackers(long amountOfTrackers) {
        this.amountOfTrackers = amountOfTrackers;
    }

    public int getTrackingPeriodCycles() {
        return trackingPeriodCycles;
    }

    public void setTrackingPeriodCycles(int trackingPeriodCycles) {
        this.trackingPeriodCycles = trackingPeriodCycles;
    }

    public boolean isRunning() {
        return simulator != null && simulator.isRunning();
    }
    //</editor-fold>
}
