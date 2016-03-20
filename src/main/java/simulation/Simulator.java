package simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import domain.CarTracker;
import domain.Position;

/**
 * Class to simulate cars driving in Portugal.
 */
public class Simulator {

    /**
     * The time in milliseconds the simulation waits when it has
     * simulated all trackers once before starting again.
     */
    private static final long SIMULATION_INTERVAL = 30000; //30 seconds

    /**
     * The amount of trackers to simulate.
     */
    private static final long AMOUNT_OF_TRACKERS = 1;

    /**
     * The degrees per second a car travels when it's driving at 80 km per hour.
     */
    private static final double CAR_SPEED = (0.0002 * (SIMULATION_INTERVAL / 1000));

    /**
     * The number of cycles a tracker has to be simulated before it sends data.
     */
    //Als de simulation interval te klein wordt (bijv. 30) dan komt er een devide by zero exception
    private static final int TRACKING_PERIOD_CYCLES = (int) (14400 / (SIMULATION_INTERVAL / 1000)); //14400 sec = 4 hours

    private boolean running = true;

    private List<CarTracker> trackers;

    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.before();
        simulator.simulate();
    }

    /**
     * Get the simulation ready by creating all the trackers and their belonging simulation info.
     */
    private void before() {
        trackers = new ArrayList<>();
        for (long i = 0; i < AMOUNT_OF_TRACKERS; i++) {
            SimulationInfo simulationInfo = createSimulationInfo();
            simulationInfo.setTrackingPeriodCycles(getRandomCycles(10, 480));

            trackers.add(new CarTracker(i, simulationInfo));
        }
    }

    /**
     * Used for simulating the trackers in cars. A car can be driving or stationary.
     */
    private void simulate() {
        while (running) {
            for (CarTracker tracker : trackers) {
                simulateTracker(tracker);
            }

            try {
                //Thread.sleep(SIMULATION_INTERVAL);
                Thread.sleep(30);
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * Simulate a single carTracker.
     * <p/>
     * Firstly check if the amount of cycles the trackers trackingPeriod has left is zero.
     * If this is true the trackingPeriod has expired. The trackingPeriod's info will be send
     * and a new trackingPeriod will be created for the tracker.
     * <p/>
     * Next determine if the tracker's car is driving or stationary.
     *
     * @param tracker to simulate.
     */
    private void simulateTracker(CarTracker tracker) {
        SimulationInfo info = tracker.getSimulationInfo();

        if (info.getTrackingPeriodCycles() == 0) {
            tracker.finishTrackingPeriod();
            sendData(tracker);

            tracker.startTrackingPeriod();
            tracker.getSimulationInfo().setTrackingPeriodCycles(100);
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
     * Simulate a given tracker driving at a certain speed in degrees.
     * Positive speed is driving north and negative speed is driving south.
     * <p/>
     * If the tracker has reached a country border the tracker's car will be turned around.
     * <p/>
     * After the simulation add a new current position to the trackingPeriod of the tracker.
     *
     * @param tracker to simulate.
     */
    private void moveTracker(CarTracker tracker) {
        Position lastPosition = tracker.getLastPosition();
        double speed = tracker.getSimulationInfo().getSpeed();

        if (Constants.NORTH_BORDER < lastPosition.getLatitude() + speed
                || Constants.SOUTH_BORDER > lastPosition.getLatitude() + speed) {

            tracker.getSimulationInfo().setSpeed((speed * -1));
        }

        double newLatitude = lastPosition.getLatitude() + tracker.getSimulationInfo().getSpeed();
        tracker.getCurrentTrackingPeriod().addPosition(
                new Position(newLatitude, lastPosition.getLongitude()));
    }

    /**
     * Simulate a tracker's car being stationary.
     * <p/>
     * After the simulation add a new current position to the trackingPeriod of the tracker.
     *
     * @param tracker to simulate/
     */
    private void holdTracker(CarTracker tracker) {
        tracker.getSimulationInfo().setSpeed(0.0);

        tracker.getCurrentTrackingPeriod().addPosition(
                tracker.getLastPosition().copy());
    }

    /**
     * Stop the simulation.
     */
    public void stop() {
        running = false;
    }

    /**
     * Called after a trackingPeriod of a tracker has expired.
     * At this point data has to be send.
     *
     * @param tracker to send the data from.
     */
    private void sendData(CarTracker tracker) {
        //Eric
        System.out.println("CarId= " + tracker.getId() + " position long" + tracker.getLastPosition().getLongitude() + " position lat= " + tracker.getLastPosition().getLatitude());
        
        
        
    }

    //<editor-fold desc="Utility Methods">

    /**
     * Create a new simulationInfo object with random values.
     *
     * @return simulationInfo object.
     */
    private SimulationInfo createSimulationInfo() {
        int cyclesToDrive = getRandomCycles(5, 500);
        int cyclesToWait = getRandomCycles(10, 1000);
        Position startingPosition = getPositionWithinBounds();

        return new SimulationInfo(
                cyclesToDrive,
                cyclesToWait,
                TRACKING_PERIOD_CYCLES,
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
    private int getRandomCycles(int min, int max) {
        return ThreadLocalRandom.current().nextInt((max - min) + 1) + min;
    }

    /**
     * Get a random position within the country's borders.
     *
     * @return position within the country's borders.
     */
    private Position getPositionWithinBounds() {
        double latitude = ThreadLocalRandom.current().nextDouble(
                Constants.SOUTH_BORDER, Constants.NORTH_BORDER);

        double longitude = ThreadLocalRandom.current().nextDouble(
                Constants.WEST_BORDER, Constants.EAST_BORDER);

        return new Position(latitude, longitude);
    }

    /**
     * Get the speed the car of a tracker has to drive.
     * This can be the predetermined car speed in positive or negative values.
     *
     * @return car speed.
     */
    private double getCarSpeed() {
        double multiplier = ThreadLocalRandom.current().nextBoolean() ? 1.0 : -1.0;
        return CAR_SPEED * multiplier;
    }
    //</editor-fold>
}
