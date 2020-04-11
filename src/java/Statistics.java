import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Creates and holds every statistic in the class.
 * It is also responsible for calculating them with
 * every change in price range, or with every selection
 * from ComboBoxes.
 */
public class Statistics {

    // List of every SingleStatistic.
    private List<SingleStatistic> statistics;
    // Map of attribute names (like "entireHomes"), and their values to be represented on StatisticsSnippets.
    private Map<String, String> attributes;
    // List of the filtered DataSet used across whole application.
    private List<Listing> listings;

    // Names of attractions to be put in one ComboBox.
    private List<String> attractions;
    // "garden", "terrace" and "balcony" to be put in one ComboBox.
    private List<String> gardenStrings;
    // Names of boroughs to be put in one ComboBox.
    private List<String> boroughs;


    /*
     * Names of attributes for each statistic:
     */
    private int availableProperties; // Number of available properties.
    private double avgNumberOfReviews; // Average number of reviews per property.
    private String mostExpensiveBorough; // The most expensive borough.
    private int entireHomes; // Number of entire homes/apartments available.
    private String mostReviewedHost; // ID and name of the most reviewed host.
    private int gardenProperties; // Number of properties with either "garden", "terrace" or "balcony".
    private String cheapestHome; // The cheapest entire home/apartment available in a borough of choice.
    private int attractionProperties; // Number of properties close to an attraction of choice.

    /**
     * Creates array list of SingleStatistics, StatisticsSnippets
     * and their graphical representations (BorderPanes). Invokes
     * methods that update the snippets and statistics so that
     * the program is filled with data ans is ready to launch.
     */
    public Statistics()
    {
        statistics = new ArrayList<>();
        attributes = new HashMap<>();

        updateListings();
        createComboBoxLists();
        createSingleStatistics();
    }

    /**
     * Update the List of Listings by getting the filtered dataset.
     */
    private void updateListings()
    {
        listings = Main.getDataset().getFilteredDatasetList();
    }

    /**
     * Create SingleStatistics and add them to the "statistics" list.
     * The null values are StatisticsSnippet are set afterwards.
     */
    private void createSingleStatistics()
    {
        statistics.add(new SingleStatistic("Number of available properties", "availableProperties", Integer.toString(availableProperties), null));
        statistics.add(new SingleStatistic("Average number of reviews per property", "avgNumberOfReviews", Double.toString(avgNumberOfReviews), null));
        statistics.add(new SingleStatistic("Most expensive borough", "mostExpensiveBorough", mostExpensiveBorough, null));
        statistics.add(new SingleStatistic("Number of entire home/apartments", "entireHomes", Integer.toString(entireHomes), null));
        statistics.add(new SingleStatistic("Most reviewed host", "mostReviewedHost", mostReviewedHost, null, null, new ArrayList<>()));
        statistics.add(new SingleStatistic("Number of properties with", "gardenProperties", Integer.toString(gardenProperties), null, gardenStrings, new ArrayList<>()));
        statistics.add(new SingleStatistic("Cheapest home/apartment in", "cheapestHome", cheapestHome, null, boroughs, new ArrayList<>()));
        statistics.add(new SingleStatistic("Number of properties near", "attractionProperties", Integer.toString(attractionProperties), null, attractions, new ArrayList<>()));
    }

    /**
     * Create lists to be put in ComboBoxes of some StatisticsSnippets.
     */
    private void createComboBoxLists()
    {
        gardenStrings = new ArrayList<>() {
            {
                add("garden");
                add("terrace");
                add("balcony");
            }
        };

        boroughs = new ArrayList<>();
        for (Borough borough : Main.getBoroughs().values()) {
            boroughs.add(borough.getName());
        }
        boroughs.sort(String::compareToIgnoreCase);

        attractions = new ArrayList<>();
        for (Attraction attraction : Main.getDataset().getAttractionsDataSet()) {
            attractions.add(attraction.getName());
        }
        attractions.sort(String::compareToIgnoreCase);
    }

