import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Sam on 8-3-2016.
 */
public class Main {

    public static void main(String[] args) {
        List<Car> cars = new ArrayList<Car>();

        double startingLongtitude = 51.457519;
        double startingLattitude = 5.492169;

        for (int j = 0; j < 10; j++) {
            double longtitudeOffset = ThreadLocalRandom.current().nextDouble(-2, 2);
            double lattitudeOffset = ThreadLocalRandom.current().nextDouble(-2, 2);

            Location location = new Location((startingLongtitude + longtitudeOffset), (startingLattitude + lattitudeOffset));
            cars.add(new Car((long) j, location));
        }

        for (int i = 0; i < 1000; i++) {
            for(Car car : cars){
                car.moveBy(0.01, 0.00);

                System.out.println(car.toString());
            }
        }
    }
}
