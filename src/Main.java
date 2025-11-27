import controllers.MainController;
import ui.ridegui;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Ride-Share App Starting ===");

        // Create main controller (shared between console demo and GUI)
        MainController mainController = new MainController();

        // Option 1: Run console demo
        mainController.runDemo();

        // Option 2: Launch GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            ridegui gui = new ridegui();
            gui.initWithController(mainController); // pass existing MainController to GUI
        });

        System.out.println("=== GUI Launched ===");
    }
}