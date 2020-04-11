/**
 * An abstract class for calculating distance (in kilometres) between two coordinates.
 * Taken from: https://dzone.com/articles/distance-calculation-using-3
 */
public abstract class Distance {

    /**
     * Calculates distance between two objects using their
     * latitudes and longitudes.
     * @param lat1 Latitude of the first coordinate.
     * @param lat2 Latitude of the second coordinate.
     * @param lon1 Longitude of the first coordinate.
     * @param lon2 Longitude of the second coordinate.
     * @return The calculated distance as a double in kilometres.
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return dist;
    }

    /**
     * Converts decimal degrees to radians.
     * @param deg The degree value to be converted.
     * @return The converted radian value.
     */
    private static double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }

    /**
     * Converts radians degrees to decimal degrees.
     * @param rad The radian value to be converted.
     * @return The converted degree value.
     */
    private static double rad2deg(double rad)
    {
        return (rad * 180.0 / Math.PI);
    }
}
