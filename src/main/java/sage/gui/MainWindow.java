package sage.gui;

import java.util.Objects;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import sage.Sage;

/**
 * Controls the main Sage chat window.
 */
public class MainWindow extends AnchorPane {
    private static final String WELCOME_MESSAGE = "Hello! I'm Sage.\nWhat can I do for you?";
    private static final Duration EXIT_DELAY = Duration.seconds(1.5);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Sage sage;

    /**
     * Creates the controller used by the main-window FXML loader.
     */
    public MainWindow() {
    }

    /**
     * Initializes the conversation display after its FXML controls are injected.
     */
    @FXML
    private void initialize() {
        dialogContainer.heightProperty().addListener((observable, oldHeight, newHeight) ->
                scrollPane.setVvalue(scrollPane.getVmax()));
        dialogContainer.getChildren().add(DialogBox.getSageDialog(WELCOME_MESSAGE));
        Platform.runLater(userInput::requestFocus);
    }

    /**
     * Connects this window to the long-lived Sage backend.
     *
     * @param sage The backend that processes commands and retains task state.
     */
    public void setSage(Sage sage) {
        this.sage = Objects.requireNonNull(sage);
    }

    /**
     * Sends the entered command to Sage and displays both sides of the exchange.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().strip();
        if (input.isEmpty()) {
            userInput.clear();
            return;
        }

        Sage activeSage = Objects.requireNonNull(sage, "Sage must be set before accepting input.");
        String response = activeSage.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getSageDialog(response));
        userInput.clear();
        userInput.requestFocus();

        if (activeSage.isExit()) {
            closeAfterFarewell();
        }
    }

    /**
     * Disables further input and closes the app after the farewell can be read.
     */
    private void closeAfterFarewell() {
        userInput.setDisable(true);
        sendButton.setDisable(true);

        PauseTransition exitDelay = new PauseTransition(EXIT_DELAY);
        exitDelay.setOnFinished(event -> Platform.exit());
        exitDelay.play();
    }
}
