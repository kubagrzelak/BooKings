import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;

/**
 * The main controller of the application.
 * It initialises all the FXML components for the main view.
 * Controls the price range, the navigation buttons and the
 * navigation menu.
 * @version 2020.03.25
 */
public class Controller{

    // key FXML controllers of the main view
    @FXML private BorderPane menubar;
    @FXML private TextField fromTextField;
    @FXML private TextField toTextField;
    @FXML private StackPane stackPane;
    @FXML private Button rightButton;
    @FXML private Button leftButton;

    final private double menubarHeight = 125;

    // the current price boundaries set by the user
    // initially set to -1, which is considered invalid
    private int fromValue = -1;
    private int toValue = -1;

    // the controllers that are updated/used from/in the main view
    private MapController mapController;
    private StatisticsController statisticsController;
    private WelcomeController welcomeController;

    // hold it as a field, so it can be easily removed
    private Node welcomePane;

    // the 'rise' transition of the menubar
    private TranslateTransition menubarTransition;

    /**
     * Initialize the controller and add all the panes to the
     * main StackPane, so their controllers can be accessed.
     * @throws IOException If any of the FXML panes are not found.
     */
    @FXML
    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/compare.fxml"));
        stackPane.getChildren().add(fxmlLoader.load());

        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/statistics.fxml"));
        stackPane.getChildren().add(fxmlLoader.load());
        statisticsController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/map.fxml"));
        stackPane.getChildren().add(fxmlLoader.load());
        mapController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
        welcomePane = fxmlLoader.load();
        stackPane.getChildren().add(welcomePane);
        welcomeController = fxmlLoader.getController();

        fromTextField.focusedProperty().addListener(observable -> fromValueChanged());
        toTextField.focusedProperty().addListener(observable -> toValueChanged());

