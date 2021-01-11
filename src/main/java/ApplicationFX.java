import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ApplicationFX extends Application {

    /**
     * Start the javaFX app
     *
     * @param stage JavaFX stage
     */
    @Override
    public void start(Stage stage) {

        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on JJ Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * App start point
     *
     * @param args cli args
     */
    public static void main(String[] args) {

        launch();
    }

}
