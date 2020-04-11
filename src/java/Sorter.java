import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sorter class that is able to sort Lists based on property attributes.
 */
public abstract class Sorter {

    /**
     * Sort list by price
     * @param listings list of listings to be sorted
     * @param sortAscending whether to sort by ascending or not (descending).
     * @return
     */
    public static List<Listing> sortByPrice(List<Listing> listings, boolean sortAscending) {
        List<Listing> sortedListings;
        if (sortAscending) {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getPrice))
                    .collect(Collectors.toList());
        } else {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getPrice).reversed())
                    .collect(Collectors.toList());
        }
        return sortedListings;
    }

    /**
     * Sort list by hostID
     * @param listings list of listings to be sorted
     * @param sortAscending whether to sort by ascending or not (descending).
     * @return
     */
    public static List<Listing> sortByHostID(List<Listing> listings, boolean sortAscending) {
        List<Listing> sortedListings;
        if (sortAscending) {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getHost_id))
                    .collect(Collectors.toList());
        } else {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getHost_id).reversed())
                    .collect(Collectors.toList());
        }
        return sortedListings;
    }

    /**
     *Sort list by neighbourhood
     * @param listings list of listings to be sorted
     * @param sortAscending whether to sort by ascending or not (descending).
     * @return
     */
    public static List<Listing> sortByNeighbourhood(List<Listing> listings, boolean sortAscending) {
        List<Listing> sortedListings;
        if (sortAscending) {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getNeighbourhood))
                    .collect(Collectors.toList());
        } else {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getNeighbourhood).reversed())
                    .collect(Collectors.toList());
        }
        return sortedListings;
    }

    /**
     *Sort list by price host name
     * @param listings list of listings to be sorted
     * @param sortAscending whether to sort by ascending or not (descending).
     * @return
     */
    public static List<Listing> sortByHostName(List<Listing> listings, boolean sortAscending) {
        List<Listing> sortedListings;
        if (sortAscending) {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getHost_name))
                    .collect(Collectors.toList());
        } else {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getHost_name).reversed())
                    .collect(Collectors.toList());
        }
        return sortedListings;
    }

    /**
     *Sort list by reviews
     * @param listings list of listings to be sorted
     * @param sortAscending whether to sort by ascending or not (descending).
     * @return
     */
    public static List<Listing> sortByReviews(List<Listing> listings, boolean sortAscending) {
        List<Listing> sortedListings;
        if (sortAscending) {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getNumberOfReviews))
                    .collect(Collectors.toList());
        } else {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getNumberOfReviews).reversed())
                    .collect(Collectors.toList());
        }
        return sortedListings;
    }

    /**
     *Sort list by minimum nights
     * @param listings list of listings to be sorted
     * @param sortAscending whether to sort by ascending or not (descending).
     * @return
     */
    public static List<Listing> sortByMinNights(List<Listing> listings, boolean sortAscending) {
        List<Listing> sortedListings;
        if (sortAscending) {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getMinimumNights))
                    .collect(Collectors.toList());
        } else {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getMinimumNights).reversed())
                    .collect(Collectors.toList());
        }
        return sortedListings;
    }

    /**
     *Sort list by minimum price
     * @param listings list of listings to be sorted
     * @param sortAscending whether to sort by ascending or not (descending).
     * @return
     */
    public static List<Listing> sortByMinimumPrice(List<Listing> listings, boolean sortAscending) {
        List<Listing> sortedListings;
        if (sortAscending) {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getMinimumPrice))
                    .collect(Collectors.toList());
        } else {
            sortedListings = listings.stream()
                    .sorted(Comparator.comparing(Listing::getMinimumPrice).reversed())
                    .collect(Collectors.toList());
        }
        return sortedListings;
    }
}
