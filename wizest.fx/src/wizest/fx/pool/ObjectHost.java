package wizest.fx.pool;
/**
 * Pool�� ����ѵ� pool�� ��� object�� ã�� ���ϸ� null�� ���������� host�� ���� �����Ͽ� �����Ѵ�.
 * 
 */
public abstract class ObjectHost {
    private final ObjectPool pool;
    private final Class objectClass;

    public ObjectHost(ObjectPool pool, Class objectClass) {
        this.pool = pool;
        this.objectClass = objectClass;
    }

    public void destroy() {
        this.pool.destroy();
    }

    public final Object askObject() {
        Object o = this.pool.getObject();
        if (o == null) {
            try {
                o = objectClass.newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("could not instantiate the class, class name=" + objectClass.getName(), ex);
            }
        }
        beforeAskObject(o);
        return o;
    }

    public final void releaseObject(Object o) {
        beforeReleaseObject(o);
        this.pool.releaseObject(o);
    }

    public abstract void beforeAskObject(Object o);

    public abstract void beforeReleaseObject(Object o);
}
