/*
package GUI;

import Heckmeck.Die;
import Heckmeck.InputHandler;
import exception.IllegalInput;

import javax.swing.*;

import java.awt.*;

import static javax.swing.JOptionPane.*;

public class GUIInputHandler implements InputHandler{
    private JFrame frame;
    public GUIInputHandler(JFrame frame){
        this.frame = frame;
    }

    @Override
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

    @Override
    public int chooseNumberOfPlayers(){
        try{
            return Integer.parseInt(showInputDialog(null, "Choose number of players between 2 and 7:"));
        }catch(NumberFormatException ex){
            throw new IllegalInput("Invalid number of player, please select a number between 2 and 7");
        }
    }

    @Override
    public String choosePlayerName(){
        return showInputDialog(null, "Insert player name");
    }

    @Override
    public void pressEnter(){

    }

    @Override
    public Die.Face chooseDiceFace() throws IllegalInput{

        String chosenDice = showInputDialog(null, "Which die face do you want to pick?");
        if (Die.stringToFaceMap.containsKey(chosenDice)) {
            return Die.stringToFaceMap.get(chosenDice);
        } else throw new IllegalInput("Incorrect input, choose between {1, 2, 3, 4, 5, w}: ");
    }

    @Override
    public boolean wantToPick() throws IllegalInput {
        int result = showConfirmDialog(null, "Do you want to pick?");
        return result == JOptionPane.OK_OPTION;
    }

    @Override
    public boolean wantToSteal() {
        int result = showConfirmDialog(null, "Do you want to steal?");
        return result == JOptionPane.OK_OPTION;
    }
}
*/
