package org.ns.vkcachegrabber.ui;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import org.ns.ioc.IoC;
import org.ns.vkcachegrabber.api.Application;
import org.ns.vkcachegrabber.vk.model.Audio;
import org.ns.vkcachegrabber.vk.VKApi;
import org.ns.vkcachegrabber.vk.VkException;

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
            audio = vkApi.getAudioById(ownerId, audioId);
        } catch (VkException ex) {
            Logger.getLogger(TestAction.class.getName()).log(Level.INFO, "Error when audio loading ", ex);
        }
        if ( audio != null ) {
            Logger.getLogger(TestAction.class.getName()).log(Level.INFO, "Audio loaded: {0}", audio);
        }
    }
    
}
