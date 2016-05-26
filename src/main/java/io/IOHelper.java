package io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import domain.CarTracker;
import java.util.ArrayList;

/**
 * Class for saving and loading simulation data to a file.
 */
public class IOHelper {

    public IOHelper() {
        // empty constructor
    }

    /**
     * File to save to, and retrieve data from.
     */
    private static final String FILE = "C:\\School\\PTS6\\SimulationSystem\\trackers.tmp";

    /**
     * Save a list of carTrackers to a file.
     *
     * @param carTrackers to save.
     */
    public static void serialize(List<CarTracker> carTrackers) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(getFile()));
            oos.writeObject((ArrayList) carTrackers);
        } catch (IOException ex) {
            Logger.getLogger(IOHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(oos);
        }
    }

    /**
     * Load previously used saved carTrackers from a file.
     *
     * @return a list of carTrackers.
     * @throws IOException to be thrown.
     * @throws ClassNotFoundException to be thrown.
     */
    @SuppressWarnings("unchecked")
    public static List<CarTracker> deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getFile()));
        List<CarTracker> carTrackers = (List<CarTracker>) ois.readObject();
        close(ois);

        return carTrackers;
    }

    /**
     * @return true if previous simulation data is available to use.
     */
    public static boolean previousDataAvailable() {
        return getFile().exists();
    }

    /**
     * Close a object implementing the closeable interface.
     *
     * @param closeable object to close.
     */
    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ex) {
                Logger.getLogger(IOHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @return the file to save and load data from.
     */
    private static File getFile() {
        return new File(FILE);
    }
}
