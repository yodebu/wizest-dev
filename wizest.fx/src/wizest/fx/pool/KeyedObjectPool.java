package wizest.fx.pool;

public interface KeyedObjectPool
{
    /**
     * pool에 object를 넣는다.
     * @param o
     */
    public void addObject( Object key, Object o );

    /**
     * pool에서 object를 가져온다.
     * @return null if not exists
     */
    public Object getObject( Object key );

    /**
     * 사용한 object를 반납한다.
     * @param o
     */
    public void releaseObject( Object key, Object o );

    /**
     * pool의 모든 object를 정리한다.
     */
    public void clear();

    /**
     * pool을 소멸시킨다.
     */
    public void destroy();

}