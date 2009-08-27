package wizest.fx.template;

import wizest.fx.pool.ObjectPool;
import wizest.fx.pool.SimpleObjectPool;
import wizest.fx.util.Parser;

/**
 * @author wizest
 */
public class ParserHost {
    private final ObjectPool pool;
    private final Class objectClass;

    public ParserHost(Class parserClass) {
        this.pool = new SimpleObjectPool();
        this.objectClass = parserClass;
    }

    public Parser getParser() {
        Object o = this.pool.getObject();
        if (o == null) {
            try {
                o = objectClass.newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("could not instantiate the class, class name=" + objectClass.getName(), ex);
            }
        }
        return (Parser) o;
    }

    public void releaseParser(Parser parser) {
        pool.releaseObject(parser);
    }

    protected void finalize() throws Throwable {
        pool.destroy();
        super.finalize();
    }
}
