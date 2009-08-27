/*
 * Created on 2004. 8. 18
 */
package wizest.fx.mesh;

/**
 * @author user
 */
public interface MeshRuntimeListener {
    
    void afterMeshRuntimeStart(MeshRuntime runtime);

    void beforeMeshRuntimeDispose(MeshRuntime runtime);

    void afterAddNewPartPack(MeshRuntime runtime, PartPack partPack);

    void beforeRemovePartPack(MeshRuntime runtime, PartPack partPack);
}