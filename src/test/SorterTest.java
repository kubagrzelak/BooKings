import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SorterTest {

    private List<Listing> dataset;
    private Sorter sorter;
    private List<Listing> expected;
    private List<Listing> sortedDataset;

    @BeforeEach
    void setUp() {
        dataset = new ArrayList<Listing>();
        dataset.add(new Listing("13355982", "Lovely double bedroom in Kingston Upon Thames", "75741819", "Maria", "Camden", 51.41585101, -0.28649628, "Private room", 24, 1, 2, "27/07/2016", 0.25, 1, 0));
        dataset.add(new Listing("4836957", "Modern room 25m from Central London", "18154504", "Anas", "Kingston upon Thames", 51.41148217, -0.290704075, "Private room", 50, 1, 15, "07/09/2016", 0.67, 1, 364));
        dataset.add(new Listing("13472704", "Double Room in North Kingston (Richmond Park)", "77078182", "Hannah", "City of London", 51.41572344, -0.292245941, "Private room", 50, 2, 0, "", 0, 1, 0));
        dataset.add(new Listing("15896822", "Double room in newly refurbished flat", "69018624", "Dafina", "Kingston upon Thames", 51.41003566, -0.306322953, "Private room", 23, 7, 1, "03/12/2016", 0.32, 1, 61));
        expected = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        assertEquals(expected, sortedDataset);
    }

    @Test
    void sortByPrice() {
        // descending
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        sortedDataset = Sorter.sortByPrice(expected, false);
        tearDown();

        setUp();
        // ascending
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        sortedDataset = Sorter.sortByPrice(expected, true);
    }

    @Test
    void sortByHostID() {
        // ascending
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        sortedDataset = Sorter.sortByHostID(expected, true);
        tearDown();

        setUp();
        // descending
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        sortedDataset = Sorter.sortByHostID(expected, false);
    }

    @Test
    void sortByNeighbourhood() {
        // descending
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        sortedDataset = Sorter.sortByNeighbourhood(expected, false);
        tearDown();

        setUp();
        // ascending
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        sortedDataset = Sorter.sortByNeighbourhood(expected, true);
    }

    @Test
    void sortByHostName() {
        // ascending
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        sortedDataset = Sorter.sortByHostName(expected, true);
        tearDown();

        setUp();
        // descending
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        sortedDataset = Sorter.sortByHostName(expected, false);
    }

    @Test
    void sortByReviews() {
        // descending
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        sortedDataset = Sorter.sortByReviews(expected, false);
        tearDown();

        setUp();
        // ascending
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        sortedDataset = Sorter.sortByReviews(expected, true);
    }

    @Test
    void sortByMinNights() {
        // sory by ascending minimum nights.
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        sortedDataset = Sorter.sortByMinNights(expected, true);
        tearDown();

        // sory by descending minimum nights.
        setUp();
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        sortedDataset = Sorter.sortByMinNights(expected, false);
    }

    @Test
    void sortByMinimumPrice() {
        // Sory by ascending minimum nights * price.
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        sortedDataset = Sorter.sortByMinNights(expected, true);
        tearDown();

        // Sory by descending minimum nights * price.
        setUp();
        expected.add(new Listing("15896822","Double room in newly refurbished flat","69018624","Dafina","Kingston upon Thames",51.41003566,-0.306322953,"Private room",23,7,1,"03/12/2016",0.32,1,61));
        expected.add(new Listing("13472704","Double Room in North Kingston (Richmond Park)","77078182","Hannah","City of London",51.41572344,-0.292245941,"Private room",50,2,0,"",0,1,0));
        expected.add(new Listing("4836957","Modern room 25m from Central London","18154504","Anas","Kingston upon Thames",51.41148217,-0.290704075,"Private room",50,1,15,"07/09/2016",0.67,1,364));
        expected.add(new Listing("13355982","Lovely double bedroom in Kingston Upon Thames","75741819","Maria","Camden",51.41585101,-0.28649628,"Private room",24,1,2,"27/07/2016",0.25,1,0));
        sortedDataset = Sorter.sortByMinNights(expected, false);
    }
}
