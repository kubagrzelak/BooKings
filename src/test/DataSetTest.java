import javafx.util.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataSetTest {

    private DataSet dataset;

    @BeforeEach
    void setUp() {
        dataset = new DataSet("test-dataset");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getPriceRange() {
        // Checks if the price range made by all properties is correct
        Pair<Integer, Integer> priceRange = dataset.getPriceRange();
        assertEquals(new Pair(23, 50), priceRange);
    }

    @Test
    void filterPrice() {

        // test valid price range with 1 property.
        dataset.filterPrice(23, 23);
        List<Listing> filteredDatasetList = dataset.getFilteredDatasetList();
        List<Listing> expectedFilteredDatasetList = new ArrayList<>();
        expectedFilteredDatasetList.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        assertEquals(expectedFilteredDatasetList, filteredDatasetList);

        // test price range with all properties (boundries)
        dataset.filterPrice(23,50);
        filteredDatasetList = dataset.getFilteredDatasetList();
        assertEquals(dataset.getDatasetList(), filteredDatasetList);

        // test price rannge without any properties (boundries)
        dataset.filterPrice(25,49);
        filteredDatasetList = dataset.getFilteredDatasetList();
        expectedFilteredDatasetList.clear();
        assertEquals(expectedFilteredDatasetList, filteredDatasetList);

        // test price range without listings.
        dataset.filterPrice(0, 22);
        filteredDatasetList = dataset.getFilteredDatasetList();
        expectedFilteredDatasetList.clear();
        assertEquals(expectedFilteredDatasetList, filteredDatasetList);

        // test invalid price range
        dataset.filterPrice(22, 0);
        filteredDatasetList = dataset.getFilteredDatasetList();
        assertEquals(expectedFilteredDatasetList, filteredDatasetList);

        // test invalid input price (-10 should be turn to '0')
        dataset.filterPrice(-10, 10000000);
        filteredDatasetList = dataset.getFilteredDatasetList();
        assertEquals(dataset.getDatasetList(), filteredDatasetList);
    }

    @Test
    void filterNeighbourhood() {
        // test valid borough name (expected 1)
        List<Listing> filteredDatasetList = dataset.filterNeighbourhood("City of London");
        List<Listing> expectedFilteredDatasetList = new ArrayList<>();
        expectedFilteredDatasetList.add(new Listing("13472704", "Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        assertEquals(expectedFilteredDatasetList, filteredDatasetList);

        // test valid borough name (expected 0)
        filteredDatasetList = dataset.filterNeighbourhood("Croydon");
        expectedFilteredDatasetList.clear();
        assertEquals(expectedFilteredDatasetList, filteredDatasetList);

        // test invalid borough name
        filteredDatasetList = dataset.filterNeighbourhood("Warsaw");
        expectedFilteredDatasetList.clear();
        assertEquals(expectedFilteredDatasetList, filteredDatasetList);
    }

    @Test
    void favourite() {
        // test adding to favourites
        dataset.favourite(dataset.getDatasetList().get(0));
        List<Listing> favourites = dataset.getFavourites();
        List<Listing> expectedFavourites = new ArrayList<>();
        expectedFavourites.add(dataset.getDatasetList().get(0));
        assertEquals(expectedFavourites, favourites);

        // test removing from favourites
        dataset.favourite(dataset.getDatasetList().get(0));
        expectedFavourites.clear();
        assertEquals(expectedFavourites, favourites);
    }
}
