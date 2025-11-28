import javax.swing.SwingUtilities;
import controllers.MainController;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Ride-Share App Starting ===");

        // Create the main controller
        MainController mainController = new MainController();

        // Launch GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            mainController.showLoginGUI(); // login GUI now knows the controller
        });

        System.out.println("=== GUI Launched ===");

        
    }
}
