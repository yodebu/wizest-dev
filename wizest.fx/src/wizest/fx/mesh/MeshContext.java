/*
 * Created on 2004. 8. 16
 */
package wizest.fx.mesh;

import wizest.fx.util.Context;
import wizest.fx.util.GenericContext;

/**
 * @author user
 */
public class MeshContext extends GenericContext {
    private static int uniStaticId = 0;
    private final int uniId;

    private MeshRuntime runtime;

    public int getId() {
        return uniId;
    }

    public MeshContext(Context parent, MeshRuntime runtime) {
        super(parent);
        this.runtime = runtime;
        this.uniId = uniStaticId++;
    }

    /*
     * (non-Javadoc)
     * 
     * @see wizest.fx.util.GenericContext#release()
     */
    public synchronized void release() {
        runtime = null;
        super.release();
    }

    public MeshRuntime getMeshRuntime() {
        return this.runtime;
    }

}