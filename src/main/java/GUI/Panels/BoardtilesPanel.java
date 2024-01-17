package GUI.Panels;

import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Tile;

import javax.swing.*;
import java.awt.*;
import static Utils.GUI.IconHandler.getTileIcon;

public class BoardtilesPanel extends JPanel {
    private static final int TILES_GAP = 10;
    private static final int TOP_BORDER = 20;
    private static final int BOARDTILES_BOTTOM_BORDER = 40;
    public BoardtilesPanel(){
        super();
        setLayout(new FlowLayout(FlowLayout.CENTER, TILES_GAP, 0));
        setBorder(BorderFactory.createEmptyBorder(TOP_BORDER, 0, BOARDTILES_BOTTOM_BORDER, 0));
        setOpaque(false);
    }

    public void update(BoardTiles tiles){
        removeAll();
        for (Tile tile : tiles.tiles()) {
            add(getTileIcon(tile.number()));
        }
        revalidate();
        repaint();
    }
}
