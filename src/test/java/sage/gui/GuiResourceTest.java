package sage.gui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class GuiResourceTest {
    @Test
    void guiResources_areAvailableOnClasspath() {
        assertNotNull(Main.class.getResource("/view/MainWindow.fxml"));
        assertNotNull(DialogBox.class.getResource("/view/DialogBox.fxml"));
        assertNotNull(Main.class.getResource("/css/main.css"));
        assertNotNull(DialogBox.class.getResource("/css/dialog-box.css"));
    }
}
