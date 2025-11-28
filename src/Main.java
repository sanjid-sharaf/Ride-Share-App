import javax.swing.SwingUtilities;

import controllers.MainController;
import ui.ridegui;
import models.Database;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Ride-Share App Starting ===");

        // Create main controller (shared between console demo and GUI)
        MainController mainController = new MainController();

        SwingUtilities.invokeLater(() -> {
            ridegui gui = new ridegui();
            gui.initWithController(mainController);
        });


        System.out.println("=== GUI Launched ===");
    }
}