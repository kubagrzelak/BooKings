import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import java.io.IOException;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;

/**
 * This class allows to control a property snippet for the
 * comparison view.
 * It sets the title snippet,
 * then it fills the snippet with data,
 * sets frame and graphics for the BEST MATCH property.
 */
public class ComparisonPropertySnippet extends BorderPane {

    @FXML
    Label bestMatch;
    @FXML
    ImageView propertyImage;
    @FXML
    Label nameLabel;
    @FXML
    Label neighbourhoodLabel;
    @FXML
    Label typeLabel;
    @FXML
    Label hostLabel;
    @FXML
    Label nightsLabel;
    @FXML
    Label distanceLabel;
    @FXML
    Label priceLabel;
    @FXML
    Button removeButton;

    private Listing property;
    private ComparisonController comparisonController;

    /**
     * Constructor for a property snippet in the comparison pane.
     * @param prop Listing that is added to the snippet.
     * @param comparisonControl Comparison control object.
     */
    public ComparisonPropertySnippet(Listing prop, ComparisonController comparisonControl)
    {
        this.property = prop;
        this.comparisonController = comparisonControl;

        // load FXML version of the snippet
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/comparisonPropertySnippet.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        // fill in data of the property
        try {
            loader.load();
            propertyImage.setImage(new Image(getClass().getResourceAsStream(property.getImageName())));
            nameLabel.setText(property.getName());
            neighbourhoodLabel.setText(property.getNeighbourhood());
            typeLabel.setText(property.getRoom_type());
            hostLabel.setText(property.getHost_name());
            nightsLabel.setText(Integer.toString(property.getMinimumNights()));
            distanceLabel.setText(String.format("%.2f km", Distance.calculateDistance(property.getLatitude(), property.getLongitude(), 51.5074, -0.1277)));
            priceLabel.setText("£" + property.getPrice());
            setBorder();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    /**
     * Sets white border for all snippets.
     */
    public void setBorder(){
        this.setBorder(new Border(new BorderStroke(Paint.valueOf("transparent"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        bestMatch.setVisible(false);
    }

    /**
     * Sets layout for the best match property:
     * sets text of the label "Best Match"
     * sets gold border to make an effect of the gold frame
     */
    public void setBestMatch()
    {
        bestMatch.setVisible(true);
        this.setBorder(new Border(new BorderStroke(LinearGradient.valueOf("linear-gradient(from 0% 0% to 100% 100%, yellow  0% , gold 15%, orange 49%, gold 52%, yellow 78%, gold 90%, gold 100%)"),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
    }

    /**
     * Sets price label to green.
     */
    @FXML
    public void setPriceGreen(){
        priceLabel.setStyle("-fx-text-fill: green");
    }

    /**
     * Sets distance label to green.
     */
    @FXML
    public void setDistanceGreen(){
        distanceLabel.setStyle("-fx-text-fill: green;");
    }

    /**
     * Sets nights label to green.
     */
    @FXML
    public void setNightsGreen(){
        nightsLabel.setStyle("-fx-text-fill: green;");
    }

    /**
     * Removes a property from the view when remove button is clicked.
     * @param e Click of the mouse.
     */
    @FXML
    public void removeButton(ActionEvent e)
    {
        comparisonController.removeProperty(property);
    }
}
