package GUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();
        Tiles tiles = new Tiles();
        borderPane.setCenter(tiles.getPane());

        BorderPane.setAlignment(ActionLog.getLogLabel(), Pos.CENTER);
        borderPane.setBottom(ActionLog.getLogLabel());


        Scene root = new Scene(borderPane, 600, 250);
        stage.setTitle("Heckmeck");
        stage.setScene(root);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}