/*
 * Created on 2004. 8. 13
 */
package wizest.fx.mesh;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.logging.Logger;

import wizest.fx.logging.LogBroker;


/**
 * @author Sanghoon, Kim
 */
public class EventExchanger {
    private static Logger log = LogBroker.getLogger(EventExchanger.class);

    private EventExchanger() {

    }

    public static void encast(PartContext partContext, Part owner, String method, Object[] args) {
        Mesh mesh = partContext.getMeshContext().getMeshRuntime().getMesh();

        Iterator i = mesh.getListenPartPacks(partContext.getPartQName());
        while (i.hasNext()) {
            decast(owner, ((PartPack) i.next()).part, method, args);
        }
    }

    /**
     * @param sender
     * @param owner
     * @param method
     * @param args
     * @return false if no callback method corresponding event
     * @throws OperationException
     */
    public static boolean decast(Part sender, Part owner, String method, Object[] args) {
        // call individual 'listen' method

        Method m = PartIntrospector.getListenMethod(owner.getClass(), method, args);
        if (m == null)
            return false;

        try {
            Object[] newArgs = new Object[args.length + 1];
            newArgs[0] = sender;
            System.arraycopy(args, 0, newArgs, 1, args.length);
            m.invoke(owner, newArgs);
            //        } catch (IllegalArgumentException e) {
            //            throw new PartException(e);
            //        } catch (IllegalAccessException e) {
            //            throw new PartException(e);
            //        } catch (InvocationTargetException e) {
            //            if (e.getCause() instanceof PartException) {
            //                throw (PartException) e.getCause();
            //            } else
            //                throw new PartException(e.getCause());
            //        }
        } catch (Throwable e) {
            log.severe(e.getMessage() + ":owner=" + owner + ",sender=" + sender + ",method=" + method);
        }
        return true;
    }
}

