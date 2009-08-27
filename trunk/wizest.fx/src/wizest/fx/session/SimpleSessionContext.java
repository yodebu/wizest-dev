package wizest.fx.session;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class SimpleSessionContext implements SessionContext,Serializable
{
    Hashtable sessions;

    public SimpleSessionContext()
    {
        this.sessions=new Hashtable();
    }

    public Enumeration getSessions()
    {
        return this.sessions.elements();
    }

    public void add(AbstractSession session)
    {
        this.sessions.put(session.getId(),session);
    }

    public void remove(String sessionId)
    {
        this.sessions.remove(sessionId);
    }

    public AbstractSession find(String sessionId)
    {
        return(AbstractSession)this.sessions.get(sessionId);
    }

    public AbstractSession[] findSessions()
    {
        return(AbstractSession[])this.sessions.values().toArray(new AbstractSession[0]);
    }

    public String getInfo()
    {
        return this.getClass().getName();
    }

    public int size()
    {
        return this.sessions.size();
    }
}
