import controllers.MainController;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Ride-Share App Demo ===");

        // Create main controller and run demo
        MainController app = new MainController();
        app.runDemo();

        System.out.println("=== Demo Finished ===");
    }
}
