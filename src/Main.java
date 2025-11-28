import javax.swing.SwingUtilities;

import controllers.MainController;
import ui.RideGUI;
import models.Database;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Ride-Share App Starting ===");

        // Create main controller (shared between console demo and GUI)
       

        SwingUtilities.invokeLater(() -> {
            RideGUI gui = new RideGUI();
            gui.init();
        });


        System.out.println("=== GUI Launched ===");
    }
    
}