import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controls the main Statistics Panel in the application.
 * It contains 4 BorderPanes that display 4 StatisticsSnippets
 * at one time. It also updates the information displayed by
 * StatisticsSnippet.
 */
public class StatisticsController {

    // BorderPanes which statistic panel is showing.
    @FXML private BorderPane paneOne;
    @FXML private BorderPane paneTwo;
    @FXML private BorderPane paneThree;
    @FXML private BorderPane paneFour;

    // Class that creates and holds every statistic.
    private Statistics statistics;
    // List of 4 BorderPanes that the StatisticsPanel is made of.
    private List<BorderPane> borderPaneList;
    // List of all StatisticsSnippets BorderPanes. One for each statistic.
    private List<BorderPane> statisticsPanes;


    /**
     *  Initialise the controller of the statistics panel.
     */
    @FXML
    public void initialize()
    {
        statistics = new Statistics();
        statisticsPanes = new ArrayList<BorderPane>();

        createBorderPaneList();
        createStatisticsSnippets();
        update();
        fillPanes();
    }

    /**
     * Creates and fills borderPaneList.
     */
    private void createBorderPaneList()
    {
        borderPaneList = new ArrayList<BorderPane>();
        borderPaneList.add(paneOne);
        borderPaneList.add(paneTwo);
        borderPaneList.add(paneThree);
        borderPaneList.add(paneFour);
    }

    /**
     * Creates and adjust the look of StatisticsSnippets. This method
     * also loads their BorderPanes and adds puts each StatisticsSnippet
     * to their SingleStatistic.
     */
    private void createStatisticsSnippets()
    {
        for (SingleStatistic singleStatistic : statistics.getStatistics()) {

            try {
                FXMLLoader snippetLoader = new FXMLLoader(getClass().getResource("/fxml/statisticsSnippet.fxml"));
                BorderPane snippetPane = snippetLoader.load();
                StatisticsSnippet snippet = snippetLoader.getController();

                snippet.changeNameLabel(singleStatistic.getDescription());
                snippet.setSingleStatistic(singleStatistic);
                statisticsPanes.add(snippetPane);
                statistics.getAttributes().put(singleStatistic.getAttributeName(), singleStatistic.getAttributeValue());
                singleStatistic.setStatisticsSnippet(snippet);

                if (singleStatistic.getStatisticListings() == null) {
                    snippet.removeShowButton();
                }
                else {
                    snippet.setNewStyleClass();
                }

                if (singleStatistic.getList() == null) {
                    snippet.removeComboBox();
                }
                else {
                    snippet.setBoxMargin();
                    ObservableList<String> observableList = FXCollections.observableArrayList(singleStatistic.getList());
                    snippet.setComboBox(observableList);
                    snippet.getComboBox().valueProperty().addListener(observable -> updateComboBoxSnippet(singleStatistic.getList(), singleStatistic));
                    snippet.getComboBox().getSelectionModel().select(0);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates statistics, and their statisticSnippets
     * with every change in the input price range.
     */
    public void update()
    {
        statistics.updateStatistics();
        updateSnippets();
    }

    /**
     * Updates the attribute values in each SingleStatistic from statistics List.
     * It updates the StatisticsSnippet by calling updateSingleSnippet method.
     * It uses Map attributes to get the updated value for each statistic.
     */
    private void updateSnippets()
    {
        for (SingleStatistic statistic : statistics.getStatistics())
        {
            String newValue = statistics.getAttributes().get(statistic.getAttributeName());
            updateSingleSnippet(statistic, newValue);
        }
    }

    /**
     * Change the value of SingleStatistic's attribute
     * and update StatisticsSnippet by changing its valueLabel.
     * @param statistic SingleStatistic parameters to be changed.
     * @param newValue New value to be displayed on valueLabel.
     */
    private void updateSingleSnippet(SingleStatistic statistic, String newValue)
    {
        statistic.changeAttribute(newValue);
        statistic.getStatisticsSnippet().changeValueLabel(newValue);
    }

    /**
     * Call methods that accordingly update the StatisticsSnippet
     * where the new value was put by the user in the ComboBox.
     * @param list List from SingleStatistic's parameters.
     * @param singleStatistic SingleStatistic itself.
     */
    private void updateComboBoxSnippet(List<String> list, SingleStatistic singleStatistic)
    {
        statistics.updateComboBoxStatistic(list);
        updateSingleSnippet(singleStatistic, statistics.getAttributes().get(singleStatistic.getAttributeName()));
    }

    /**
     * Fill 4 BorderPanes in the Panel by setting their center to StatisticSnippets.
     */
    private void fillPanes()
    {
        for (int i = 0; i < 4; i++)
        {
            borderPaneList.get(i).setCenter(statisticsPanes.get(0));
            statisticsPanes.remove(0);
        }
    }

    /**
     * Changes the content in the BorderPane accordingly to the clicked button.
     * The old StatisticsSnippet fades out and the new one fades in. The application
     * uses a list to keep track of which StatisticsSnippet has to appear to keep
     * a logical order.
     */
    @FXML
    private void changePane(MouseEvent event)
    {
        BorderPane clickedBorderPane = (BorderPane)((Node)event.getTarget()).getParent();

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), clickedBorderPane.getCenter());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();

        int index;
        if(clickedBorderPane.getLeft() == event.getTarget()) {
            statisticsPanes.add(0,(BorderPane)clickedBorderPane.getCenter());
            index = statisticsPanes.size() - 1;
        }
        else {
            statisticsPanes.add((BorderPane)clickedBorderPane.getCenter());
            index = 0;
        }

        clickedBorderPane.setCenter(statisticsPanes.get(index));
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), statisticsPanes.get(index));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        statisticsPanes.remove(index);
    }

    /**
     * Changes the image of the "arrow" button when user hovers on it.
     * @param event Mouse action occurred.
     */
    @FXML
    private void changeColorOn(MouseEvent event)
    {
        BorderPane clickedBorderPane = (BorderPane)((Node)event.getTarget()).getParent();
        ImageView imageView = (ImageView) event.getTarget();
        Image hoverImage = null;
        if (clickedBorderPane.getLeft() == event.getTarget()) {
            hoverImage = new Image("/images/statistics-left-red.png");
        }
        else {
            hoverImage = new Image("/images/statistics-right-red.png");
        }
        imageView.setImage(hoverImage);
    }

    /**
     * Changes the image of the "arrow" button back to its usual
     * one after user stops hovering his mouse on it.
     * @param event Mouse action occurred.
     */
    @FXML
    private void changeColorOff(MouseEvent event)
    {
        BorderPane clickedBorderPane = (BorderPane)((Node)event.getTarget()).getParent();
        ImageView imageView = (ImageView) event.getTarget();
        Image normalImage = null;
        if (clickedBorderPane.getLeft() == event.getTarget()) {
            normalImage = new Image("/images/statistics-left.png");
        }
        else {
            normalImage = new Image("/images/statistics-right.png");
        }
        imageView.setImage(normalImage);
    }
}
