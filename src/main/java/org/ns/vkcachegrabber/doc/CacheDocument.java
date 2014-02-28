package org.ns.vkcachegrabber.doc;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import org.ns.gui.LinkLabel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.ns.gui.LinkLabel.GoToLinkListener;
import org.ns.util.Strings;
import org.ns.vkcachegrabber.ui.AbstractDocument;

/**
 *
 * @author stupak
 */
class CacheDocument extends AbstractDocument {

    
    
    private final JPanel root;
    private final GoToLinkListener cachePathOpener = new GoToLinkListener() {

        @Override
        public void goToLink() {
        }
    };

    public CacheDocument() {
        this.root = new JPanel(new BorderLayout());
    }
    
    @Override
    protected void processOpenableChanged() {
        String cachePath = (String) openable.getParameter(CacheHandler.CACHE_PATH);
        if ( Strings.empty(cachePath) ) {
            root.removeAll();
            root.add(createEmptyCachePathPane(), BorderLayout.CENTER);
        }
    }

    @Override
    public JComponent getComponent() {
        return root;
    }

    private JPanel createEmptyCachePathPane() {
        JPanel emptyCachePathPane = new JPanel();
        BoxLayout layout = new BoxLayout(emptyCachePathPane, BoxLayout.Y_AXIS);
        emptyCachePathPane.setLayout(layout);
        JLabel label = new JLabel("Выберите папку кеша аудиазаписей ВКонтакте.");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        emptyCachePathPane.add(label);
        LinkLabel selectLabel = new LinkLabel("Выбрать...", cachePathOpener);
        emptyCachePathPane.add(selectLabel);
        return emptyCachePathPane;
    }
}
