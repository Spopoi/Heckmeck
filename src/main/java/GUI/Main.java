package GUI;

import Heckmeck.Tile;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        BorderPane borderPane = new BorderPane();
        Tiles tiles = new Tiles();
        borderPane.setCenter(tiles.getTilePane());
        //String javaVersion = System.getProperty("java.version");
        //String javafxVersion = System.getProperty("javafx.version");
        //Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        //Scene scene = new Scene(new StackPane(l), 640, 480);
        //Parent root = new VBox();

        //Tiles tiles = new Tiles();

        Scene root = new Scene(borderPane, 600, 250);
        stage.setTitle("Heckmeck");
        stage.setScene(root);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}