package GUI;

import Heckmeck.Player;
import com.sun.prism.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class PlayerGround {

    private Player player;
    private VBox pane;
    private Button playerTiles;

    public PlayerGround(Player player){
        this.pane = new VBox(60);
        pane.setPadding(new Insets(20));


        Label playerName = new Label(player.getName());
        playerName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));


        this.playerTiles = new Button("No tile");
        playerTiles.setPrefSize(70,100);


        pane.getChildren().add(playerName);
        pane.getChildren().add(playerTiles);

    }

    public void setPlayerTiles(Button tiles){
        playerTiles.setText(tiles.getText());
    }

    public VBox getPane(){
        return pane;
    }
}
