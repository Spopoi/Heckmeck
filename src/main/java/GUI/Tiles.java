package GUI;

import Heckmeck.Tile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static javafx.scene.input.KeyCode.U;

public class Tiles {

    public Stage stage;
    private TilePane tilePane;
    private Scene scene;
    private Button[] buttons;
    private final String worm = "\uD83D\uDc1B";

    public Tiles(Stage stage){
        this.stage = stage;
        buildTilesUI();
    }

    private void buildTilesUI() {
        configTilePane();

        for (int tileNumber = 21; tileNumber < 21+ Heckmeck.Tiles.numberOfTiles; tileNumber++) {
            if(tileNumber < 25){
                Button button = new Button(Integer.toString(tileNumber) + '\n' + worm);
                button.setPrefSize(60,80);
                tilePane.getChildren().add(button);
            }
            else if( tileNumber < 29){
                Button button = new Button(Integer.toString(tileNumber) + '\n' + worm + worm);
                button.setPrefSize(60,80);
                tilePane.getChildren().add(button);
            }
            else {
                Button button = new Button(Integer.toString(tileNumber) + '\n' + worm + worm + '\n' + worm + worm);
                button.setPrefSize(60,80);
                //button.setAlignment(Pos.BOTTOM_LEFT);
                tilePane.getChildren().add(button);
            }
        }

        scene = new Scene(tilePane, 600, 250);
        stage.setTitle("Tiles");
        stage.setScene(scene);
    }

    private void configTilePane() {
        tilePane = new TilePane();
        tilePane.setPadding(new Insets(30));
        tilePane.setHgap(8);
        tilePane.setVgap(10);
        tilePane.setPrefColumns(8);
    }

    public void show(){
        this.stage.show();
    }
}