    /**
     * Calls each method responsible for calculating and updating statistics values.
     */
    public void updateStatistics()
    {
        updateListings();
        updateAvailableProperties();
        updateAvgNumberOfReviews();
        updateMostExpensiveBorough();
        updateEntireHomes();
        updateMostReviewedHost();
        updateGardenProperties();
        updateBoroughProperty();
        updateAttractionProperties();
    }

    /**
     * Takes a list that is assigned to one of the StatisticsSnippets
     * with ComboBox and calls the corresponding method that updates
     * the attribute value.
     * @param list List from SingleStatistic's parameters.
     */
    public void updateComboBoxStatistic(List<String> list)
    {
        if (list.equals(gardenStrings)) {
            updateGardenProperties();
        }
        else if (list.equals(boroughs)) {
            updateBoroughProperty();
        }
        else if (list.equals(attractions)) {
            updateAttractionProperties();
        }
    }

    /**
     * Updates the attribute "availableProperties".
     */
    private void updateAvailableProperties()
    {
        availableProperties = listings.size();
        attributes.put("availableProperties", Integer.toString(availableProperties));
    }

    /**
     * Updates the attribute "avgNumberOfReviews"
     */
    private void updateAvgNumberOfReviews()
    {
        avgNumberOfReviews = 0;
        for (Listing listing : listings) {
            avgNumberOfReviews += listing.getNumberOfReviews();
        }
        if (availableProperties != 0) {
            avgNumberOfReviews /= availableProperties;
        } else {
            avgNumberOfReviews = 0;
        }
        avgNumberOfReviews = Math.round(avgNumberOfReviews * 10.0) / 10.0; // Rounding double to 1 decimal place.
        attributes.put("avgNumberOfReviews", Double.toString(avgNumberOfReviews));
    }

    /**
     * Updates the attribute "mostExpensiveBorough".
     * It checks the average price per night for each borough.
     */
    private void updateMostExpensiveBorough()
    {
        mostExpensiveBorough = "There are no available properties within this price range";
        for (Borough borough : Main.getBoroughs().values()) {
            int boroughPrices = 0;
            int boroughNights = 0;
            int priceOfMostExpensive = 0;
            for (Listing listing : borough.getListings()) {
                boroughPrices += (listing.getPrice() * listing.getMinimumNights());
                boroughNights += listing.getMinimumNights();
                if ((boroughPrices / boroughNights) > priceOfMostExpensive) {
                    priceOfMostExpensive = boroughPrices / boroughNights;
                    mostExpensiveBorough = borough.getName();
                }
            }
        }
        attributes.put("mostExpensiveBorough", mostExpensiveBorough);
    }

    /**
     * Updates the attribute "entireHomes".
     */
    private void updateEntireHomes()
    {
        entireHomes = 0;
        for (Listing listing : listings) {
            if (listing.getRoom_type().equals("Entire home/apt")) {
                entireHomes++;
            }
        }
        attributes.put("entireHomes", Integer.toString(entireHomes));
    }

    /**
     * Updates the attribute "mostReviewedHost".
     */
    private void updateMostReviewedHost()
    {
        mostReviewedHost = "There are no available properties within the price range";
        List<Listing> temporaryList = new ArrayList<>();
        List<Listing> hostListings = findStatistic("mostReviewedHost").getStatisticListings();
        int biggestAmountOfReviews = 0;
        int numberOfReviews = 0;
        String currentHostId = "";
        for (Listing listing : Sorter.sortByHostID(listings, true)) {
            if (!listing.getHost_id().equals(currentHostId)) {
                currentHostId = listing.getHost_id();
                numberOfReviews = 0;
                temporaryList.clear();
            }
            temporaryList.add(listing);
            numberOfReviews += listing.getNumberOfReviews();
            if (numberOfReviews > biggestAmountOfReviews) {
                hostListings.clear();
                hostListings.addAll(temporaryList);
                biggestAmountOfReviews = numberOfReviews;
                mostReviewedHost = listing.getHost_name();
            }
        }
        attributes.put("mostReviewedHost", mostReviewedHost);
    }

