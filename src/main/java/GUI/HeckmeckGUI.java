package GUI;

import Heckmeck.Game;
import Heckmeck.IOHandler;
import Heckmeck.Rules;
import exception.IllegalInput;

import javax.swing.*;
import java.awt.*;

public class HeckmeckGUI {

    public static void main(String[] args) {
//        Game game = new Game(new GUIOutputHandler(), new GUIInputHandler());
//        game.init();
//        game.play();
        JFrame frame = new JFrame("HECKMECK");
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        GUIOutputHandler out = new GUIOutputHandler(frame);
        GUIInputHandler in = new GUIInputHandler(frame);

        out.showWelcomeMessage();
        out.wantToPlay();
        System.out.println(in.wantToPlay());
        System.out.println(in.choosePlayerName(0));
        //System.out.println(in.chooseNumberOfPlayers());
        //SwingUtilities.invokeLater(HeckmeckGUI::Main);
    }

    private static void Main(){
        JFrame frame = new JFrame("HECKMECK");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel("Hello, World!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(label, BorderLayout.CENTER);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(x -> frame.dispose());
        frame.getContentPane().add(closeButton, BorderLayout.SOUTH);
        frame.setSize(400, 200);
        frame.setVisible(true);
    }
}

//
//import Heckmeck.Player;
//import javafx.application.Application;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.TilePane;
//import javafx.stage.Stage;
//
//public class Main extends Application {
//
//    @Override
//    public void start(Stage stage) {
//        BorderPane borderPane = new BorderPane();
//        Tiles tiles = new Tiles();
//        borderPane.setCenter(tiles.getPane());
//
//        BorderPane.setAlignment(ActionLog.getLogLabel(), Pos.CENTER);
//        borderPane.setBottom(ActionLog.getLogLabel());
//
//
//
//        PlayerGround player1 = new PlayerGround(Player.generatePlayer("Davide"));
//        borderPane.setRight(player1.getPane());
//
//        Scene root = new Scene(borderPane, 600, 250);
//        stage.setTitle("Heckmeck");
//        stage.setScene(root);
//        stage.show();
//
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}


