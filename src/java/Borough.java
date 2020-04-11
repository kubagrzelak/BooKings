import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Borough of London in our implementation of
 * a map.
 * @version 2020.03.25
 */
public class Borough {

    // the official name of the borough
    private String name;
    // how many listings from the dataset are in this borough
    private int listingCount;
    // the listings from the dataset which are in this borough
    private List<Listing> listings;

    /**
     * Constructor for Borough object.
     * Set initial values.
     * @param name The name of the borough.
     */
    public Borough(String name) {
        this.name = name;
        listingCount = 0;
        listings = new ArrayList<>();
    }

    /**
     * Increment the number of listings in the borough.
     */
    public void addCount() {
        listingCount += 1;
    }

    /**
     * Add a listing to the listings in the borough.
     */
    public void addListing(Listing listing) { listings.add(listing); }

    /**
     * Accessor method for the name of the borough.
     * @return Name of the borough.
     */
    public String getName() {
        return name;
    }

    /**
     * Accessor method for the number of listings in the borough.
     * @return Number of listings in the borough.
     */
    public int getCount() {
        return listingCount;
    }

    /**
     * Accessor method for the list of listings in the borough.
     * @return List of listings in the borough.
     */
    public List<Listing> getListings() { return listings; }

    /**
     * Clear the borough of all contained information except it's name.
     */
    public void clear() {
        listingCount = 0;
        listings.clear();
    }
}
