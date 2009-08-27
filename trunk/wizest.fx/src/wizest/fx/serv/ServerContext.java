/*
 * Created on 2004. 9. 7
 */
package wizest.fx.serv;

import wizest.fx.util.GenericContext;

/**
 * @author wizest
 */
public class ServerContext extends GenericContext {
    private final Server server;

    public ServerContext(/* Context parent, */Server server) {
        //        super(parent);
        this.server = server;
    }

    public Server getServer() {
        return this.server;
    }
}