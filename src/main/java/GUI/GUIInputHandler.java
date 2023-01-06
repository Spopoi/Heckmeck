package GUI;

import Heckmeck.InputHandler;
import exception.IllegalInput;

import javax.swing.*;

import java.awt.*;

import static javax.swing.JOptionPane.*;

public class GUIInputHandler{
    private JFrame frame;
    public GUIInputHandler(JFrame frame){
        this.frame = frame;
    }

    public boolean wantToPlay(){
        int result = showConfirmDialog(null, "Are you ready for playing?");
        if (result == JOptionPane.OK_OPTION) {
            return true;
        } else {
            showMessageDialog(null, "See you next time!");
            frame.dispose();
            return false;
        }
    }

    public int chooseNumberOfPlayers(){
        try{
            return Integer.parseInt(showInputDialog(null, "Choose number of players between 2 and 7:"));
        }catch(NumberFormatException ex){
            throw new IllegalInput("Invalid number of player, please select a number between 2 and 7");
        }
    }

    public String choosePlayerName(){
        return showInputDialog(null, "Insert player name");
    }
}
