package org.ns.vkcachegrabber.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author stupak
 */
public class BrandingPane extends JPanel {

    private JLabel brandingLabel;
    
    public BrandingPane() {
        super();
        initComponents();
        layoutComponents();
    }
    
    private void initComponents() {
        setBackground(Color.white);
        
        brandingLabel = new JLabel("VK CACHE GRABBER");
        brandingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        brandingLabel.setVerticalAlignment(SwingConstants.CENTER);
        Font font = brandingLabel.getFont();
        Map<TextAttribute, Object> attrs = new HashMap<>();
        attrs.put(TextAttribute.SIZE, 24);
        attrs.put(TextAttribute.FOREGROUND, Color.BLUE);
        font = font.deriveFont(attrs);
        brandingLabel.setFont(font);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(brandingLabel, BorderLayout.CENTER);
    }
}
