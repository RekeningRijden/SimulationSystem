/**
 * Created by Sam on 8-3-2016.
 */
public class Car {

    private Long id;
    private Location location;

    public Car(Long id, Location location) {
        this.id = id;
        this.location = location;
    }

    public void moveBy(double longtitiude, double lattitude) {
        location.addToLongtitude(longtitiude);
        location.addToLattitude(lattitude);
    }

    @Override
    public String toString(){
        return "Id: " + id + " / " + location.toString();
    }
}
