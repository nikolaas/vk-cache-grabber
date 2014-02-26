package org.ns.vkcachegrabber.ui;

import org.ns.vkcachegrabber.api.Document;

/**
 *
 * @author stupak
 */
public abstract class ResultableDocument implements Document {

    public static final int OK_RESULT = 0;
    public static final int CANCEL_RESULT = 1;
    public static final int ERROR_RESULT = 2;
    
    private MainPane.ResultListener resultListener;
    
    protected final void setResult(Object result, int statusCode) {
        if ( resultListener != null )  {
            resultListener.onResult(result, statusCode);
        }
    }
    
    final void setResultListener(MainPane.ResultListener resultListener) {
        this.resultListener = resultListener;
    }
}
