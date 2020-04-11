import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class DataLoader {

    private List<String> imageNames;

    public DataLoader() {
        imageNames = loadImages();
    }

    /**
     * Return an ArrayList containing the rows in the AirBnB London data set csv file.
     */
    public ArrayList<Listing> loadListings(String filename) {
        System.out.print("Begin loading Airbnb london dataset...");
        ArrayList<Listing> listings = new ArrayList<>();
        try {
            URL url = getClass().getResource("/datasets/" + filename + ".csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String[] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String id = line[0];
                String name = line[1];
                String host_id = line[2];
                String host_name = line[3];
                String neighbourhood = line[4];
                double latitude = convertDouble(line[5]);
                double longitude = convertDouble(line[6]);
                String room_type = line[7];
                int price = convertInt(line[8]);
                int minimumNights = convertInt(line[9]);
                int numberOfReviews = convertInt(line[10]);
                String lastReview = line[11];
                double reviewsPerMonth = convertDouble(line[12]);
                int calculatedHostListingsCount = convertInt(line[13]);
                int availability365 = convertInt(line[14]);

                Listing listing = new Listing(id, name, host_id,
                        host_name, neighbourhood, latitude, longitude, room_type,
                        price, minimumNights, numberOfReviews, lastReview,
                        reviewsPerMonth, calculatedHostListingsCount, availability365
                );
                listing.addImageName(randomImage());
                listings.add(listing);
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        System.out.println("Success! Number of loaded records: " + listings.size());
        return listings;
    }

    /**
     * Return an ArrayList containing the rows in the London attractions data set csv file.
     */
    public ArrayList<Attraction> loadAttractions()
    {
        System.out.print("Begin loading London attractions dataset...");
        ArrayList<Attraction> attractions = new ArrayList<>();
        try {
            URL url = getClass().getResource("/datasets/attractions-london.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String[] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String name = line[0];
                double latitude = convertDouble(line[1]);
                double longitude = convertDouble(line[2]);

                Attraction attraction = new Attraction(name, latitude, longitude);
                attractions.add(attraction);
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        System.out.println("Success! Number of loaded attractions: " + attractions.size());
        return attractions;
    }

    /**
     *
     * @param doubleString the string to be converted to Double type
     * @return the Double value of the string, or -1.0 if the string is
     * either empty or just whitespace
     */
    private Double convertDouble(String doubleString){
        if(doubleString != null && !doubleString.trim().equals("")){
            return Double.parseDouble(doubleString);
        }
        return -1.0;
    }

    /**
     *
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is
     * either empty or just whitespace
     */
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return -1;
    }

    /**
     * Load all the filenames in the "property-images" folder.
     * @return List of filenames in the directory.
     */
    private List<String> loadImages() {
        try (Stream<Path> walk = Files.walk(Paths.get("src/res/images/property-images"))) {
            List<String> paths = walk.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
            List<String> result = new ArrayList<>();
            for (String x : paths) result.add(x.split("/res")[1]);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get a random filename from the "property-images" folder.
     * @return String name of a random file from the directory.
     */
    private String randomImage() {
        Random random = new Random();
        return imageNames.get(random.nextInt(imageNames.size()));
    }
}
