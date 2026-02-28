package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModernTextField extends JTextField {

    private static final Color BG_INPUT    = new Color(20, 25, 32);
    private static final Color ACCENT_BLUE = new Color(56, 139, 253);
    private static final Color BORDER_COLOR= new Color(48, 54, 61);
    private static final Color TEXT_PRIMARY= new Color(230, 237, 243);
    private static final Color TEXT_MUTED  = new Color(110, 118, 129);
    private static final Font  FONT_LABEL  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);

    private String  placeholder;
    private boolean focused = false;

    public ModernTextField(String placeholder) {
        this.placeholder = placeholder;
        setOpaque(false);
        setFont(FONT_LABEL);
        setForeground(TEXT_PRIMARY);
        setCaretColor(ACCENT_BLUE);
        setBorder(null);

        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { focused = true;  repaint(); }
            public void focusLost (FocusEvent e)  { focused = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fond
        g2.setColor(BG_INPUT);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

        // Bordure normale / focus
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(focused ? ACCENT_BLUE : BORDER_COLOR);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

        // Glow focus
        if (focused) {
            g2.setColor(new Color(56, 139, 253, 30));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);
        }
        g2.dispose();

        setMargin(new Insets(0, 12, 0, 12));
        super.paintComponent(g);

        // Placeholder
        if (getText().isEmpty() && !focused) {
            Graphics2D g3 = (Graphics2D) g.create();
            g3.setColor(TEXT_MUTED);
            g3.setFont(FONT_SMALL);
            FontMetrics fm = g3.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g3.drawString(placeholder, 12, y);
            g3.dispose();
        }
    }
}