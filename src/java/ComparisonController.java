import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allows to control a comparison pane.
 * It allows to add or remove properties to the comparison list,
 * fill properties on the screen, choose the best match property.
 */
public class ComparisonController {

    @FXML StackPane stackPane;
    @FXML Label emptyList;
    @FXML BorderPane compareView;
    @FXML ScrollPane scrollPane;
    @FXML CheckBox distanceCheckBox;
    @FXML CheckBox priceCheckBox;
    @FXML CheckBox nightsCheckBox;

    // a list of properties to compare
    private List<Listing> properties = new ArrayList<>();
    HBox hbox = new HBox();


    /**
     * initializes the comparison pane
     */
    @FXML
    public void initialize(){
        Main.setComparisonController(this);

        hbox.getStyleClass().add("hbox");

        if(properties.isEmpty()){
            Node currentPane = stackPane.getChildren().get(0);
            currentPane.toFront();
        }
        else{
            Node currentPane = stackPane.getChildren().get(1);
            currentPane.toFront();
            fillProperties();
        }
    }

    /**
     * Adds a listing to the list of properties to compare.
     * If a property repeats then it shows another warning
     * message dialog.
     * @param listing Single listing to add to compare view.
     */
    public void addProperty(Listing listing){
        if(checkIfRepeated(listing.getId())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("It seems that you are already comparing this property!");
            alert.showAndWait();
        }
        else{
            properties.add(listing);
            // refresh view
            fillProperties();
        }
    }

    /**
     * Controls CheckBoxes
     */
    @FXML
    public void handleCheckBox(){
        fillProperties();
    }

    /**
     * Fills the selected properties into the pane.
     */
    public void fillProperties() {
        // clear an old pane/view
        hbox.getChildren().clear();

        // sets bottom pane to invisible
        emptyList.setVisible(false);
        compareView.setVisible(true);

        // checks if user wants to see Best Match property
        boolean existsBM = chooseBestMatch();

        // properties with below parameters
        // will be highlighted
        int minPrice = getMinPrice();
        double minDistance = getMinDistance();
        int minNights = getMinNights();

        // fills the pane
        for (Listing property : properties) {
            ComparisonPropertySnippet newSnippet = new ComparisonPropertySnippet(property, this);

            if (property.getPrice() == minPrice) {
                newSnippet.setPriceGreen();
            }
            if (property.getMinimumNights() == minNights) {
                newSnippet.setNightsGreen();
            }
            double distance = Distance.calculateDistance(property.getLatitude(), property.getLongitude(), 51.5074, -0.1277);
            if (distance == minDistance){
                newSnippet.setDistanceGreen();
            }
            if(existsBM) {
                // sets layout for Best Match property
                newSnippet.setBestMatch();

                // once there is Best Match displayed
                // existsBM can be set to false
                existsBM = false;
            }
            hbox.getChildren().add(newSnippet);
        }
        scrollPane.setContent(hbox);
    }

    /**
     * Removes a property from the list of properties to compare.
     * @param listing Property to remove.
     */
    public void removeProperty(Listing listing){
        for (Listing property : properties) {
            if (listing.equals(property)) {
                properties.remove(property);
                break;
            }
        }
        if(properties.isEmpty()){
            stackPane.getChildren().get(1).toFront();
            // sets the empty list view
            emptyList.setVisible(true);
            compareView.setVisible(false);
        }
        else {
            // refreshes the view
            fillProperties();
        }
    }

    /**
     * Check if a new property is already on the list of compared properties.
     * Helps to avoid repetitions.
     * @param propertyID ID of an added new property.
     * @return True if a property is already on the comparison list.
     */
    public boolean checkIfRepeated(String propertyID){
        for(Listing property : properties){
            if(property.getId().equals(propertyID)){
                return true;
            }
        }
        return false;
    }

    /**
     * @return minPrice Minimum price of properties on the list.
     */
    public int getMinPrice()
    {
        int minPrice = properties.get(0).getPrice();
        for (Listing property : properties) {
            if (property.getPrice() < minPrice) {
                minPrice = property.getPrice();
            }
        }
        return minPrice;
    }

