import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 *  The JavaFX controller for the List window that appears for borough views or the favourite view.
 *  @version 2020.03.25
 */
public class ListController {

    // Label to represent borough listed
    @FXML private Label boroughLabel;
    // ComboBox to choose how many listings to show per page
    @FXML private ComboBox<String> listingPerPageBox;
    // ComboBox to choose how to sort listing
    @FXML private ComboBox<String> sortByBox;
    // ComboBox to choose direction of sort
    @FXML private ComboBox<String> directionBox;
    // Pagination to represent all the listings in different pages.
    @FXML private Pagination pagination;
    // Label to show how many properties are being listed.
    @FXML private Label numberOfProperties;
    // List of listings being shown
    private List<Listing> listings;
    // List of sorted listings to show
    private List<Listing> sortedListings;

    // Initial number of listings per page.
    int listingPerPage = 32;

    public ListController() {
        listings = new ArrayList<>();
    }

    /**
     * initialize the ListController
     * set the max page indicator to 5
     * set the page factory of the pagination to fillpage() utilising the page index and the list of listings.
     * also initialize sort comboboxes.
     */
    @FXML
    public void initialize() {
        pagination.setMaxPageIndicatorCount(5);
        pagination.setPageFactory(pageIndex -> fillPage(pageIndex, listings));

        ObservableList<String> listingPerPageOptions = FXCollections.observableArrayList(
                "16",
                "32",
                "64",
                "100"
        );
        listingPerPageBox.setItems(listingPerPageOptions);
        ObservableList<String> sortOptions = FXCollections.observableArrayList(
                "Price (per Night)",
                "Minimum Price",
                "Minimum Nights",
                "Number of Reviews",
                "Host Name",
                "Host ID"
        );
        sortByBox.setItems(sortOptions);
        ObservableList<String> sortDirections = FXCollections.observableArrayList(
                "Ascending",
                "Descending",
                "-"
        );
        directionBox.setItems(sortDirections);
    }

    /**
     * Generates a scroll pane of listings according to the page of Pagination selected.
     * @param pageIndex the page of the Pagination
     * @param listings List of properties to display
     * @return scrollPane of listings for that page
     */
    public ScrollPane fillPage(int pageIndex, List<Listing> listings) {
        ScrollPane scrollPane = new ScrollPane();
        sortedListings = listings;
        scrollPane.setFitToWidth(true);
        if (!listings.isEmpty()) {
            VBox box = new VBox();
            int pageStartingIndex = pageIndex * listingPerPage;
            for (int i = pageStartingIndex; i < pageStartingIndex + listingPerPage && i < listings.size(); i++) {
                Listing listing = listings.get(i);
                box.getChildren().add(new PropertySnippet(listing, this));
            }
            scrollPane.setContent(box);
        }
        else {
            VBox box = new VBox();
            Label label = new Label("No listings found.");
            box.setAlignment(Pos.CENTER);
            box.getChildren().add(label);
            scrollPane.setContent(box);
        }
        return scrollPane;

    }

    /**
     * set the list of properties to list (show)
     * @param listings the list of listings
     * @param listName the name of the list
     */
    public void setListingList(List<Listing> listings, String listName) {
        this.listings = listings;
        setPageCount();
        boroughLabel.setText(listName);
        if (!listName.equals("Favourites")) {
            numberOfProperties.setText(listings.size() + " properties in the price range.");
        }
        else {
            numberOfProperties.setText(listings.size() + " properties.");

        }
    }

    /**
     * calculate and set the number of pages in the pagination based on the number of listings per page and number of listings.
     */
    private void setPageCount() {
        if (!listings.isEmpty()) {
            pagination.setPageCount((int) Math.ceil(listings.size() / (double)listingPerPage));
        }
        else {
            pagination.setPageCount(1);
        }
    }

    /**
     * set the number of listings per page based on the value selected on the relevant 'listingPerPageBox' combobox.
     * @param event the selection on the 'listingPerPageBox' combobox.
     */
    @FXML
    private void listingPerPageSelection(ActionEvent event) {
        listingPerPage = Integer.parseInt(listingPerPageBox.getValue());
        sortBySelection(null);
        setPageCount();
    }

    /**
     * sort the listings on the page based on the sort type selected on the relevant 'sortBySelection' and
     * 'directionSelection' combobox.
     * @param event the selection on the 'sortBySelection' combobox.
     */
    @FXML
    private void sortBySelection(ActionEvent event) {
        if (directionBox.getValue() == null && event != null) {
            directionBox.setDisable(false);
        } else {
            directionSelection(null);
        }

    }

    /**
     * sort the listings on the page based on the sort type selected on the relevant 'sortBySelection' and
     * 'directionSelection' combobox.
     * @param event the selection on the 'directionSelection' combobox.
     */
    @FXML
    private void directionSelection(ActionEvent event) {
        String sortType;
        boolean ascending;
        if ("Ascending".equals(directionBox.getValue())) {
            ascending = true;
            sortType = sortByBox.getValue();
        }
        else if ("Descending".equals(directionBox.getValue())) {
            ascending = false;
            sortType = sortByBox.getValue();
        }
        else {
            ascending = false;
            sortType = "None";
        }
        refreshPage(sortType, ascending);
    }

    /**
     * refreshes the page based on a sort type and sort direction.
     * @param sortType String sort type (valid strings include combobox strings)
     * @param ascending String sort direction (valid strings include combobox strings)
     */
    public void refreshPage (String sortType, boolean ascending) {
        switch (sortType) {
            case "Price (per Night)":
                pagination.setPageFactory(pageIndex -> fillPage(pageIndex, Sorter.sortByPrice(listings, ascending)));
                break;
            case "Minimum Price":
                pagination.setPageFactory(pageIndex -> fillPage(pageIndex, Sorter.sortByMinimumPrice(listings, ascending)));
                break;
            case "Minimum Nights":
                pagination.setPageFactory(pageIndex -> fillPage(pageIndex, Sorter.sortByMinNights(listings, ascending)));
                break;
            case "Number of Reviews":
                pagination.setPageFactory(pageIndex -> fillPage(pageIndex, Sorter.sortByReviews(listings, ascending)));
                break;
            case "Host Name":
                pagination.setPageFactory(pageIndex -> fillPage(pageIndex, Sorter.sortByHostName(listings, ascending)));
                break;
            case "Host ID":
                pagination.setPageFactory(pageIndex -> fillPage(pageIndex, Sorter.sortByHostID(listings, ascending)));
                break;
            case "None":
                pagination.setPageFactory(pageIndex -> fillPage(pageIndex, listings));
                break;
        }
    }

    public List<Listing> getSortedListings() {
        return sortedListings;
    }

}
