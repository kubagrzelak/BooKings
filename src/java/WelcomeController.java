import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @version 2020.03.25
 */
public class WelcomeController {

    @FXML private StackPane welcomePane;
    @FXML private ImageView welcomeImage;
    @FXML private Label prompt;

    // the blinking effect on the instruction label
    SequentialTransition blinkTransition;

    /**
     * Create the binding for the welcome image,
     * create the blinking transition and play it.
     */
    @FXML
    public void initialize() {
        welcomeImage.fitHeightProperty().bind(welcomePane.heightProperty());
        welcomeImage.fitWidthProperty().bind(welcomePane.widthProperty());

        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(1000), prompt);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(1000), prompt);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        blinkTransition = new SequentialTransition(fadeOutTransition, fadeInTransition);
        blinkTransition.setCycleCount(Animation.INDEFINITE);
        blinkTransition.play();
    }

    /**
     * Stop the blinking effect by decreasing the label's
     * opacity to zero.
     */
    public void stopBlink() {
        blinkTransition.stop();
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(1000), prompt);
        fadeOutTransition.setToValue(0.0);
        fadeOutTransition.play();
    }
}
