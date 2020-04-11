import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Main application class for a London Property Marketplace application,
 * called BooKing's.
 *
 * In the application the user can choose a price range in which they are
 * looking for properties. The user can select a specific borough and
 * select properties in that borough from a list.
 * By selecting a property information is provided about the properties
 * name, host, location, reviews, type and minimum number of nights.
 * The user can add a property to their favourites or assign them for comparison.
 *
 * In the comparison view the user can select the attributes according
 * to which they want to find the best match. They are presented with a
 * table of the properties being compared and are able to view all their
 * attributes.
 *
 * STATISTICS
 *
 * @version 2020.03.25
 */
public class Main extends Application {

    // the main dataset of the application
    private static DataSet dataset = new DataSet("airbnb-london");
    // the mapping between borough id's and their object representations
    private static Map<String, Borough> boroughs;
    // the comparison pane's controller
    private static ComparisonController comparisonController;

    /**
     * Start the application, open the main stage.
     * @param stage The main stage of the application.
     * @throws IOException If the FXML for the main window is not found.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        stage.setTitle("BooKing's");
        stage.setScene(new Scene(root));
        stage.setMinHeight(820);
        stage.setMinWidth(1100);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo3.png")));
        stage.setOnCloseRequest(e -> {
            // exit the application if the main window is closed
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

    public static void main(String[] args) {
        fillBoroughs();
        launch(args);
    }

    /**
     * Accessor method for the dataset of the application.
     * @return The dataset of the application.
     */
    public static DataSet getDataset() { return dataset; }

    /**
     * Accessor method for the Map of boroughs.
     * @return The Map of boroughs.
     */
    public static Map<String, Borough> getBoroughs() { return boroughs; }

    /**
     * Create a new mapping between the borough objects and their id's.
     */
    private static void fillBoroughs() {
        boroughs = new HashMap<>();
        boroughs.put("harrow", new Borough("Harrow"));
        boroughs.put("brent", new Borough("Brent"));
        boroughs.put("westminster", new Borough("Westminster"));
        boroughs.put("city_of_london", new Borough("City of London"));
        boroughs.put("hackney", new Borough("Hackney"));
        boroughs.put("waltham_forest", new Borough("Waltham Forest"));
        boroughs.put("islington", new Borough("Islington"));
        boroughs.put("haringey", new Borough("Haringey"));
        boroughs.put("barnet", new Borough("Barnet"));
        boroughs.put("enfield", new Borough("Enfield"));
        boroughs.put("camden", new Borough("Camden"));
        boroughs.put("tower_hamlets", new Borough("Tower Hamlets"));
        boroughs.put("croydon", new Borough("Croydon"));
        boroughs.put("redbridge", new Borough("Redbridge"));
        boroughs.put("hillingdon", new Borough("Hillingdon"));
        boroughs.put("bromley", new Borough("Bromley"));
        boroughs.put("bexley", new Borough("Bexley"));
        boroughs.put("havering", new Borough("Havering"));
        boroughs.put("sutton", new Borough("Sutton"));
        boroughs.put("kingston_upon_thames", new Borough("Kingston upon Thames"));
        boroughs.put("richmond_upon_thames", new Borough("Richmond upon Thames"));
        boroughs.put("hounslow", new Borough("Hounslow"));
        boroughs.put("kensington_and_chelsea", new Borough("Kensington and Chelsea"));
        boroughs.put("hammersmith_and_fulham", new Borough("Hammersmith and Fulham"));
        boroughs.put("ealing", new Borough("Ealing"));
        boroughs.put("wandsworth", new Borough("Wandsworth"));
        boroughs.put("merton", new Borough("Merton"));
        boroughs.put("southwark", new Borough("Southwark"));
        boroughs.put("lewisham", new Borough("Lewisham"));
        boroughs.put("greenwich", new Borough("Greenwich"));
        boroughs.put("newham", new Borough("Newham"));
        boroughs.put("barking_and_dagenham", new Borough("Barking and Dagenham"));
        boroughs.put("lambeth", new Borough("Lambeth"));
    }

    /**
     * Clear the values from each borough on the map.
     */
    public static void clearBoroughs() {
        for (Borough borough : boroughs.values()) {
            borough.clear();
        }
    }

    /**
     * Sets comparison controller, so it is available from other panes.
     * @param comparisonControl The controller to be set.
     */
    public static void setComparisonController(ComparisonController comparisonControl)
    {
        comparisonController = comparisonControl;
    }

    /**
     * Accessor method for the comparison pane's controller.
     * @return The comparison pane's controller.
     */
    public static ComparisonController getComparisonController(){
        return comparisonController;
    }

    /**
     * Check if there is a stage open with the same title
     * as the provided string. If yes, bring it to the front
     * and return false, else returns true.
     * @param title The stage title to check.
     * @return True if there is no stage open with the same title,
     *         false otherwise.
     */
    public static boolean getIfNotOpen(String title) {
        for (Window window : Stage.getWindows()) {
            try {
                Stage stage = (Stage) window;
                if (stage.getTitle().equals(title)) {
                    stage.toFront();
                    return false;
                }
            }
            // if it cannot be casted we are not interested
            catch (ClassCastException ignored) {}
        }
        return true;
    }
}
