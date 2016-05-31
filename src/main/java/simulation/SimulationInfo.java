package simulation;

import java.io.Serializable;

import domain.Position;

/**
 * Class to keep Info about the simulation of a carTracker.
 */
public class SimulationInfo implements Serializable {

    /**
     * SimulationCycles a tracker's car has to drive before it can stop.
     */
    private int cyclesToDrive;

    /**
     * SimulationCycles a tracker's car has to be stationary before it can drive
     * again.
     */
    private int cyclesToWait;

    /**
     * SimulationCycles a tracker's trackingPeriod is still active.
     */
    private int trackingPeriodCycles;

    /**
     * Speed of the tracker's car in degrees per simulationCycle.
     */
    private double speed;

    private final Position startingPosition;

    public SimulationInfo(int cyclesToDrive, int cyclesToWait, int trackingPeriodCycles, double speed, Position startingPosition) {
        this.cyclesToDrive = cyclesToDrive;
        this.cyclesToWait = cyclesToWait;
        this.trackingPeriodCycles = trackingPeriodCycles;
        this.speed = speed;
        this.startingPosition = startingPosition;
    }

    public int getCyclesToDrive() {
        return cyclesToDrive;
    }

    public void decreaseCyclesToDrive() {
        this.cyclesToDrive--;
    }

    public int getCyclesToWait() {
        return cyclesToWait;
    }

    public void decreaseCyclesToWait() {
        this.cyclesToWait--;
    }

    public int getTrackingPeriodCycles() {
        return trackingPeriodCycles;
    }

    public void setTrackingPeriodCycles(int trackingPeriodCycles) {
        this.trackingPeriodCycles = trackingPeriodCycles;
    }

    public void decreaseTrackingPeriodCycles() {
        this.trackingPeriodCycles--;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
