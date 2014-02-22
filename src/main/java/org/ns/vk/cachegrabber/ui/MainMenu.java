package org.ns.vk.cachegrabber.ui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 *
 * @author stupak
 */
public class MainMenu extends JMenuBar {

    public MainMenu() {
        super();
        
        JMenu file = new JMenu("File");
        JMenuItem openCache = new JMenuItem("Open VkCache");
        JMenuItem exit = new JMenuItem("Exit");
        TestAction testAction = new TestAction();
        file.add(testAction);
        file.add(openCache);
        file.add(new JSeparator());
        file.add(exit);
        add(file);
        
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About...");
        help.add(about);
        add(help);
    }
    
}
