package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ModernButton extends JButton {

    private Color baseColor;
    private float hoverAlpha = 0f;
    private Timer hoverTimer;

    public ModernButton(String text, Color color, Font font) {
        super(text);
        this.baseColor = color;
        setFont(font);
        setForeground(Color.WHITE);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(10, 16, 10, 16));
        setHorizontalAlignment(SwingConstants.LEFT);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { animateTo(1f); }
            public void mouseExited (MouseEvent e) { animateTo(0f); }
        });
    }

    private void animateTo(float target) {
        if (hoverTimer != null) hoverTimer.stop();
        hoverTimer = new Timer(16, null);
        hoverTimer.addActionListener(e -> {
            float step = target > hoverAlpha ? 0.08f : -0.08f;
            hoverAlpha += step;
            if ((step > 0 && hoverAlpha >= target) || (step < 0 && hoverAlpha <= target)) {
                hoverAlpha = target;
                hoverTimer.stop();
            }
            repaint();
        });
        hoverTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fond semi-transparent
        int alpha = (int)(40 + hoverAlpha * 80);
        g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

        // Bordure
        g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(),
                              (int)(120 + hoverAlpha * 135)));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

        // Indicateur couleur à gauche
        g2.setColor(baseColor);
        g2.fillRoundRect(0, 6, 3, getHeight() - 12, 2, 2);

        g2.dispose();
        super.paintComponent(g);
    }
}