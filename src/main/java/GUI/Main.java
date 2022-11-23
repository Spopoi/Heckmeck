package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        //this.stage = stage;
        BorderPane borderPane = new BorderPane();
        Tiles tiles = new Tiles();
        borderPane.setCenter(tiles.getTilePane());

        Scene root = new Scene(borderPane, 600, 250);
        stage.setTitle("Heckmeck");
        stage.setScene(root);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}