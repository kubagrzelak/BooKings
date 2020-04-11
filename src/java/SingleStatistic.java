import java.util.List;

/**
 * Represents a single statistic in the application that has
 * general information about it such as description, StatisticsSnippet
 * and attribute name and value from "Statistics" class. Some SingleStatistics
 * have also a list of Strings that represent available choices
 * user can make while opening the comboBox of this statistic.
 */

public class SingleStatistic {

    // Indicates what is the data displayed by the program for example
    // "Amount of available properties".
    private String description;
    // Attribute name that gets updates in "Statistics" class.
    private String attributeName;
    // Attribute value that gets updated in "Statistics" class and is assigned here.
    private String attributeValue;
    // List of choices from comboBox that some single statistics provide.
    private List<String> list;
    // List of properties that show up then you click "show details" button.
    private List<Listing> statisticListings;
    // StatisticsSnippet, on which the SingleStatistic is displayed.
    private StatisticsSnippet statisticsSnippet;

    /**
     * Creates a SingleStatistic without a comboBox.
     * @param description Description of statistic.
     * @param attributeName Attribute name from "Statistics" class.
     * @param attributeValue Attribute value from "Statistics" class.
     * @param snippet StatisticsSnippet, on which the SingleStatistic is displayed.
     */
    public SingleStatistic(String description, String attributeName, String attributeValue, StatisticsSnippet snippet)
    {
        this.description = description;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        statisticsSnippet = snippet;
    }

    /**
     * Creates a SingleStatistic with a comboBox or List of properties it is related to.
     * @param description Description of statistic.
     * @param attributeName Attribute name from "Statistics" class.
     * @param attributeValue Attribute value from "Statistics" class.
     * @param snippet StatisticsSnippet, on which the SingleStatistic is displayed.
     * @param list List of choices in a comboBox.
     * @param listings List of Listings that show up when you click "show details" button.
     */
    public SingleStatistic(String description, String attributeName, String attributeValue, StatisticsSnippet snippet, List<String> list, List<Listing> listings)
    {
        this.description = description;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.list = list;
        statisticsSnippet = snippet;
        statisticListings = listings;
    }

    /**
     * Updates the attribute value.
     * @param newValue New value to be assigned to attributeValue.
     */
    public void changeAttribute(String newValue)
    {
        attributeValue = newValue;
    }

    /**
     * Sets the StatisticsSnippet.
     * @param snippet StatisticsSnippet to be set.
     */
    public void setStatisticsSnippet(StatisticsSnippet snippet)
    {
        statisticsSnippet = snippet;
    }

    /**
     * @return Statistic's description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return Statistic's attribute name.
     */
    public String getAttributeName()
    {
        return attributeName;
    }

    /**
     * @return Statistic's attribute value.
     */
    public String getAttributeValue()
    {
        return attributeValue;
    }

    /**
     * @return Statistic's list of choices in comboBox.
     */
    public List<String> getList()
    {
        return list;
    }

    /**
     * @return List of properties assigned to one singleStatistic.
     */
    public List<Listing> getStatisticListings()
    {
        return statisticListings;
    }


    /**
     * @return StatisticsSnippet assigned to SingleStatistic.
     */
    public StatisticsSnippet getStatisticsSnippet()
    {
        return statisticsSnippet;
    }
}
