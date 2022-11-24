package GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

public class Tiles{

    private TilePane tilePane;
    private Button[] tileButtons;
    private final String worm = "\uD83D\uDc1B";

    public Tiles(){
        configTilePane();
        tileButtons = new Button[Heckmeck.Tiles.numberOfTiles];
        for (int tileNumber = 21; tileNumber < 21+ Heckmeck.Tiles.numberOfTiles; tileNumber++) {
            if(tileNumber < 25){
                Button button = new Button(Integer.toString(tileNumber) + '\n' + worm);
                button.setPrefSize(60,80);
                tileButtons[tileNumber-21]=button;
                setTileButtonAction(tileNumber);
                tilePane.getChildren().add(button);
            }
            else if( tileNumber < 29){
                Button button = new Button(Integer.toString(tileNumber) + '\n' + worm + worm);
                button.setPrefSize(60,80);
                tileButtons[tileNumber-21]=button;
                setTileButtonAction(tileNumber);
                tilePane.getChildren().add(button);
            }
            else {
                Button button = new Button(Integer.toString(tileNumber) + '\n' + worm + worm + '\n' + worm + worm);
                button.setPrefSize(60,80);
                tileButtons[tileNumber-21]=button;
                setTileButtonAction(tileNumber);
                tilePane.getChildren().add(button);
            }
        }
    }

    public TilePane getPane() {
        return tilePane;
    }

    public void setTileButtonAction(int tileNumber){
        tileButtons[tileNumber-21].setOnAction(event -> {
            ActionLog.setLastAction("Tile #"+ tileNumber + " selected");
            tilePane.getChildren().remove(tileButtons[tileNumber-21]);
        });
    }

    private void configTilePane() {
        tilePane = new TilePane();
        tilePane.setPadding(new Insets(30));
        tilePane.setHgap(8);
        tilePane.setVgap(10);
        tilePane.setPrefColumns(8);
    }
}
