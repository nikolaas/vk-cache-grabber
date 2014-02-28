package org.ns.vkcachegrabber.ui;

/**
 *
 * @author stupak
 */
public abstract class ResultableDocument extends AbstractDocument {

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
