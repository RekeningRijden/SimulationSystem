package web;

import communication.Communicator;
import domain.CarTracker;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import simulation.Simulator;

/**
 * @author Sam
 */


@Named
@ApplicationScoped
public class SimulatorController implements Serializable {

    private long simulationInterval = 3000; //3 seconds
    //(int) (14400 / (SIMULATION_INTERVAL / 1000)); //14400 sec = 4 hours
    private int trackingPeriodCycles = 1;  //use for testing

    private Simulator simulator;

    private transient Thread simulationThread;

    /**
     * Start the simulation which is running on a newly spawned thread.
     */
    public void start() {
        List<CarTracker> trackers = null;
        try {
            trackers = Communicator.getAllCartrackers();
        } catch (IOException ex) {
            Logger.getLogger(SimulatorController.class.getName()).log(Level.SEVERE, null, ex);
            RequestContext.getCurrentInstance().execute("alert('failed to start simulation');");
        }

        if (trackers != null) {
            simulator = new Simulator(simulationInterval, trackingPeriodCycles, trackers);

            simulationThread = new Thread(simulator);
            simulationThread.start();
        }
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
