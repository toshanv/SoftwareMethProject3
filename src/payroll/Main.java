package payroll;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is the main driver that runs the program and starts the application
 * @author Christopher Nguyen
 * @author Toshanraju Vysyaraju
 */
public class Main extends Application {

    /**
     * start function starts the application window and sets its parameters
     * @param primaryStage initial stage of the application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        final int WINDOW_SIZE = 650;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Payroll Processing");
        primaryStage.setScene(new Scene(root, WINDOW_SIZE, WINDOW_SIZE));
        primaryStage.show();
    }


    /**
     * Main driver that runs the payroll processor
     * @param args is array of arguments passed in when starting the program
     */
    public static void main(String[] args) {
        launch(args);
    }
}
