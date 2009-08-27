/*
 * Created on 2004. 8. 11
 */
package wizest.fx.mesh;

/**
 * @author user
 */
public interface Part {
    String getPartId();

    void start(PartContext context) throws OperationException;

    void dispose() throws OperationException;

    // event model - 1. principle

    /**
     * callback point - public event sender
     * 
     * @param toFunc
     *            information for a receiver to decide what to work
     * @param arg
     *            argument to send
     */
    void encast(String method, Object[] args);

    /**
     * callback point - public event receiver
     * 
     * @param sender
     *            event sender
     * @param toFunc
     *            information for a receiver to decide what to work
     * @param arg
     *            received argument
     */
    void decast(Part sender, String method, Object[] args);

    //    // event model - 2. application
    //    void fireSomeMethod(Object forObj1, Object forObj2);
    //
    //    void listenSomeMethod(Part sender, Object forObj1, Object forObj2) throws
    // PartException;

    // manager's rule
    //    void addPartListeners(Part part);
    //    void removePartListener();

}