    /**
     * @return minDistance Minimum distance to Central London of properties on the list.
     */
    public double getMinDistance()
    {
        double minDistance = Distance.calculateDistance(properties.get(0).getLatitude(), properties.get(0).getLongitude(), 51.5074, -0.1277);
        for (Listing property : properties) {
            double distance = Distance.calculateDistance(property.getLatitude(), property.getLongitude(), 51.5074, -0.1277);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }

    /**
     * @return minNights Minimum nights of properties on the list.
     */
    public int getMinNights()
    {
        int minNights = properties.get(0).getMinimumNights();
        for (Listing property : properties) {
            if (property.getMinimumNights() < minNights) {
                minNights = property.getMinimumNights();
            }
        }
        return minNights;
    }

    /**
     * Chooses best match property basing on the check boxes ticked
     * by the user and moves the property to the front
     * of the comparison list.
     *
     * If there are more than one best match properties,
     * then the system selects the one which was found first.
     *
     * @return false If user did not tick any Best Match Preferences box.
     */
    private boolean chooseBestMatch(){
        // by default the first property on the list is Best Match
        Listing bestMatch = properties.get(0);
        boolean status = false;

        /*
         *  If user ticked:
         *  min. nights and price and distance boxes
         *  then best match property is the one with minimum:
         *  distance*price + nights*price.
         */
        if(priceCheckBox.isSelected() && distanceCheckBox.isSelected() && nightsCheckBox.isSelected()) {
            double minRatio = Distance.calculateDistance(properties.get(0).getLatitude(), properties.get(0).getLongitude(), 51.5074, -0.1277) * properties.get(0).getPrice();
            minRatio = minRatio + properties.get(0).getMinimumNights() * properties.get(0).getPrice();
            for(Listing property : properties){
                double ratio = Distance.calculateDistance(property.getLatitude(), property.getLongitude(), 51.5074, -0.1277) * property.getPrice();
                ratio = ratio + property.getMinimumNights() * property.getPrice();
                if(ratio < minRatio){
                    minRatio = ratio;
                    bestMatch = property;
                }
            }
            status = true;
        }
        /*
         *  If user ticked:
         *  price and distance boxes but not min. nights
         *  then best match property is the one with minimum:
         *  distance*price.
         */
        else if(priceCheckBox.isSelected() && distanceCheckBox.isSelected() && !nightsCheckBox.isSelected()) {
            double minRatio = Distance.calculateDistance(properties.get(0).getLatitude(), properties.get(0).getLongitude(), 51.5074, -0.1277) * properties.get(0).getPrice();
            for (Listing property : properties) {
                double ratio = Distance.calculateDistance(property.getLatitude(), property.getLongitude(), 51.5074, -0.1277) * property.getPrice();
                if(ratio < minRatio){
                    minRatio = ratio;
                    bestMatch = property;
                }
            }
            status = true;
        }
        /*
         *  If user ticked:
         *  price and min. nights but not distance
         *  then best match property is the one with minimum:
         *  nights*price.
         */
        else if(priceCheckBox.isSelected() && !distanceCheckBox.isSelected() && nightsCheckBox.isSelected()) {
            double minRatio = properties.get(0).getMinimumNights() * properties.get(0).getPrice();
            for (Listing property : properties) {
                double ratio = property.getMinimumNights() * property.getPrice();
                if (ratio < minRatio) {
                    minRatio = ratio;
                    bestMatch = property;
                }
            }
            status = true;
        }
        /*
         *  If user ticked:
         *  only price but not min. nights nor distance
         *  then best match property is the one with minimum:
         *  price.
         */
        else if(priceCheckBox.isSelected() && !distanceCheckBox.isSelected() && !nightsCheckBox.isSelected()) {
            int minPrice = properties.get(0).getPrice();
            for (Listing property : properties) {
                if (property.getPrice() < minPrice) {
                    minPrice = property.getPrice();
                    bestMatch = property;
                }
            }
            status = true;
        }
        /*
         *  If user ticked:
         *  distance and min. nights boxes but not price
         *  then best match property is the one with minimum:
         *  distance + nights.
         */
        else if(!priceCheckBox.isSelected() && distanceCheckBox.isSelected() && nightsCheckBox.isSelected()) {
            double minRatio = Distance.calculateDistance(properties.get(0).getLatitude(), properties.get(0).getLongitude(), 51.5074, -0.1277) + properties.get(0).getMinimumNights();
            for (Listing property : properties) {
                double ratio = Distance.calculateDistance(property.getLatitude(), property.getLongitude(), 51.5074, -0.1277) + property.getMinimumNights();
                if (ratio < minRatio) {
                    minRatio = ratio;
                    bestMatch = property;
                }
            }
            status = true;
        }
        /*
         *  If user ticked:
         *  only distance but not min. nights nor price
         *  then best match property is the one with minimum:
         *  distance.
         */
        else if(!priceCheckBox.isSelected() && distanceCheckBox.isSelected() && !nightsCheckBox.isSelected()) {
            double minDis = Distance.calculateDistance(properties.get(0).getLatitude(), properties.get(0).getLongitude(), 51.5074, -0.1277);
            for (Listing property : properties) {
                double dis = Distance.calculateDistance(property.getLatitude(), property.getLongitude(), 51.5074, -0.1277);
                if (dis < minDis) {
                    minDis = dis;
                    bestMatch = property;
                }
            }
            status = true;
        }
        /*
         *  If user ticked:
         *  only min. nights but not price nor distance
         *  then best match property is the one with minimum:
         *  min nights.
         */
        else if(!priceCheckBox.isSelected() && !distanceCheckBox.isSelected() && nightsCheckBox.isSelected()) {
            int minNights = properties.get(0).getMinimumNights();
            for (Listing property : properties) {
                if (property.getMinimumNights() < minNights) {
                    minNights = property.getMinimumNights();
                    bestMatch = property;
                }
            }
            status = true;
        }

        // push best match property to the front of the list
        properties.remove(bestMatch);
        properties.add(0, bestMatch);

        return status;
    }
}
