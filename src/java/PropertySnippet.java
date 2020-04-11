import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Represents the graphical interface of one property 'snippet',
 * which is displayed on a list window for a borough or favourites list.
 * The snippets are loaded onto a vBox of a scroll pane for users to scroll through and interact with.
 */
public class PropertySnippet extends BorderPane {

    private Listing listing;
    private ListController listController;

    // Label that shows type of property
    @FXML private Label typeLabel;
    // Label that shows minimum nights of property
    @FXML private Label nightsLabel ;
    // Label that shows host name of property
    @FXML private Label hostLabel ;
    // Label that shows number of reviews of property
    @FXML private Label reviewLabel ;
    // Label that shows price per night of property
    @FXML private Label priceLabel ;
    // Label that shows id of property
    @FXML private Label idLabel ;
    // Label that shows favourite star for property
    @FXML private ImageView star;
    // image of the property
    @FXML private ImageView propertyImage;

    File starOutlineFile = new File("src/res/images/star-outline.png");
    File starFilledFile = new File("src/res/images/star-filled.png");
    Image starOutline = new Image(starOutlineFile.toURI().toString());
    Image starFilled = new Image(starFilledFile.toURI().toString());

    // Context menu and menu items for each snipppet. Includes functions that the user can do to interact with the property.
    @FXML private ContextMenu contextMenu;
    @FXML private MenuItem favourite;
    @FXML private MenuItem compare;
    @FXML private MenuItem open;

    ComparisonController comparisonController = Main.getComparisonController();

    /**
     * Initialize method to set all the JavaFX code actions to the correct methods.
     */
    @FXML
    private void initialize() {
        this.setOnContextMenuRequested(event -> contextMenu.show(this, event.getScreenX(), event.getScreenY()));
        this.setOnMouseClicked(event -> {
            MouseButton button = event.getButton();
            if (button == MouseButton.PRIMARY && (event.getTarget() != star)) {
                openListing();
            }
        });
        favourite.setOnAction(t -> favourite());
        compare.setOnAction(t -> compare());
        open.setOnAction(t -> openListing());
        star.setOnMouseEntered(t -> starEnter());
        star.setOnMouseExited(t -> starExit());
        star.setOnMouseClicked(t -> starClicked());
    }

    // UI Functionality for hovering over the star.
    private void starEnter() {
        if (star.getId().equals("starOutline")) {
            star.setImage(starFilled);
        } else {
            star.setImage(starOutline);

        }
    }

    // UI Functionality for hovering over the star.
    private void starExit() {
        if (star.getId().equals("starOutline")) {
            star.setImage(starOutline);
        } else {
            star.setImage(starFilled);
        }
    }

    // Functionality for clicking on the favourite star.
    private void starClicked() {
        favourite();
    }

    /**
     * Constructor for the property snippet. Includes filling out all the details available on the snippet.
     * @param listing the listing being represented in the snippet.
     * @param controller the list controller the property snippet belongs to.
     */
    public PropertySnippet(Listing listing, ListController controller) {
        this.listing = listing;
        listController = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/propertySnippet.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
            propertyImage.setImage(new Image(getClass().getResourceAsStream(listing.getImageName())));
            this.setId(listing.getId() + "");
            idLabel.setText("#" + listing.getId() + "");
            if (Main.getDataset().getFavourites().contains(listing)) {
                idLabel.setId("idLabelFavourite");
                favourite.setText("Remove from Favourites");
                star.setId("starFilled");
                star.setImage(starFilled);
            }
            typeLabel.setText(listing.getRoom_type());
            nightsLabel.setText(listing.getMinimumNights() + "");
            hostLabel.setText(listing.getHost_name());
            reviewLabel.setText(listing.getNumberOfReviews() + "");
            priceLabel.setText(listing.getPrice() + "");


        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    // Open the listing in a new window if not already open.
    private void openListing() {
        if (Main.getIfNotOpen(listing.getId())) {
            Parent propertyRoot;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/property.fxml"));
            try {
                propertyRoot = loader.load();

                Stage propertyListStage = new Stage();
                propertyListStage.setScene(new Scene(propertyRoot));
                propertyListStage.resizableProperty().setValue(false);
                propertyListStage.setMaxHeight(450);
                propertyListStage.setMaxWidth(580);

                propertyListStage.show();

                PropertyController controller = loader.getController();
                controller.setListController(listController, propertyListStage);
                controller.setListing(listing);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Toggle the favourite status of the property and change relevant UI based on change.
    private void favourite() {

        if (star.getId().equals("starOutline")) {
            star.setImage(starFilled);
            star.setId("starFilled");
        }
        else {
            star.setImage(starOutline);
            star.setId("starOutline");
        }

        if (idLabel.getId().equals("idLabel")) {
            idLabel.setId("idLabelFavourite");
            favourite.setText("Remove from Favourites");
        }
        else {
            idLabel.setId("idLabel");
            favourite.setText("Add to Favourites");
        }
        Main.getDataset().favourite(listing);
    }

    // Add the property to the compare list.
    public void compare() {
        comparisonController.addProperty(listing);
    }
}


