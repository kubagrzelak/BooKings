import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import net.java.html.boot.fx.FXBrowsers;
import net.java.html.leaflet.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An interactive map view implementation using JavaScript Leaflet
 * map and a WebView.
 * @version 2020.03.25
 */
public class MapView extends StackPane {
    // the webview hosting the map
    private final WebView webView;
    // JS Leaflet map
    private Map map;

    // center coordinates for the map
    private double latitude;
    private double longitude;

    /**
     * Constructor for a MapView object.
     * Create the WebView and load the map using it.
     * @param latitude Center latitude for the map.
     * @param longitude Center longitude for the map.
     */
    public MapView(double latitude, double longitude) {
        // disable warning messages from third party class
        Logger.getLogger("net.java.html.boot.BrowserBuilder").setLevel(Level.OFF);

        this.latitude = latitude;
        this.longitude = longitude;

        webView = new WebView();
        getChildren().add(webView);

        // load an empty html page using the web view's engine
        // if loaded load the map onto it
        FXBrowsers.load(webView, getClass().getResource("/html/index.html"), this::showMap);
    }

    /**
     * Create and show the Leaflet map and add the marker to the provided coordinates.
     */
    private void showMap() {
        map = new Map("map");
        LatLng coordinates = new LatLng(latitude, longitude);

        // center the map around the provided location
        map.setView(coordinates, 17);

        // add the map layer from MapBox API
        map.addLayer(new TileLayer("https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoidGFwc29taWxpYW4iLCJhIjoiY2s3dGxzcTN4MHIzczNuc2Y4a3hzczFvZCJ9.nAy3sJVJgbDwvMWTBy16jA",
                new TileLayerOptions()
                        .setAttribution(
                                "Map data &copy;"
                                        + "<a href=\"https://www.openstreetmap.org/\">OpenStreetMap</a> contributors,"
                                        + "<a href=\"https://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Imagery ©"
                                        + "<a href=\"https://www.mapbox.com/\">Mapbox</a>")
                        .setMaxZoom(19)
                        .setMinZoom(10)
                        .setId("mapbox/satellite-streets-v11")
                        .setTileSize(256)
        ));

        // add the marker to the provided location
        map.addLayer(new Marker(coordinates, new MarkerOptions().setIcon(new Icon(new IconOptions("../images/map-marker.png").setIconSize(new Point(48, 96))))));
    }
}
