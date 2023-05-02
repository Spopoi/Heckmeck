package GUI;

import Heckmeck.*;

import javax.swing.*;
import java.awt.*;

public class HeckmeckGUI {

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(HeckmeckGUI::gui);
    }

    private static void gui(){
        JFrame frame = new JFrame("HECKMECK");
        frame.setSize(1100,600);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(50,30));
        GUIIOHandler io = new GUIIOHandler(frame);
        Game game = new Game(io);

        final var worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                game.init();
                game.play();
                return null;
            }
        };
        worker.execute();
    }
}
