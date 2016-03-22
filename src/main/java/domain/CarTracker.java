package domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import simulation.SimulationInfo;

/**
 * Class to keep info about a carTracker.
 */
public class CarTracker {

    private Long id;
    private String authorisationCode;
    private List<TrackingPeriod> trackingPeriods;

    private SimulationInfo simulationInfo;

    public CarTracker(Long id, SimulationInfo info) {
        this.id = id;
        this.simulationInfo = info;
        this.trackingPeriods = new ArrayList<>();

        TrackingPeriod trackingPeriod = new TrackingPeriod(0L);
        trackingPeriod.addPosition(info.getStartingPosition());
        trackingPeriods.add(trackingPeriod);
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
    
    public void generateAuthorisationCode() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(UUID.randomUUID().toString().getBytes());

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }

            this.authorisationCode  =  sb.toString();

        } catch (NoSuchAlgorithmException e) {
        }
    }
    
    public void saveAuthorisationCodeFile() {
        try {
            byte data[] = getAuthorisationCode().getBytes();
            Path file = Paths.get("authcodetestfile");
            Files.write(file, data);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    //<editor-fold desc="Getters/Setters">
    public Long getId() {
        return id;
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
