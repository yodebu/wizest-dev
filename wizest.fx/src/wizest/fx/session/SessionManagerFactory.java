package wizest.fx.session;

import wizest.fx.configuration.Configuration;

/**
 * SessionManagerFactory
 *
 * <pre>
 *      SessionManagerFactory factory = SessionManagerFactory.createFactory();
 *      SessionManager manager = factory.createManager(config);
 * </pre>
 */

public abstract class SessionManagerFactory
{
    private static SessionManagerFactoryType type=SessionManagerFactoryType.SIMPLE_SESSION_MANAGER_FACTORY;

    public static void setFactoryClass(SessionManagerFactoryType type)
    {
        SessionManagerFactory.type=type;
    }

    public static SessionManagerFactoryType getFactoryType()
    {
        return type;
    }

    public static SessionManagerFactory createFactory()
    {
        try {
            String factoryClass=type.getTypeClassName();
            return(SessionManagerFactory)Class.forName(factoryClass).newInstance();

        }
        catch(Throwable t) {
//            t.printStackTrace();
            return null;
        }
    }

    public abstract SessionManager createManager(Configuration config);

    public abstract SessionManager createManager(String name);
}
