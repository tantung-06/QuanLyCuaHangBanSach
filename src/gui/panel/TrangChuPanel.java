package gui.panel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TrangChuPanel extends JPanel {

    private BufferedImage backgroundImage;

    public TrangChuPanel() {
        try {
            backgroundImage = ImageIO.read(new File("src/img/trangchu.png"));
        } catch (IOException e) {
            System.err.println("Không tìm thấy file ảnh: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
