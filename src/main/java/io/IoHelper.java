package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import domain.CarTracker;

/**
 * Created by Sam on 22-3-2016.
 */
public class IoHelper {

    public static void serialize(List<CarTracker> carTrackers) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(getFile()));
        oos.writeObject(carTrackers);
        oos.close();
    }

    public static List<CarTracker> deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(getFile()));
        List<CarTracker> carTrackers = (List<CarTracker>) ois.readObject();
        ois.close();

        return carTrackers;
    }

    private static File getFile() {
        return new File("trackers.tmp");
    }
}
