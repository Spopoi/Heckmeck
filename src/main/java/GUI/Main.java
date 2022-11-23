package GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class Main extends Application {
    private Button[] tileButtons;
    private TilePane tiles;
    private Label lastAction;
    private final String worm = "\uD83D\uDc1B";
    @Override
    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();
        //Tiles tiles = new Tiles();
        tiles = tiles();
        borderPane.setCenter(tiles);

        lastAction = new Label("");
        BorderPane.setAlignment(lastAction, Pos.CENTER);
        borderPane.setBottom(lastAction);


        Scene root = new Scene(borderPane, 600, 250);
        stage.setTitle("Heckmeck");
        stage.setScene(root);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    private TilePane tiles(){
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(30));
        tilePane.setHgap(8);
        tilePane.setVgap(10);
        tilePane.setPrefColumns(8);
        tileButtons = new Button[Heckmeck.Tiles.numberOfTiles];
        for (int tileNumber = 21; tileNumber < 21+ Heckmeck.Tiles.numberOfTiles; tileNumber++) {
            if(tileNumber < 25){
                Button button = new Button(Integer.toString(tileNumber) + '\n' + worm);
                button.setPrefSize(60,80);
                int finalTileNumber = tileNumber;
                button.setOnAction(event -> selectTile(button, finalTileNumber));
                tileButtons[tileNumber-21]=button;
                tilePane.getChildren().add(button);
            }
            else if( tileNumber < 29){
                Button button = new Button(Integer.toString(tileNumber) + '\n' + worm + worm);
                button.setPrefSize(60,80);
                tileButtons[tileNumber-21]=button;
                tilePane.getChildren().add(button);
            }
            else {
                Button button = new Button(Integer.toString(tileNumber) + '\n' + worm + worm + '\n' + worm + worm);
                button.setPrefSize(60,80);
                tileButtons[tileNumber-21]=button;
                tilePane.getChildren().add(button);
            }
        }
        return tilePane;
    }

    private void selectTile(Button button, int tileNumber) {
        lastAction.setText("Tile #"+ tileNumber + " selected");
        tiles.getChildren().remove(button);
    }

}