    /**
     * Updates the attribute "gardenProperties".
     */
    private void updateGardenProperties()
    {
        gardenProperties = 0;
        SingleStatistic statistic = findStatistic("gardenProperties");
        String gardenType = statistic.getStatisticsSnippet().getComboBoxChoice();
        statistic.getStatisticListings().clear();

        for (Listing listing : listings) {
            if (listing.getName().toLowerCase().contains(gardenType)) {
                gardenProperties++;
                statistic.getStatisticListings().add(listing);
            }
        }
        attributes.put("gardenProperties", Integer.toString(gardenProperties));
    }

    /**
     * Updates the attribute "cheapestHome".
     */
    private void updateBoroughProperty()
    {
        cheapestHome = "There are no home/apartments in this price range";
        SingleStatistic statistic = findStatistic("cheapestHome");
        String boroughString = statistic.getStatisticsSnippet().getComboBoxChoice();

        // This class is used to align the cheapestHome String correctly in the Label.
        StringAlignUtils util = new StringAlignUtils(50, StringAlignUtils.Alignment.CENTER);

        int priceOfCheapestHome = 0;
        boolean assigned = false;
        for (Borough borough : Main.getBoroughs().values()) {
            if (borough.getName().equals(boroughString)) {
                for (Listing listing : borough.getListings()) {
                    if (listing.getRoom_type().equals("Entire home/apt")) {
                        if(listing.getPrice() < priceOfCheapestHome || !assigned)
                        {
                            statistic.getStatisticListings().clear();
                            priceOfCheapestHome = listing.getPrice();
                            cheapestHome = "Property ID: " + listing.getId() + "\nProperty name: " +
                                    listing.getName() + "\nPrice: £" + priceOfCheapestHome;
                            util.format(cheapestHome);
                            statistic.getStatisticListings().add(listing);
                            assigned = true;
                        }
                    }
                }
            }
        }
        attributes.put("cheapestHome", cheapestHome);
    }

    /**
     * Updates the attribute "attractionProperties" by checking if
     * the property are placed within 1 km from chosen attraction (in straight line).
     */
    private void updateAttractionProperties()
    {
        attractionProperties = 0;
        SingleStatistic statistic = findStatistic("attractionProperties");
        String attractionType = statistic.getStatisticsSnippet().getComboBoxChoice();
        statistic.getStatisticListings().clear();

        for (Attraction attraction : Main.getDataset().getAttractionsDataSet()) {
            if (attraction.getName().equals(attractionType)) {
                for (Listing listing : Main.getDataset().getFilteredDatasetList()) {
                    if (Distance.calculateDistance(listing.getLatitude(), listing.getLongitude(),
                            attraction.getLatitude(), attraction.getLongitude()) <= 1.0) {
                        attractionProperties++;
                        statistic.getStatisticListings().add(listing);
                    }
                }
            }
        }
        attributes.put("attractionProperties", Integer.toString(attractionProperties));
    }

    /**
     * Takes ComboBox List as a parameter and finds the
     * SingleStatistic that has this list as a parameter.
     */
    private SingleStatistic findStatistic(String attributeName)
    {
        boolean found = false;
        SingleStatistic statisticToReturn = null;
        while(!found) {
            for(SingleStatistic statistic : statistics)
            {
                if(statistic.getAttributeName().equals(attributeName)) {
                    statisticToReturn = statistic;
                    found = true;
                }
            }
        }
        return statisticToReturn;
    }

    /**
     * @return List of SingleStatistics.
     */
    public List<SingleStatistic> getStatistics()
    {
        return statistics;
    }

    /**
     * @return Map of attribute names and current values.
     */
    public Map<String, String> getAttributes()
    {
        return attributes;
    }

}