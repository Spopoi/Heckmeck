package GUI;

import Heckmeck.*;

import javax.swing.*;
import java.awt.*;

public class HeckmeckGUI {

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(HeckmeckGUI::gui);
        gui();
    }

    private static void gui(){
        JFrame frame = new JFrame("HECKMECK");
        frame.setSize(1100,600);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(50,30));
        GUIIOHandler io = new GUIIOHandler(frame);
        Game game = new Game(io);
        game.init();
        game.play();
    }
}