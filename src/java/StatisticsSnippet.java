import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Represents the graphical interface of one statistic snippet,
 * which is displayed on the statistics page in the application.
 * Its components are put in BorderPane that has a GridPane on top
 * and valueLabel in the center. GridPane contains an HBox with nameLabel
 * and possible ComboBox (depends on if the statistic needs one).
 */
public class StatisticsSnippet {

    // BorderPane, in which all the components are wrapped in.
    @FXML private BorderPane pane;
    // Holds single statistic's description such as "Available properties"
    @FXML private Label nameLabel;
    // Holds single statistic's attribute value.
    @FXML private Label valueLabel;
    // Used in some single statistics. It provides an option for the user
    // to choose some preferences that filter the properties.
    @FXML private ComboBox<String> combo;
    // HBox that holds the nameLabel and in some cases ComboBox.
    @FXML private HBox box;
    // Button that shows the properties when you click on it.
    @FXML private Button show;
    // The singleStatistic that the StatisticsSnippet is connected to.
    private SingleStatistic singleStatistic;

    /**
     * Changes the content of nameLabel.
     * @param text Content to be set in nameLabel.
     */
    public void changeNameLabel(String text)
    {
        nameLabel.setText(text);
    }

    /**
     * Changes the content of valueLabel.
     * @param text Content to be set in valueLabel
     */
    public void changeValueLabel(String text) {
        valueLabel.setText(text);
    }

    /**
     * Sets margin within the components of the HBox.
     */
    public void setBoxMargin()
    {
        HBox.setMargin(combo, new Insets(0,0,0,10));
    }

    /**
     * Sets new css style class.
     */
    public void setNewStyleClass()
    {
        valueLabel.getStyleClass().remove("dataLabel");
        valueLabel.getStyleClass().add("dataLabelWithCombo");
    }

    /**
     * Removes ComboBox from the StatisticSnippet.
     */
    public void removeComboBox()
    {
        box.getChildren().remove(combo);
    }

    /**
     * Removes ShowDetails button from the StatisticSnippet.
     */
    public void removeShowButton()
    {
        pane.getChildren().remove(show);
    }

    /**
     * Sets the ObservableList in the ComboBox.
     * @param list List to be set in the ComboBox.
     */
    public void setComboBox(ObservableList<String> list)
    {
        combo.setItems(list);
    }

    /**
     * @return Value that is currently selected in the ComboBox.
     */
    public String getComboBoxChoice()
    {
        return combo.getValue();
    }

    /**
     * Sets the singleStatistic.
     * @param singleStatistic The singleStatistic that the StatisticsSnippet is connected to.
     */
    public void setSingleStatistic(SingleStatistic singleStatistic)
    {
        this.singleStatistic = singleStatistic;
    }

    /**
     * @return The ComboBox.
     */
    public ComboBox<String> getComboBox() {
        return combo;
    }

    /**
     * On clicking a "show details" button, create a new window
     * for the list of properties appropriate to the statistic clicked
     */
    @FXML
    public void clickShow() {

        Parent propertyListRoot;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/list.fxml"));
        try {
            propertyListRoot = loader.load();

            Stage propertyListStage = new Stage();
            propertyListStage.setScene(new Scene(propertyListRoot));
            ListController controller = loader.getController();
            propertyListStage.setMinHeight(263);
            propertyListStage.setMaxHeight(900);
            propertyListStage.setMinWidth(422);

            String title = "";
            if (singleStatistic.getAttributeName().equals("gardenProperties")) {
                title = "Properties with " + getComboBoxChoice();
            }
            else if (singleStatistic.getAttributeName().equals("cheapestHome")) {
                title = "Cheapest home/apartment in " + getComboBoxChoice();
            }
            else if (singleStatistic.getAttributeName().equals("attractionProperties")) {
                title = "Properties near " + getComboBoxChoice();
            }
            else if (singleStatistic.getAttributeName().equals("mostReviewedHost")) {
                title = singleStatistic.getAttributeValue() + "'s properties";
            }

            propertyListStage.setTitle(title);
            controller.setListingList(singleStatistic.getStatisticListings(), title);
            propertyListStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
