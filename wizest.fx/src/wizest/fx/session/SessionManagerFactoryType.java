package wizest.fx.session;



public class SessionManagerFactoryType
{
    final public static SessionManagerFactoryType SIMPLE_SESSION_MANAGER_FACTORY=new SessionManagerFactoryType(
        SimpleSessionManagerFactory.class);

    String className=null;

    public SessionManagerFactoryType(Class sessionManagerFactoryClass)
    {
        this.className=sessionManagerFactoryClass.getName();
    }

    public SessionManagerFactoryType(String sessionManagerFactoryClassString)
    {
        this.className=sessionManagerFactoryClassString;
    }

    public String getTypeClassName()
    {
        return className;
    }

    public String toString()
    {
        return getTypeClassName();
    }
}