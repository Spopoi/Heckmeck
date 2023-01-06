package GUI;

import Heckmeck.OutputHandler;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.*;
import static javax.swing.JOptionPane.showMessageDialog;

public class GUIOutputHandler{

    private JFrame frame;
    public GUIOutputHandler(JFrame frame){
        this.frame =frame;
        frame.setVisible(true);
    }
    //@Override
    public void showWelcomeMessage(){
        showMessageDialog(null, "WELCOME to Heckmeck!");
    }

    public void wantToPlay(){
//        JLabel message = new JLabel("Are you ready for playing? :)");
//        message.setHorizontalAlignment(SwingConstants.CENTER);
//        frame.getContentPane().add(message, BorderLayout.CENTER);
//        frame.setVisible(true);
//        //showMessageDialog(null, "Are you ready for playing? :)");
//        //int result = showConfirmDialog(null, "Are you ready for playing? :)");
    }


    public void printMessage(String message){
        showMessageDialog(null, message);
    }


}
