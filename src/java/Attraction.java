
/**
 * Represents one attraction that is used to see if listings
 * are nearby this place. This is essentially one row in the
 * data table. Each column has a corresponding field.
 */
public class Attraction {


    // The name of the attraction.
    private String name;

    // The location of attraction on a map.
    private double latitude;
    private double longitude;

    /**
     * Creates one attraction with its parameters.
     * @param name Name of an attraction.
     * @param latitude Latitude value of an attraction.
     * @param longitude Longitude value of an attraction.
     */
    public Attraction(String name, double latitude,
                   double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return Attraction's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Attraction's latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return Attraction's longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

