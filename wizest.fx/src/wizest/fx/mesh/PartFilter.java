/*
 * Created on 2004. 8. 11
 */
package wizest.fx.mesh;

/**
 * @author user
 */
public interface PartFilter {
    /**
     * @param discriptor
     * @return ������ ��� true / ������ ��� false
     */
    boolean filterPart(PartDescriptor discriptor);

}