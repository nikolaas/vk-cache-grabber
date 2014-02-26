package org.ns.vkcachegrabber.api;

/**
 *
 * @author stupak
 */
public interface DocumentManager {
    
    void open(Openable openable, OpenContext openContext);
    
    void openForResult(Openable openable, OpenContext openContext, ResultReceiver receiver);
    
    void closeCurrent();
    
    public static interface ResultReceiver {
        void onResult(int statusCode, Object result);
    }
}
