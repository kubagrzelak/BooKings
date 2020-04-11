import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

/**
 * The JavaFX controller for the Map pane on the main view of the application.
 * @version 2020.03.25
 */
public class MapController {

    @FXML private BorderPane mapPane;
    @FXML private Pane map;
    @FXML private HBox legendBox;
    // current borough being hovered on
    @FXML private Label boroughName;

    private final int RED = 226;
    private final int BLUE = 210;
    private final int GREEN = 210;

    // old values of the pane, used for resizing the map
    private double oldHeight;
    private double oldWidth;


    /**
     * Initialize the controller for the map view.
     */
    @FXML
    public void initialize() {
        update();

        // set mouse actions for the paths of the boroughs
        for (Node node : map.getChildren()) {
            node.setOnMouseClicked(e -> onClick(node));
            node.setOnMouseEntered(e -> onHover(node));
            node.setOnMouseExited(e -> onHoverEnded(node));
        }

        // create legend for the map
        ImageView colorScale = new ImageView(createColorScaleImage(50, 200, Orientation.VERTICAL));
        legendBox.getChildren().add(colorScale);
        mapPane.setRight(legendBox);

        oldHeight = mapPane.getPrefHeight();
        oldWidth = mapPane.getPrefWidth();
        mapPane.widthProperty().addListener((obs, oldVal, newVal) -> resize(map, (Double) newVal, mapPane.getHeight()));
        mapPane.heightProperty().addListener((obs, oldVal, newVal) -> resize(map, mapPane.getWidth(), (Double) newVal));
    }

    /**
     * Update the coloring of the map according to the changed values.
     */
    private void updateMap() {
        for (Node node : map.getChildren()) {
            SVGPath path = (SVGPath) node;
            path.setFill(getColorForCount(Main.getBoroughs().get(node.getId()).getCount()));
        }
    }

    /**
     * Rescale the map according to the size of the stage.
     * @param pane The pane to be rescaled.
     * @param newWidth New width of the component the scaling is based on.
     * @param newHeight New height of the component the scaling is based on.
     */
    private void resize(Pane pane, double newWidth, double newHeight) {

        double scaleX = newHeight / oldHeight;
        double scaleY = newWidth / oldWidth;
        double scale;

        scale = Math.min(scaleX, scaleY);

        pane.setScaleX(scale);
        pane.setScaleY(scale);
    }

