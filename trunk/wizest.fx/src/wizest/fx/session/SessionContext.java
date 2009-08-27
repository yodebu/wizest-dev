package wizest.fx.session;

import java.util.Enumeration;

public interface SessionContext
{
    public Enumeration getSessions();

    /**
     * session을 추가한다.
     * @param session
     */
    public void add(AbstractSession session);

    /**
     * session을 삭제한다.
     * @param sessionId
     */
    public void remove(String sessionId);

    /**
     * session id로 session을 찾는다.
     * @param sessionId
     * @return
     */
    public AbstractSession find(String sessionId);

    /**
     * 전체 session을 가져온다
     * @return
     */
    public AbstractSession[] findSessions();

    /**
     * session의 수
     * @return
     */
    public int size();

    /**
     * session context implementation 정보를 얻는다.
     * @return
     */
    public String getInfo();
}
