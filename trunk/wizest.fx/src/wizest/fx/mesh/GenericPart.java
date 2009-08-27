/*
 * Created on 2004. 8. 12
 */
package wizest.fx.mesh;
/**
 * @author user
 */
public/* abstract */class GenericPart implements Part {
    PartContext partContext;

    public String getPartId() {
        return getClass().getName();
    }

    /**
     * @see afterStart() to override this.
     */
    public final void start(PartContext context) throws OperationException {
        this.partContext = context;
        afterStart(context);
    }

    protected/* abstract */void afterStart(PartContext context) throws OperationException {}

    /**
     * @see beforeDispose() to override this.
     */
    public final void dispose() throws OperationException {
        beforeDispose();
        this.partContext = null;
    }

    protected/* abstract */void beforeDispose() throws OperationException {}

    public void encast(String method, Object[] args) {
        EventExchanger.encast(partContext, this, method, args);
    }

    public void decast(Part sender, String method, Object[] args) {
        EventExchanger.decast(sender, this, method, args);
    }
    //    // example - fire
    //    public void fireExampleEvent(String arg1) {
    //        encast("Example", new Object[] { arg1 });
    //    }
    //
    //    // example - listen
    //    public void listenExampleEvent(Part sender, String arg1) {
    //
    //    }
}