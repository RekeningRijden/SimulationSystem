package domain;

import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import simulation.SimulationInfo;

/**
 * Class to keep info about a carTracker.
 */
public class CarTracker implements Serializable {

    private Long id;
    @Expose
    private String authorisationCode;

    @Expose
    private List<TrackingPeriod> trackingPeriods;

    private SimulationInfo simulationInfo;

    public CarTracker() {
        this.trackingPeriods = new ArrayList<>();
    }

    /**
     * Start a new trackingPeriod.
     */
    public void startTrackingPeriod() {
        trackingPeriods.add(new TrackingPeriod((long) trackingPeriods.size()));
    }

    /**
     * Finish the current trackingPeriod.
     */
    public void finishTrackingPeriod() {
        getCurrentTrackingPeriod().finishTracking();
    }

    /**
     * @return the current trackingPeriod.
     */
    public TrackingPeriod getCurrentTrackingPeriod() {
        return trackingPeriods.get((trackingPeriods.size() - 1));
    }

    public Position getLastPosition() {
        if (getCurrentTrackingPeriod().getPositions().isEmpty()) {
            return trackingPeriods.get((trackingPeriods.size() - 2)).getLastPosition();
        } else {
            return getCurrentTrackingPeriod().getLastPosition();
        }
    }



    public void saveAuthorisationCodeFile() {
        try {
            byte data[] = getAuthorisationCode().getBytes();
            Path file = Paths.get("authcodetestfile");
            Files.write(file, data);
        } catch (IOException ex) {
            Logger.getLogger(CarTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setInitialSimulationInfo(SimulationInfo info) {
        this.simulationInfo = info;


        TrackingPeriod trackingPeriod = new TrackingPeriod(0L);
        trackingPeriod.addPosition(info.getStartingPosition());
        trackingPeriods.add(trackingPeriod);
    }

    //<editor-fold desc="Getters/Setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorisationCode() {
        return authorisationCode;
    }

    public void setAuthorisationCode(String authorisationCode) {
        this.authorisationCode = authorisationCode;
    }

    public List<TrackingPeriod> getTrackingPeriods() {
        return trackingPeriods;
    }

    public SimulationInfo getSimulationInfo() {
        return simulationInfo;
    }

    public void setSimulationInfo(SimulationInfo simulationInfo) {
        this.simulationInfo = simulationInfo;
    }

    //</editor-fold>
}
