package GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tiles{

    //map button-Tile
    //listener of Tiles change
    private TilePane tilePane;
    private List<Button> tileButtons;
    private static final String worm = "\uD83D\uDc1B";

    public Tiles(){
        configTilePane();
        tileButtons = IntStream.range(21,37).mapToObj(Tiles::generateTileButton).collect(Collectors.toList());
        for (int i = 0; i < tileButtons.size(); i++) {
            int tileNumber = i +21;
            tileButtons.get(i).setOnAction(event -> {
                ActionLog.setLastAction("Tile #"+ tileNumber + " selected");
                tilePane.getChildren().remove(tileButtons.get(tileNumber-21));

            });
            tilePane.getChildren().add(tileButtons.get(i));
        }
    }

    private static Button generateTileButton(int tileNumber) {
        String tileText = Integer.toString(tileNumber) + '\n';
        if(tileNumber < 25) tileText += worm;
        else if(tileNumber < 29)  tileText += worm + worm;
        else tileText += worm + worm + '\n' + worm + worm;
        Button button = new Button(tileText);
        button.setPrefSize(60,80);
        return button;
    }

    public TilePane getPane() {
        return tilePane;
    }

//    public void setTileButtonAction(int tileNumber){
//        tileButtons[tileNumber-21].setOnAction(event -> {
//            ActionLog.setLastAction("Tile #"+ tileNumber + " selected");
//            tilePane.getChildren().remove(tileButtons[tileNumber-21]);
//        });
//    }

    private void configTilePane() {
        tilePane = new TilePane();
        tilePane.setPadding(new Insets(30));
        tilePane.setHgap(8);
        tilePane.setVgap(10);
        tilePane.setPrefColumns(8);
    }
}
