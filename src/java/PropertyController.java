
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *  This class allows to control the property main view,
 *  and its tab view containing:
 *  - Tab 1 - general property info
 *  - Tab 2 - map view with property's location.
 */
public class PropertyController {

    private Listing listing;
    private ComparisonController comparisonController;
    private ListController listController;
    private Stage propertyStage;

    @FXML private Tab propertyTab;
    @FXML private Tab mapTab;
    @FXML private ImageView propertyImage;
    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label neighbourhoodLabel;
    @FXML private Label hostLabel;
    @FXML private Label priceLabel;
    @FXML private Label nightsLabel;
    @FXML private Label reviewsLabel;
    @FXML private Label availabilityLabel;
    @FXML private Button compareButton;
    @FXML private ImageView nextButton;
    @FXML private ImageView previousButton;
    @FXML private BorderPane mapPane;

    /**
     * Set the chosen property into the view.
     * Fill in data and set the map.
     * @param listing Single Airbnb property.
     */
    public void setListing(Listing listing) {
        this.listing = listing;
        fillData();
        this.comparisonController = Main.getComparisonController();
        final MapView map = new MapView(listing.getLatitude(), listing.getLongitude());
        mapPane.setCenter(map);
    }

    /**
     *
     * @param controller
     */
    public void setListController(ListController controller, Stage stage) {
        listController = controller;
        propertyStage = stage;
    }

    /**
     *  Fill in data into the panes and views.
     */
    private void fillData() {
        propertyImage.setImage(new Image(getClass().getResourceAsStream(listing.getImageName())));
        propertyStage.setTitle(listing.getId());
        nameLabel.setText(listing.getName());
        typeLabel.setText(listing.getRoom_type());
        neighbourhoodLabel.setText(listing.getNeighbourhood());
        hostLabel.setText(listing.getHost_name());
        priceLabel.setText("£" + listing.getPrice());
        nightsLabel.setText(listing.getMinimumNights() + " nights");
        reviewsLabel.setText(listing.getNumberOfReviews() + " reviews (last: " + listing.getLastReview() + ")");
        availabilityLabel.setText("Available " + listing.getAvailability365() + " days in a year");

        Pair<Boolean, Boolean> navBounds = navBounds(getCurrentIndex());
        previousButton.setVisible(navBounds.getKey());
        nextButton.setVisible(navBounds.getValue());
    }

    /**
     * When "Add to comparison list" button is pressed,
     * it adds property to the comparison view.
     * @param e Mouse click.
     */
    @FXML
    private void addButton(ActionEvent e)
    {
        comparisonController.addProperty(listing);
    }

    /**
     *
     */
    @FXML
    private void next() {
        if (navBounds(getCurrentIndex()).getValue()) {
            setListing(listController.getSortedListings().get(getCurrentIndex() + 1));
        }
        else {
            nextButton.setVisible(false);
        }
    }

    /**
     * Present the previous listing
     */
    @FXML
    private void previous() {
        if (navBounds(getCurrentIndex()).getKey()) {
            setListing(listController.getSortedListings().get(getCurrentIndex() - 1));
        }
        else {
            previousButton.setVisible(false);
        }
    }

    private Pair<Boolean, Boolean> navBounds(int index) {
        Boolean previousInBound = index >= 1 ? Boolean.TRUE : Boolean.FALSE;
        Boolean nextInBound = index < listController.getSortedListings().size() - 1 ? Boolean.TRUE : Boolean.FALSE;
        return new Pair<>(previousInBound, nextInBound);
    }

    private int getCurrentIndex() {
        return listController.getSortedListings().indexOf(listing);
    }
}
