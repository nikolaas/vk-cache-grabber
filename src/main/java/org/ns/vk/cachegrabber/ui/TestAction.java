package org.ns.vk.cachegrabber.ui;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import org.ns.ioc.IoC;
import org.ns.vk.cachegrabber.api.Application;
import org.ns.vk.cachegrabber.api.vk.Audio;
import org.ns.vk.cachegrabber.api.vk.VKApi;
import org.ns.vk.cachegrabber.api.vk.VkException;

/**
 *
 * @author stupak
 */
public class TestAction extends AbstractAction {

    public TestAction() {
        super("Test action");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VKApi vkApi = IoC.get(Application.class).getVKApi();
        
        String ownerId = "32659923";
        String audioId = "259636837";
        
        Audio audio = null;
        try {
            audio = vkApi.getById(ownerId, audioId);
        } catch (VkException ex) {
            Logger.getLogger(TestAction.class.getName()).log(Level.INFO, "Error when audio loading ", ex);
        }
        Logger.getLogger(TestAction.class.getName()).log(Level.INFO, "Audio loaded: {0}", audio);
    }
    
}