    /**
     * On clicking a borough, create a new window for the
     * list of properties in that borough.
     * @param node The node clicked.
     */
    @FXML
    private void onClick(Node node) {
        Parent propertyListRoot;

        if (Main.getIfNotOpen(Main.getBoroughs().get(node.getId()).getName())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/list.fxml"));
            try {
                propertyListRoot = loader.load();

                Stage propertyListStage = new Stage();
                propertyListStage.setTitle(Main.getBoroughs().get(node.getId()).getName());
                propertyListStage.setScene(new Scene(propertyListRoot));
                propertyListStage.setMinHeight(205);
                propertyListStage.setMaxHeight(900);
                propertyListStage.setMinWidth(422);
                propertyListStage.setMaxWidth(520);

                ListController controller = loader.getController();
                controller.setListingList(Main.getDataset().filterNeighbourhood(Main.getBoroughs().get(node.getId()).getName()), Main.getBoroughs().get(node.getId()).getName());

                propertyListStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * On hovering over a borough, set it to the top of the pane
     * and display it's name to the boroughName label.
     * @param node The node hovered on.
     */
    @FXML
    private void onHover(Node node) {
        node.setViewOrder(-1);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), node);
        scaleTransition.setToX(3.5);
        scaleTransition.setToY(3.5);
        scaleTransition.play();
        boroughName.setText(Main.getBoroughs().get(node.getId()).getName() + " - " + Main.getBoroughs().get(node.getId()).getCount() + " properties available");
    }

    /**
     * On exiting a borough, place the borough to the bottom of the pane
     * and clear the boroughName label.
     * @param node The node just exited.
     */
    @FXML
    private void onHoverEnded(Node node) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), node);
        scaleTransition.setToX(3);
        scaleTransition.setToY(3);
        scaleTransition.play();
        node.setViewOrder(0);
        boroughName.setText("");
    }

    /**
     * Update the values for listings in each borough.
     */
    private void updateListingCounts() {
        Main.clearBoroughs();
        for (Listing listing : Main.getDataset().getFilteredDatasetList()) {
            String id = listing.getNeighbourhood().replaceAll(" ", "_").toLowerCase();
            Main.getBoroughs().get(id).addCount();
            Main.getBoroughs().get(id).addListing(listing);
        }
    }

    /**
     * Update the filtering, the listing counts and the map visually.
     */
    public void update() {
        updateListingCounts();
        updateMap();
        if (legendBox.getChildren().size() > 0) { legendBox.getChildren().remove(0); }
        legendBox.getChildren().add(0, createLegendLabels());
        oldHeight = mapPane.getHeight();
        oldWidth = mapPane.getWidth();
    }

    /**
     * Create the labels for the numbering of the legend
     * and space them in a VBox.
     * @return A VBox with the created labels.
     */
    private VBox createLegendLabels() {
        VBox labels = new VBox();
        labels.setAlignment(Pos.CENTER_RIGHT);

        int maxCount = getMaxCount();
        double diff = Math.round((double) maxCount / 4);
        int distributionNum;

        if (maxCount > 3) { distributionNum = 4; }
        else {
            distributionNum =  maxCount;
            if (maxCount == 0) {
                labels.getChildren().add(createSpacer());
            }
        }

        for (int i = 0; i < distributionNum; i++) {
            labels.getChildren().addAll(new Label( maxCount + ""), createSpacer());
            maxCount -= diff;
        }
        labels.getChildren().add(new Label("0"));

        return labels;
    }

    /**
     * Create an empty region for spacing purposes.
     * @return The empty region node.
     */
    private Node createSpacer() {
        final Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    /**
     * Create the appropriate color for the provided
     * number of listings in a borough.
     * @param count Number of listings in a borough.
     * @return The appropriate color for the map representation.
     */
    private Color getColorForCount(double count) {
        if (getMaxCount() == 0) {
            return Color.rgb(RED, BLUE, GREEN);
        }
        double hue = 1 - count / getMaxCount();
        return Color.rgb(RED, (int) Math.round(hue * BLUE), (int) Math.round(hue * GREEN));
    }

    /**
     * @return The maximum number of listings in a single borough.
     */
    private int getMaxCount() {
        int maxCount = 0;
        for (Borough borough : Main.getBoroughs().values()) {
            if (borough.getCount() > maxCount) maxCount = borough.getCount();
        }
        return maxCount;
    }

    /**
     * Create a color scale image with provided width, height and orientation.
     * Taken from: https://stackoverflow.com/questions/25214538/draw-a-smooth-color-scale-and-assign-specific-values-to-it
     * @param width Width for the image.
     * @param height Height for the image.
     * @param orientation Orientation for the image. (vertical or horizontal)
     * @return The created color scale image.
     */
    private Image createColorScaleImage(int width, int height, Orientation orientation) {
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        if (orientation == Orientation.HORIZONTAL) {
            for (double x = 0; x < width; x++) {
                double value = getMaxCount() * x / width;
                Color color = getColorForCount(value);

                for (int y = 0; y < height; y++) {
                    pixelWriter.setColor((int) x, y, color);
                }
            }
        } else {
            for (double y=0; y<height; y++) {
                double value = getMaxCount() - getMaxCount() * y / height ;
                Color color = getColorForCount(value);

                for (int x=0; x<width; x++) {
                    pixelWriter.setColor(x, (int) y, color);
                }
            }
        }

        return image;
    }
}
