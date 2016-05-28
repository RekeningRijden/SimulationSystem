package simulation;

import communication.Communicator;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import domain.CarTracker;
import domain.Position;
import io.IOHelper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

/**
 * Class to run cars driving in Portugal.
 */
public class Simulator implements Runnable, Serializable {

    /**
     * The time in milliseconds the simulation waits when it has simulated all
     * trackers once before starting again.
     */
    private long simulationInterval;

    /**
     * The degrees per second a car travels when it's driving at 80 km per hour.
     */
    private double carSpeed;

    /**
     * The number of cycles a tracker has to be simulated before it sends data.
     */
    private int trackingPeriodCycles; //use for testing

    /**
     * Indicates if the simulation can start another simulation cycle.
     */
    private boolean running = true;

    /**
     * Trackers being simulated.
     */
    private List<CarTracker> trackers;

    public Simulator(long simulationInterval, int trackingPeriodCycles) {
        this.simulationInterval = simulationInterval;
        this.trackingPeriodCycles = trackingPeriodCycles;
        this.carSpeed = 0.0002 * (this.simulationInterval / 1000);

        try {
            this.trackers = Communicator.getAllCartrackers();
        } catch (IOException ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
            this.running = false;
        }

        if (trackers == null || trackers.isEmpty()) {
            this.running = false;
        }

        before();
    }

    /**
     * Prepare the simulation and start it when ready.
     */
    public void start() {
        before();
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Check for old available carTracker data. If this data exists, import it.
     * Generate new trackers if no previous data was found.
     */
    private void before() {
        for (CarTracker tracker : trackers) {
            SimulationInfo simulationInfo = createSimulationInfo();
            simulationInfo.setTrackingPeriodCycles(1);
            if (tracker.getTrackingPeriods().isEmpty()) {
                tracker.setInitialSimulationInfo(simulationInfo);
            } else {
                tracker.setSimulationInfo(simulationInfo);
            }
        }
    }

    /**
     * Used for simulating the trackers in cars. A car can be driving or
     * stationary.
     */
    @Override
    public void run() {
        while (running) {
            for (CarTracker tracker : trackers) {
                simulateTracker(tracker);
            }

            IOHelper.serialize(trackers);

            try {
                Thread.sleep(simulationInterval);
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }

    /**
     * Simulate a single carTracker.
     * <p/>
     * Firstly check if the amount of cycles the trackers trackingPeriod has
     * left is zero. If this is true the trackingPeriod has expired. The
     * trackingPeriod's info will be send and a new trackingPeriod will be
     * created for the tracker.
     * <p/>
     * Next determine if the tracker's car is driving or stationary.
     *
     * @param tracker to run.
     */
    private void simulateTracker(CarTracker tracker) {
        SimulationInfo info = tracker.getSimulationInfo();

        if (info.getTrackingPeriodCycles() == 0) {
            tracker.finishTrackingPeriod();
            sendData(tracker);

            tracker.startTrackingPeriod();
            tracker.getSimulationInfo().setTrackingPeriodCycles(trackingPeriodCycles);
        } else {
            info.decreaseTrackingPeriodCycles();
        }

        if (info.getCyclesToDrive() > 0) {
            moveTracker(tracker);

            info.decreaseCyclesToDrive();
        } else if (info.getCyclesToWait() > 0) {
            holdTracker(tracker);

            info.decreaseCyclesToWait();
        } else {
            SimulationInfo newInfo = createSimulationInfo();
            newInfo.setTrackingPeriodCycles(info.getTrackingPeriodCycles());

            tracker.setSimulationInfo(newInfo);
        }
    }

    /**
     * Simulate a given tracker driving at a certain speed in degrees. Positive
     * speed is driving north and negative speed is driving south.
     * <p/>
     * If the tracker has reached a country border the tracker's car will be
     * turned around.
     * <p/>
     * After the simulation add a new current position to the trackingPeriod of
     * the tracker.
     *
     * @param tracker to run.
     */
    private static void moveTracker(CarTracker tracker) {
        Position lastPosition = tracker.getLastPosition();
        double speed = tracker.getSimulationInfo().getSpeed();

        if (Constants.NORTH_BORDER < lastPosition.getLatitude() + speed
                || Constants.SOUTH_BORDER > lastPosition.getLatitude() + speed) {

            tracker.getSimulationInfo().setSpeed(speed * -1);
        }

        double newLatitude = lastPosition.getLatitude() + tracker.getSimulationInfo().getSpeed();
        tracker.getCurrentTrackingPeriod().addPosition(
                new Position(newLatitude, lastPosition.getLongitude()));
    }

    /**
     * Simulate a tracker's car being stationary.
     * <p/>
     * After the simulation add a new current position to the trackingPeriod of
     * the tracker.
     *
     * @param tracker to run/
     */
    private static void holdTracker(CarTracker tracker) {
        tracker.getSimulationInfo().setSpeed(0.0);

        tracker.getCurrentTrackingPeriod().addPosition(
                tracker.getLastPosition().copy());
    }

    /**
     * Stop the simulation when the current simulation cycle has finished.
     */
    public void stop() {
        running = false;
    }

    /**
     * Called after a trackingPeriod of a tracker has expired. At this point
     * data has to be send. Data will also be saved locally in a file.
     *
     * @param tracker to send the data from.
     */
    private void sendData(CarTracker tracker) {
        try {
            Communicator.postTrackingPositionsForTracker(tracker);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //<editor-fold desc="Utility Methods">

    /**
     * Create a new simulationInfo object with random values.
     *
     * @return simulationInfo object.
     */
    private SimulationInfo createSimulationInfo() {
        int cyclesToDrive = getRandomCycles(5, 500);
        if (new Random().nextBoolean()) {
            cyclesToDrive = 0;
        }

        int cyclesToWait = getRandomCycles(10, 1000);
        Position startingPosition = getPositionWithinBounds();

        return new SimulationInfo(
                cyclesToDrive,
                cyclesToWait,
                trackingPeriodCycles,
                getCarSpeed(),
                startingPosition);
    }

    /**
     * Get a random amount of simulation cycles between a given range.
     *
     * @param min value of the range.
     * @param max value of the range.
     * @return amount of simulation cycles.
     */
    private static int getRandomCycles(int min, int max) {
        return ThreadLocalRandom.current().nextInt((max - min) + 1) + min;
    }

    /**
     * Get a random position within the country's borders.
     *
     * @return position within the country's borders.
     */
    private static Position getPositionWithinBounds() {
        double latitude = ThreadLocalRandom.current().nextDouble(
                Constants.SOUTH_BORDER, Constants.NORTH_BORDER);

        double longitude = ThreadLocalRandom.current().nextDouble(
                Constants.WEST_BORDER, Constants.EAST_BORDER);

        return new Position(latitude, longitude);
    }

    /**
     * Get the speed the car of a tracker has to drive. This can be the
     * predetermined car speed in positive or negative values.
     *
     * @return car speed.
     */
    private double getCarSpeed() {
        double multiplier = ThreadLocalRandom.current().nextBoolean() ? 1.0 : -1.0;
        return carSpeed * multiplier;
    }
    //</editor-fold>

    public boolean isRunning() {
        return running;
    }
}
