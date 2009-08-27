/*
 * Created on 2004. 8. 17
 */
package wizest.fx.mesh;

/**
 * @author user
 */
public interface MeshListener {


    /**
     * @param e
     */
    void partConnected(MeshEvent e);
    
    /**
     * @param e
     */
    void partDisconnected(MeshEvent e);

    /**
     * @param e
     */
    void partIsolated(MeshEvent e);

    
}
