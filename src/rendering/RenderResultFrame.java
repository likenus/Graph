package src.rendering;

import javax.swing.*;
import java.awt.*;

public class RenderResultFrame extends JFrame {
    JLabel imageLabel;

    public RenderResultFrame(Image initialImage) {
        this.setResizable(false);
        Dimension targetSize = new Dimension(initialImage.getWidth(null), initialImage.getHeight(null));
        this.setMinimumSize(targetSize);
        this.setMaximumSize(targetSize);
        this.setPreferredSize(targetSize);
        this.setSize(targetSize);
        imageLabel = new JLabel("");
        this.add(imageLabel);
        imageLabel.setBounds(0, 0, this.getWidth(), this.getHeight());
        imageLabel.setIcon(new ImageIcon(initialImage));
        imageLabel.setVisible(true);
        this.setVisible(true);
    }

    public void updateImage(Image image) {
        imageLabel.setIcon(new ImageIcon(image));
    }
}
