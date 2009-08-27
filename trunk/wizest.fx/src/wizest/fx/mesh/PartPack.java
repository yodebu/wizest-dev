/*
 * Created on 2004. 8. 19
 */
package wizest.fx.mesh;

/**
 * @author Sanghoon, Kim
 */
public class PartPack {
    public final Part part;
    public final PartContext partContext;
    public final String partName; // ex) name
    public final String partQName; // ex) name@partId

    PartPack(Part part, PartContext partContext, String partName) {
        this.part = part;
        this.partContext = partContext;
        this.partName = partName;
        this.partQName = makePartQName(part.getPartId(), partName);
    }

    /*
     * 주의: partQName 이 같을 경우 동일한 것으로 간주!!!
     */
    public boolean equals(Object obj) {
        if (obj instanceof PartPack && ((PartPack) obj).partQName.equals(this.partQName))
            return true;
        else
            return false;
    }

    public int hashCode() {
        return partQName.hashCode();
    }

    public static String makePartQName(String partId, String partName) {
        return partName + "@" + partId;
    }
}