        // initialize the menubar transition
        menubarTransition = new TranslateTransition(Duration.millis(350), menubar);
        menubar.setTranslateY(menubarHeight);
        menubarTransition.setFromY(menubarHeight);
        menubarTransition.setToY(0);
        menubar.setVisible(false);
    }

    /**
     * If the menubar is hidden, show the menubar.
     */
    @FXML
    private void showMenubar() {
        if (menubar.getTranslateY() == menubarHeight) {
            menubarTransition.setRate(1);
            menubarTransition.play();
        }
    }

    /**
     * If the menubar is shown, hide the menubar.
     */
    @FXML
    private void hideMenubar() {
        if (menubar.getTranslateY() == 0) {
            menubarTransition.setRate(-1);
            menubarTransition.play();
        }
    }

    /**
     * The lower bound of the price range was changed by the user.
     * Check if they entered a valid value.
     * If valid, filter the dataset accordingly, else set the values
     * back to the previous valid values.
     */
    @FXML
    private void fromValueChanged() {
        try {
            int newValue = Integer.parseInt(fromTextField.getText());
            if ((newValue >= 0) && (toTextField.getText().isEmpty() || toValue >= newValue)) {
                fromTextField.setText(Integer.toString(newValue));
                fromValue = newValue;
            }
            else throw new Exception("invalid value");
            checkValidRange();
        }
        catch (Exception e) {
            if (fromValue == -1) fromTextField.setText("");
            else fromTextField.setText(Integer.toString(fromValue));
        }
    }

    /**
     * The upper bound of the price range was changed by the user.
     * Check if they entered a valid value.
     * If valid, filter the dataset accordingly, else set the values
     * back to the previous valid values.
     */
    @FXML
    private void toValueChanged() {
        try {
            int newValue = Integer.parseInt(toTextField.getText());
            if ((fromTextField.getText().isEmpty() && newValue > 0) || (fromValue <= newValue)) {
                int maxValue = Main.getDataset().getPriceRange().getValue();
                if (newValue > maxValue) newValue = maxValue;
                toTextField.setText(Integer.toString(newValue));
                toValue = newValue;
            }
            else throw new Exception("invalid value");
            checkValidRange();
        }
        catch (Exception e) {
            if (toValue == -1) toTextField.setText("");
            else toTextField.setText(Integer.toString(toValue));
        }

    }

    /**
     * Gets called when either of the values in the price range
     * get changed. Checks if both values are valid and
     * updates the view accordingly.
     */
    private void checkValidRange() {
        if (fromValue > -1 && toValue > -1) {
            Main.getDataset().filterPrice(fromValue, toValue);
            welcomeController.stopBlink();
            mapController.update();
            statisticsController.update();
            // after the first valid range set these controls become available
            rightButton.setVisible(true);
            leftButton.setVisible(true);
            menubar.setVisible(true);
        }
    }

    /**
     * Called when a navigation arrow is clicked.
     * Determine the direction of the animation, the next pane
     * to navigate to, and play the transition.
     * @param event The ActionEvent created by the button click.
     */
    @FXML
    public void arrowClick(ActionEvent event) {
        // 1 is right, -1 is left
        int multiplier = 1;
        Node nextPane;
        Node currentPane = stackPane.getChildren().get(stackPane.getChildren().size() - 1);
        if (event.getTarget().equals(leftButton)) {
            multiplier = -1;
            nextPane = stackPane.getChildren().get(0);
        }
        else {
            nextPane = stackPane.getChildren().get(stackPane.getChildren().size() - 2);
        }
        transition(currentPane, nextPane, multiplier);
    }

    /**
     * After the first navigation away from the welcome pane,
     * remove it since it serves no purpose from that point on.
     */
    private void removeWelcome() {
        stackPane.getChildren().remove(welcomePane);
    }

    /**
     * Called when navigation is requested from the menubar.
     * Determine the direction of the animation, the next pane
     * to navigate to, and play the transition.
     * @param event The ActionEvent created by the button click.
     */
    @FXML
    private void menuChoice(ActionEvent event) {
        String choice = ((Node) event.getTarget()).getId();
        choice = choice.substring(0, choice.length() - 6);
        Node currentPane = stackPane.getChildren().get(stackPane.getChildren().size() - 1);
        Node nextPane = currentPane;
        for (Node pane : stackPane.getChildren()) {
            if (pane.getId().equals(choice)) {
                nextPane = pane;
            }
        }
        if (currentPane != nextPane) {
            int multiplier = stackPane.getChildren().indexOf(nextPane) == 0 ? -1 : 1;
            transition(currentPane, nextPane, multiplier);
        }
        else {
            // if the requested pane is already presented
            hideMenubar();
        }
    }

    /**
     * Sliding transition for navigation between panes.
     * @param currentPane The pane which is moved out of the view.
     * @param nextPane The pane which is moved in to the view.
     * @param multiplier The direction of the animation. 1 is right, -1 is left.
     */
    private void transition(Node currentPane, Node nextPane, int multiplier) {
        nextPane.toFront();
        nextPane.setTranslateX(multiplier * stackPane.getWidth());

        TranslateTransition appear = new TranslateTransition(Duration.millis(450), nextPane);
        appear.setToX(0);

        TranslateTransition disappear = new TranslateTransition(Duration.millis(450), currentPane);
        disappear.setToX(multiplier * -stackPane.getWidth());

        ParallelTransition transition = new ParallelTransition(appear, disappear);
        transition.setOnFinished(e -> {
            currentPane.setTranslateX(0);
            if (multiplier == 1) {
                currentPane.toBack();
            }
            leftButton.setDisable(false);
            rightButton.setDisable(false);
            removeWelcome();
        });
        // while the transition is playing the buttons can't be clicked
        leftButton.setDisable(true);
        rightButton.setDisable(true);
        transition.play();
    }

//    private Node getPane(String id) {
//        for (Node pane : stackPane.getChildren()) {
//            if (pane.getId().equals(id)) {
//                return pane;
//            }
//        }
//        return null;
//    }

    /**
     * Open a new window which contains information
     * about the application and the creators.
     */
    @FXML
    private void openAbout() {
        if (Main.getIfNotOpen("   ")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
            try {
                Stage aboutStage = new Stage();
                aboutStage.setTitle("   ");
                aboutStage.setScene(new Scene(loader.load()));
                aboutStage.setResizable(false);
                aboutStage.initStyle(StageStyle.UTILITY);
                aboutStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void openFavourites() {
        Parent propertyListRoot;
        if (Main.getIfNotOpen("Favourites")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/list.fxml"));
            try {
                propertyListRoot = loader.load();

                Stage propertyListStage = new Stage();
                propertyListStage.setTitle("Favourites");
                propertyListStage.setScene(new Scene(propertyListRoot));
                propertyListStage.setMinHeight(206);
                propertyListStage.setMaxHeight(900);
                propertyListStage.setMinWidth(422);
                propertyListStage.setMaxWidth(520);

                ListController controller = loader.getController();
                controller.setListingList(Main.getDataset().getFavourites(), "Favourites");
                propertyListStage.show();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
