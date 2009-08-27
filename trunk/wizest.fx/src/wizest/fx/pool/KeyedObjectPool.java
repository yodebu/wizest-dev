package wizest.fx.pool;

public interface KeyedObjectPool
{
    /**
     * pool�� object�� �ִ´�.
     * @param o
     */
    public void addObject( Object key, Object o );

    /**
     * pool���� object�� �����´�.
     * @return null if not exists
     */
    public Object getObject( Object key );

    /**
     * ����� object�� �ݳ��Ѵ�.
     * @param o
     */
    public void releaseObject( Object key, Object o );

    /**
     * pool�� ��� object�� �����Ѵ�.
     */
    public void clear();

    /**
     * pool�� �Ҹ��Ų��.
     */
    public void destroy();

}