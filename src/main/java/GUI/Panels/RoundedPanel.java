package GUI.Panels;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private Image backgroundImage;

    public RoundedPanel(String imagePath) {
        if (imagePath != null) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }
        setOpaque(false); // Rendi il pannello trasparente
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, width, height, 20, 20);
        g2d.clip(roundedRect);

        if (backgroundImage != null) {
            // Imposta l'opacita per rendere l'immagine di sfondo trasparente
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            // Disegna l'immagine di sfondo
            g2d.drawImage(backgroundImage, 0, 0, width, height, this);

            // Ripristina l'opacita predefinita
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        } else {
            // Se l'immagine di sfondo a null, imposta il colore di sfondo a bianco
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(0, 0, width, height, 20, 20);
        }

        g2d.dispose();
    }
}