package org.ns.vkcachegrabber;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ns.util.Closeable;
import org.ns.util.Utils;
import org.ns.vkcachegrabber.api.Application;

/**
 * 
 * @author stupak
 */
//TODO реализовать
public class Config implements Closeable {

    private static final Logger logger = Logger.getLogger(Config.class.getName());
    private static final String CONFIG = "conf.properties";
    
    private final Application application;
    private final Properties properties;
    
    public Config(Application application) {
        this.application = application;
        this.properties = new Properties();
        load();
    }

    public String getAppConfigFolder() {
        return System.getProperty("user.home") + File.separator + "." + Config.this.application.getName() + File.separator;
    }
    
    public String get(String key) {
        return properties.getProperty(key);
    }
    
    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public void set(String key, String value) {
        properties.setProperty(key, value);
    }
    
    private void save() {
        File configFile = new File(getAppConfigFolder() + CONFIG);
        OutputStream os = null;
        try {
            os = new FileOutputStream(configFile);
            properties.store(os, null);
            logger.log(Level.INFO, "Config saved to file {0}", configFile.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Utils.closeSilent(os);
        }
    }

    private void load() {
        File configFile = new File(getAppConfigFolder() + CONFIG);
        if ( configFile.exists() ) {
            logger.log(Level.INFO, "Finded config file. patch={0}", configFile.getAbsolutePath());
        }
        InputStream is = null;
        try {
            is = new FileInputStream(configFile);
            properties.load(is);
            logger.log(Level.INFO, "Loaded config file. patch={0}", configFile.getAbsolutePath());
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            Utils.closeSilent(is);
        }
    }
    
    @Override
    public void close() {
        save();
    }
}
