/**
 * Created by Sam on 8-3-2016.
 */
public class Location {

    private double longtitude;
    private double lattitude;

    public Location(double longtitude, double lattitude) {
        this.longtitude = longtitude;
        this.lattitude = lattitude;
    }

    public void addToLongtitude(double distance) {
        longtitude += distance;
    }

    public void addToLattitude(double distance) {
        lattitude += distance;
    }

    @Override
    public String toString(){
        return "Longtitude: " + longtitude + " / Lattitude: " + lattitude;
    }
}
