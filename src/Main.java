import javax.swing.SwingUtilities;
import ui.RideGUI;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Ride-Share App Starting ===");
        SwingUtilities.invokeLater(() -> {
            RideGUI gui = new RideGUI();
            gui.init();
        });

        System.out.println("=== GUI Launched ===");
    }
    
}