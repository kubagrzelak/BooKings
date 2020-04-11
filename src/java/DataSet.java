import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Stores the loaded dataset of properties and allows for functionality to filter the dataset to specific price ranges, add
 * favourites and obtain these filtered lists.
 */
public class DataSet {

    private static final DataLoader dataLoader = new DataLoader();
    private List<Listing> datasetList;
    private List<Listing> filteredDatasetList;
    private List<Attraction> attractionsDataSet;
    private List<Listing> favourites;

    /**
     * Load the dataset using the dataLoader.
     */
    public DataSet(String filename) {
        datasetList = dataLoader.loadListings(filename);
        filteredDatasetList = datasetList;
        attractionsDataSet = dataLoader.loadAttractions();

        favourites = new ArrayList<>();
    }

    /**
     * Reload the dataset using the dataLoader.
     */
    public void reloadDataSet() {
        datasetList = dataLoader.loadListings("airbnb-london");
    }

    /**
     * Get the stored filtered dataset.
     * @return dataset
     */
    public List<Listing> getFilteredDatasetList() {
        return filteredDatasetList;
    }

    public List<Listing> getDatasetList() {
        return datasetList;
    }

    public List<Attraction> getAttractionsDataSet()
    {
        return attractionsDataSet;
    }

    public Pair<Integer, Integer> getPriceRange() {
        int minPrice = datasetList.get(0).getPrice();
        int maxPrice = 0;
        for (Listing listing : datasetList) {
            int listingPrice = listing.getPrice();
            if (listingPrice > maxPrice) {
                maxPrice = listingPrice;
            }
            if (listingPrice < minPrice) {
                minPrice = listingPrice;
            }
        }
        return new Pair<>(minPrice, maxPrice);
    }

    /**
     * Filters and stores the dataset to a price range.
     * @param minPrice , minimum price of listings.
     * @param maxPrice , maximum price of listings.
     */
    public void filterPrice(int minPrice, int maxPrice) {
        List<Listing> listings = new ArrayList<>();
        for (Listing listing : datasetList) {
            if (listing.getPrice() >= minPrice && listing.getPrice() <= maxPrice) {
                listings.add(listing);
            }
        }
        filteredDatasetList = listings;
    }

    /**
     * Filters and returns the dataset to neighbourhood(s).
     * @param neighbourhood , neighbourhood(s) of listings.
     * @return The filtered dataset.
     */
    public List<Listing> filterNeighbourhood(String ... neighbourhood) {
        List<Listing> listings = new ArrayList<>();
        List<String> neighbourhoods = Arrays.asList(neighbourhood);
        for (Listing listing : filteredDatasetList) {
            if (neighbourhoods.contains(listing.getNeighbourhood())) {
                listings.add(listing);
            }
        }
        return listings;
    }

    public List<Listing> getFavourites() {
        return favourites;
    }

    public void favourite(Listing listing) {
        if (favourites.contains(listing)) {
            removeFavourite(listing);
        }
        else {
            addFavourite(listing);
        }
    }

    private void addFavourite(Listing listing) {
        favourites.add(listing);
    }

    private void removeFavourite(Listing listing) {
        for (Iterator it = favourites.iterator(); it.hasNext(); ) {
            Listing favourite = (Listing) it.next();
            if (favourite.equals(listing)) {
                it.remove();
            }
        }
    }
}
