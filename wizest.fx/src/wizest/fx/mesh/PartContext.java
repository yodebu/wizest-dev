/*
 * Created on 2004. 8. 11
 */
package wizest.fx.mesh;

import wizest.fx.util.GenericContext;

/**
 * @author user
 */
public class PartContext extends GenericContext {
    private MeshContext mc;
    private String partName;
    private String partQName;
    private String partId;

    public PartContext(MeshContext mc, String partId, String partName) {
        this.mc = mc;
        this.partId = partId;
        this.partName = partName;
        this.partQName = PartPack.makePartQName(partId, partName);
    }

    public MeshContext getMeshContext() {
        return this.mc;
    }

    public String getPartName() {
        return this.partName;
    }

    /**
     * @see PartPack.makePartQName
     * @return
     */
    public String getPartQName() {
        return this.partQName;
    }

    public String getPartId() {
        return this.partId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see wizest.fx.util.GenericContext#release()
     */
    public synchronized void release() {
        mc = null;
        partName = null;
        partQName = null;
        partId = null;
        super.release();
    }
}